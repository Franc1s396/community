package org.francis.community.modules.article.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.francis.community.core.enums.CodeEnums;
import org.francis.community.core.exception.ServiceException;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.core.utils.SecurityUtils;
import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.model.Like;
import org.francis.community.modules.article.model.Tag;
import org.francis.community.modules.article.model.request.ArticleQueryRequest;
import org.francis.community.modules.article.model.request.CreateArticleRequest;
import org.francis.community.modules.article.model.request.UpdateArticleRequest;
import org.francis.community.modules.article.model.vo.ArticleInfoVO;
import org.francis.community.modules.article.model.vo.ArticleVO;
import org.francis.community.modules.article.service.ArticleService;
import org.francis.community.modules.article.service.LikeService;
import org.francis.community.modules.article.service.TagService;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Api(tags = "帖子模块")
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private static final String ARTICLE_PV_PREFIX = "community:article:pv:";

    private final ArticleService articleService;

    private final TagService tagService;

    private final UserService userService;

    private final StringRedisTemplate redisTemplate;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final LikeService likeService;

    @GetMapping("/list")
    @ApiOperation(value = "帖子分页查询")
    public AjaxResult findArticlePageList(PageQueryRequest pageQueryRequest,
                                          ArticleQueryRequest articleQueryRequest) throws ExecutionException, InterruptedException {
        IPage<Article> articlePageList = articleService.findArticlePageList(pageQueryRequest, articleQueryRequest);
        List<Article> articleList = articlePageList.getRecords();

        // 查询标签列表
        CompletableFuture<List<Tag>> tagFuture = CompletableFuture.supplyAsync(() -> {
            Set<Long> tagIds = articleList.stream().map(Article::getTagId).collect(Collectors.toSet());
            return tagService.findTagListByIds(new ArrayList<>(tagIds));
        }, threadPoolTaskExecutor);

        // 查询用户列表
        CompletableFuture<List<UserDTO>> userFuture = CompletableFuture.supplyAsync(() -> {
            Set<Long> userIds = articleList.stream().map(Article::getUserId).collect(Collectors.toSet());
            return userService.findUserListByIds(new ArrayList<>(userIds));
        }, threadPoolTaskExecutor);

        CompletableFuture.allOf(tagFuture, userFuture);
        List<Tag> tagList = tagFuture.get();
        List<UserDTO> userList = userFuture.get();
        // 转换VO
        IPage<ArticleVO> articleVOPageList = articlePageList.convert(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);
            // 标签过滤
            Tag tag = tagList.stream()
                    .filter(tag1 -> Objects.equals(articleVO.getTagId(), tag1.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("标签未找到"));
            articleVO.setTagName(tag.getName());
            // 用户列表过滤
            UserDTO userDTO = userList.stream()
                    .filter(user -> Objects.equals(articleVO.getUserId(), user.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("用户未找到"));
            articleVO.setNickname(userDTO.getNickname());
            articleVO.setAvatarUrl(userDTO.getAvatarUrl());
            return articleVO;
        });

        return AjaxResult.success(articleVOPageList);
    }

    @GetMapping("/info/{articleId}")
    @ApiOperation(value = "帖子详情")
    public AjaxResult findArticleInfo(@PathVariable Long articleId) {
        Article article = articleService.findArticleById(articleId);
        if (Objects.isNull(article)) {
            throw new ServiceException(CodeEnums.ARTICLE_NOT_FOUND.getCode(), CodeEnums.ARTICLE_NOT_FOUND.getMessage());
        }
        UserDTO user = userService.findUserById(article.getUserId());
        Tag tag = tagService.findTagById(article.getTagId());
        boolean likeStatus=likeService.isLike(articleId);

        // 转换VO
        ArticleInfoVO articleInfoVO = new ArticleInfoVO();
        BeanUtils.copyProperties(article, articleInfoVO);
        articleInfoVO.setNickname(user.getNickname());
        articleInfoVO.setAvatarUrl(user.getAvatarUrl());
        articleInfoVO.setTagName(tag.getName());
        articleInfoVO.setLikeStatus(likeStatus);

        // 文章pv redis+mysql
        String pageViewCount = redisTemplate.opsForValue().get(ARTICLE_PV_PREFIX + articleId);
        if (StringUtils.hasText(pageViewCount)) {
            articleInfoVO.setPageViewCount(articleInfoVO.getPageViewCount() + Integer.parseInt(pageViewCount));
        }
        // pv自增
        redisTemplate.opsForValue().increment(ARTICLE_PV_PREFIX + articleId);

        return AjaxResult.success(articleInfoVO);
    }

    @PostMapping("/create")
    @ApiOperation(value = "发布帖子")
    public AjaxResult createArticle(@RequestBody @Validated CreateArticleRequest createArticleRequest) {
        // 创建帖子
        String title = createArticleRequest.getTitle();
        String content = createArticleRequest.getContent();
        Long tagId = createArticleRequest.getTagId();
        Long userId = SecurityUtils.getUserId();
        articleService.createArticle(title, content, tagId, userId);

        //TODO 为粉丝推送消息

        return AjaxResult.error("发布成功");
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新帖子")
    public AjaxResult updateArticle(@RequestBody @Validated UpdateArticleRequest updateArticleRequest) {
        Long articleId = updateArticleRequest.getId();
        String title = updateArticleRequest.getTitle();
        String content = updateArticleRequest.getContent();
        Long tagId = updateArticleRequest.getTagId();
        articleService.updateArticle(articleId, title, content, tagId);
        return AjaxResult.success("更新成功");
    }

    @DeleteMapping("/remove/{articleId}")
    @ApiOperation(value = "删除帖子")
    public AjaxResult removeArticle(@PathVariable Long articleId) {
        articleService.removeArticle(articleId);
        return AjaxResult.success("删除成功");
    }
}

