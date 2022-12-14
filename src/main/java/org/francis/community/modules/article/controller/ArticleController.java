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
import org.francis.community.modules.article.convert.ArticleConvert;
import org.francis.community.modules.article.listener.ArticlePublishEvent;
import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.model.Tag;
import org.francis.community.modules.article.model.request.ArticleQueryRequest;
import org.francis.community.modules.article.model.request.CreateArticleRequest;
import org.francis.community.modules.article.model.request.UpdateArticleRequest;
import org.francis.community.modules.article.model.vo.ArticleInfoVO;
import org.francis.community.modules.article.model.vo.ArticleVO;
import org.francis.community.modules.article.service.ArticleService;
import org.francis.community.modules.article.service.LikeService;
import org.francis.community.modules.article.service.TagService;
import org.francis.community.modules.attention.service.AttentionService;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
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
 * ???????????????
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Api(tags = "????????????")
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

    private final ArticleConvert articleConvert;

    private final ApplicationContext applicationContext;

    private final AttentionService attentionService;

    @GetMapping("/list")
    @ApiOperation(value = "??????????????????")
    public AjaxResult findArticlePageList(PageQueryRequest pageQueryRequest,
                                          ArticleQueryRequest articleQueryRequest) throws ExecutionException, InterruptedException {
        IPage<Article> articlePageList = articleService.findArticlePageList(pageQueryRequest, articleQueryRequest);
        List<Article> articleList = articlePageList.getRecords();

        // ??????????????????
        CompletableFuture<List<Tag>> tagFuture = CompletableFuture.supplyAsync(() -> {
            Set<Long> tagIds = articleList.stream().map(Article::getTagId).collect(Collectors.toSet());
            return tagService.findTagListByIds(new ArrayList<>(tagIds));
        }, threadPoolTaskExecutor);

        // ??????????????????
        CompletableFuture<List<UserDTO>> userFuture = CompletableFuture.supplyAsync(() -> {
            Set<Long> userIds = articleList.stream().map(Article::getUserId).collect(Collectors.toSet());
            return userService.findUserListByIds(new ArrayList<>(userIds));
        }, threadPoolTaskExecutor);

        CompletableFuture.allOf(tagFuture, userFuture);
        List<Tag> tagList = tagFuture.get();
        List<UserDTO> userList = userFuture.get();
        // ??????VO
        IPage<ArticleVO> articleVOPageList = articlePageList.convert(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article, articleVO);

            // ????????????
            Tag tag = tagList.stream()
                    .filter(tag1 -> Objects.equals(articleVO.getTagId(), tag1.getId()))
                    .findFirst()
                    .get();

            articleVO.setTagName(tag.getName());

            // ??????????????????
            UserDTO userDTO = userList.stream()
                    .filter(user -> Objects.equals(articleVO.getUserId(), user.getId()))
                    .findFirst()
                    .get();

            articleVO.setNickname(userDTO.getNickname());
            articleVO.setAvatarUrl(userDTO.getAvatarUrl());

            return articleVO;
        });

        return AjaxResult.success(articleVOPageList);
    }

    @GetMapping("/info/{articleId}")
    @ApiOperation(value = "????????????")
    public AjaxResult findArticleInfo(@PathVariable Long articleId) {
        Article article = articleService.findArticleById(articleId);
        if (Objects.isNull(article)) {
            throw new ServiceException(CodeEnums.ARTICLE_NOT_FOUND.getCode(), CodeEnums.ARTICLE_NOT_FOUND.getMessage());
        }
        UserDTO user = userService.findUserById(article.getUserId());
        Tag tag = tagService.findTagById(article.getTagId());
        boolean likeStatus = likeService.isLike(articleId);

        // ??????VO
        ArticleInfoVO articleInfoVO = articleConvert.entity2VO(article, user, tag.getName(), likeStatus);

        // ??????pv redis+mysql
        String pageViewCount = redisTemplate.opsForValue().get(ARTICLE_PV_PREFIX + articleId);
        if (StringUtils.hasText(pageViewCount)) {
            articleInfoVO.setPageViewCount(articleInfoVO.getPageViewCount() + Integer.parseInt(pageViewCount));
        }
        // pv??????
        redisTemplate.opsForValue().increment(ARTICLE_PV_PREFIX + articleId);

        return AjaxResult.success(articleInfoVO);
    }

    @PostMapping("/create")
    @ApiOperation(value = "????????????")
    public AjaxResult createArticle(@RequestBody @Validated CreateArticleRequest createArticleRequest) {
        // ????????????
        Article article = articleService.createArticle(createArticleRequest);

        // ???????????????-?????????????????????(??????/????????????)
        ArticlePublishEvent publishEvent = new ArticlePublishEvent();
        Long userId = SecurityUtils.getUserId();
        publishEvent.setUserId(userId);
        publishEvent.setArticleId(article.getId());
        applicationContext.publishEvent(publishEvent);

        return AjaxResult.error("????????????");
    }

    @PutMapping("/update")
    @ApiOperation(value = "????????????")
    public AjaxResult updateArticle(@RequestBody @Validated UpdateArticleRequest updateArticleRequest) {
        articleService.updateArticle(updateArticleRequest);
        return AjaxResult.success("????????????");
    }

    @DeleteMapping("/remove/{articleId}")
    @ApiOperation(value = "????????????")
    public AjaxResult removeArticle(@PathVariable Long articleId) {
        articleService.removeArticle(articleId);
        return AjaxResult.success("????????????");
    }
}

