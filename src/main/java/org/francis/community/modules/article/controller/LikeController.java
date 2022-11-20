package org.francis.community.modules.article.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.modules.article.model.request.ArticleLikeRequest;
import org.francis.community.modules.article.service.LikeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author francis
 * @since 2022-11-20
 */
@Api(tags = "用户点赞接口")
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    @ApiOperation("点赞帖子")
    public AjaxResult likeArticle(@RequestBody @Validated ArticleLikeRequest articleLikeRequest) {
        Boolean isLike = articleLikeRequest.getIsLike();
        Long articleId = articleLikeRequest.getArticleId();
        if (isLike) {
            likeService.likeArticle(articleId);
        } else {
            likeService.cancelLikeArticle(articleId);
        }
        return AjaxResult.success();
    }
}

