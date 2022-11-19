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
import org.francis.community.modules.article.model.Comment;
import org.francis.community.modules.article.model.request.CommentCreateRequest;
import org.francis.community.modules.article.model.request.CommentQueryRequest;
import org.francis.community.modules.article.model.vo.CommentVO;
import org.francis.community.modules.article.service.ArticleService;
import org.francis.community.modules.article.service.CommentService;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Api(tags = "一级评论接口")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final ArticleService articleService;

    private final UserService userService;

    @GetMapping("/list")
    @ApiOperation(value = "分页查询一级评论")
    public AjaxResult findCommentPageList(PageQueryRequest pageQueryRequest,
                                          CommentQueryRequest commentQueryRequest) {
        Long articleId = commentQueryRequest.getArticleId();
        Long userId = commentQueryRequest.getUserId();
        // 如果两个参数都为空则不通过校验
        if (Objects.isNull(articleId) && Objects.isNull(userId)) {
            return AjaxResult.error(CodeEnums.PARAM_ERROR.getCode(), CodeEnums.PARAM_ERROR.getMessage());
        }

        IPage<Comment> commentPageList = commentService.findCommentPageList(pageQueryRequest, commentQueryRequest);
        List<Comment> commentRecords = commentPageList.getRecords();
        // 获取用户id集合
        Set<Long> userIds = commentRecords.stream().map(Comment::getUserId).collect(Collectors.toSet());
        List<UserDTO> userList = userService.findUserListByIds(new ArrayList<>(userIds));
        // page转换
        IPage<CommentVO> commentVOPageList = commentPageList.convert(comment -> {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            UserDTO user = userList.stream()
                    .filter(userDTO -> Objects.equals(userDTO.getId(), comment.getUserId()))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("用户未找到"));
            commentVO.setUserNickname(user.getNickname());
            commentVO.setUserAvatarUrl(user.getAvatarUrl());
            return commentVO;
        });

        return AjaxResult.success(commentVOPageList);
    }

    @PostMapping("/create")
    @ApiOperation("发布一级评论")
    public AjaxResult createComment(@RequestBody @Validated CommentCreateRequest commentCreateRequest) {
        String content = commentCreateRequest.getContent();
        Long articleId = commentCreateRequest.getArticleId();
        Long userId = SecurityUtils.getUserId();
        // 添加一级评论
        commentService.createComment(articleId, content, userId);
        // 更新文章评论数量
        articleService.incrementCommentCount(articleId);

        return AjaxResult.success("发布成功");
    }

    @DeleteMapping("/remove/{commentId}")
    @ApiOperation(value = "删除一级评论")
    public AjaxResult removeComment(@PathVariable Long commentId) {
        Comment comment = commentService.findCommentById(commentId);
        // 检验帖子是否存在 && 是否属于登录用户
        boolean flag = Objects.nonNull(comment) && Objects.equals(comment.getUserId(), SecurityUtils.getUserId());
        if (flag) {
            boolean result = commentService.removeById(commentId);
            if (result) {
                return AjaxResult.success("删除成功");
            }
        }
        return AjaxResult.error("删除失败");
    }
}

