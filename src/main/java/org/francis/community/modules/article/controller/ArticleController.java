package org.francis.community.modules.article.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.exception.ServiceException;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.core.utils.SecurityUtils;
import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.model.Tag;
import org.francis.community.modules.article.model.request.ArticleQueryRequest;
import org.francis.community.modules.article.model.request.CreateArticleRequest;
import org.francis.community.modules.article.model.request.UpdateArticleRequest;
import org.francis.community.modules.article.model.vo.ArticleVO;
import org.francis.community.modules.article.service.ArticleService;
import org.francis.community.modules.article.service.TagService;
import org.francis.community.modules.user.model.User;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
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

    private final ArticleService articleService;

    private final TagService tagService;

    private final UserService userService;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @GetMapping("/list")
    @ApiOperation(value = "帖子分页查询")
    public AjaxResult findArticlePageList(@Validated PageQueryRequest pageQueryRequest,
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
        UserDTO user = userService.findUserById(article.getUserId());
        Tag tag = tagService.findTagById(article.getTagId());
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        articleVO.setNickname(user.getNickname());
        articleVO.setAvatarUrl(user.getAvatarUrl());
        articleVO.setTagName(tag.getName());
        return AjaxResult.success(articleVO);
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建帖子")
    public AjaxResult createArticle(@RequestBody @Validated CreateArticleRequest createArticleRequest) {
        // 参数校验
        Tag tag = tagService.findTagById(createArticleRequest.getTagId());
        if (Objects.isNull(tag)) {
            return AjaxResult.error("标签不存在");
        }
        // 创建帖子
        boolean result = articleService.createArticle(createArticleRequest.getTitle(),
                createArticleRequest.getContent(),
                createArticleRequest.getTagId(),
                SecurityUtils.getUserId());
        if (result) {
            return AjaxResult.success("创建成功");
        }
        return AjaxResult.error("创建失败");
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新帖子")
    public AjaxResult updateArticle(@RequestBody @Validated UpdateArticleRequest updateArticleRequest) {
        Long articleId = updateArticleRequest.getId();
        String title = updateArticleRequest.getTitle();
        String content = updateArticleRequest.getContent();
        Long tagId = updateArticleRequest.getTagId();
        boolean result = articleService.updateArticle(articleId, title, content, tagId);
        if (result) {
            return AjaxResult.success("更新成功");
        }
        return AjaxResult.error("更新成功");
    }

    @DeleteMapping("/remove/{articleId}")
    @ApiOperation(value = "删除帖子")
    public AjaxResult removeArticle(@PathVariable Long articleId) {
        Article article = articleService.findArticleById(articleId);
        // 检验帖子是否存在 && 是否属于登录用户
        boolean flag = Objects.nonNull(article) && Objects.equals(article.getUserId(), SecurityUtils.getUserId());
        if (flag) {
            boolean result = articleService.removeById(articleId);
            if (result) {
                return AjaxResult.success("删除成功");
            }
        }
        return AjaxResult.error("删除失败");
    }
}
