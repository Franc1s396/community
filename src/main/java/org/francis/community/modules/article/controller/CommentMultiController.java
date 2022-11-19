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
import org.francis.community.modules.article.model.Comment;
import org.francis.community.modules.article.model.CommentMulti;
import org.francis.community.modules.article.model.request.CommentCreateRequest;
import org.francis.community.modules.article.model.request.CommentMultiCreateRequest;
import org.francis.community.modules.article.model.vo.CommentMultiVO;
import org.francis.community.modules.article.model.vo.CommentVO;
import org.francis.community.modules.article.service.CommentMultiService;
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
@Api(tags = "二级评论接口")
@RestController
@RequestMapping("/comment-multi")
@RequiredArgsConstructor
public class CommentMultiController {

    private final CommentMultiService commentMultiService;

    private final CommentService commentService;

    private final UserService userService;

    @GetMapping("/list")
    @ApiOperation("分页查询二级评论")
    public AjaxResult findCommentMultiPageList(PageQueryRequest pageQueryRequest,
                                               @RequestParam Long commentId){

        IPage<CommentMulti> commentMultiPageList = commentMultiService.findCommentMultiPageList(pageQueryRequest, commentId);
        List<CommentMulti> commentMultiRecords = commentMultiPageList.getRecords();
        // 获取用户id集合
        Set<Long> userIds = commentMultiRecords.stream().map(CommentMulti::getUserId).collect(Collectors.toSet());
        List<UserDTO> userList = userService.findUserListByIds(new ArrayList<>(userIds));
        // page转换
        IPage<CommentMultiVO> commentVOPageList = commentMultiPageList.convert(commentMulti -> {
            CommentMultiVO commentMultiVO = new CommentMultiVO();
            BeanUtils.copyProperties(commentMulti, commentMultiVO);
            UserDTO user = userList.stream()
                    .filter(userDTO -> Objects.equals(userDTO.getId(), commentMulti.getUserId()))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("用户未找到"));
            commentMultiVO.setUserNickname(user.getNickname());
            commentMultiVO.setUserAvatarUrl(user.getAvatarUrl());
            return commentMultiVO;
        });

        return AjaxResult.success();
    }

    @PostMapping("/create")
    @ApiOperation("发布二级评论")
    public AjaxResult createComment(@RequestBody @Validated CommentMultiCreateRequest commentCreateRequest){
        String content = commentCreateRequest.getContent();
        Long commentId = commentCreateRequest.getCommentId();
        Long userId = SecurityUtils.getUserId();

        Comment comment = commentService.findCommentById(commentId);
        if (Objects.isNull(comment)) {
            throw new ServiceException(CodeEnums.COMMENT_NOT_FOUND.getCode(), CodeEnums.COMMENT_NOT_FOUND.getMessage());
        }

        boolean result=commentMultiService.createCommentMulti(commentId,content, userId);

        if (result) {
            return AjaxResult.success("发布成功");
        }
        return AjaxResult.error();
    }

    @DeleteMapping("/remove/{commentMultiId}")
    @ApiOperation(value = "删除二级评论")
    public AjaxResult removeCommentMulti(@PathVariable Long commentMultiId) {
        CommentMulti commentMulti = commentMultiService.findCommentMultiById(commentMultiId);
        // 检验帖子是否存在 && 是否属于登录用户
        boolean flag = Objects.nonNull(commentMulti) && Objects.equals(commentMulti.getUserId(), SecurityUtils.getUserId());
        if (flag) {
            boolean result = commentMultiService.removeById(commentMultiId);
            if (result) {
                return AjaxResult.success("删除成功");
            }
        }
        return AjaxResult.error("删除失败");
    }
}

