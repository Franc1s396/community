package org.francis.community.modules.article.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.enums.CodeEnums;
import org.francis.community.core.exception.ServiceException;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.core.utils.SecurityUtils;
import org.francis.community.modules.article.convert.CommentConvert;
import org.francis.community.modules.article.convert.CommentMultiConvert;
import org.francis.community.modules.article.mapper.CommentMapper;
import org.francis.community.modules.article.model.Comment;
import org.francis.community.modules.article.model.CommentMulti;
import org.francis.community.modules.article.mapper.CommentMultiMapper;
import org.francis.community.modules.article.model.request.CommentMultiCreateRequest;
import org.francis.community.modules.article.service.CommentMultiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentMultiServiceImpl extends ServiceImpl<CommentMultiMapper, CommentMulti> implements CommentMultiService {

    private final CommentMultiMapper commentMultiMapper;

    private final CommentMapper commentMapper;

    private final CommentMultiConvert commentMultiConvert;

    @Override
    public IPage<CommentMulti> findCommentMultiPageList(PageQueryRequest pageQueryRequest, Long commentId) {
        Page<CommentMulti> commentMultiPage = new Page<>(pageQueryRequest.getPage(), pageQueryRequest.getLimit());
        return commentMultiMapper.selectPage(commentMultiPage, Wrappers.lambdaQuery(CommentMulti.class)
                .eq(CommentMulti::getCommentId, commentId));
    }

    @Override
    public void createCommentMulti(CommentMultiCreateRequest commentCreateRequest) {
        Long commentId = commentCreateRequest.getCommentId();
        Long userId = SecurityUtils.getUserId();

        Comment comment = commentMapper.selectOne(Wrappers.lambdaQuery(Comment.class).eq(Comment::getId, commentId));
        if (Objects.isNull(comment)) {
            throw new ServiceException(CodeEnums.COMMENT_NOT_FOUND.getCode(), CodeEnums.COMMENT_NOT_FOUND.getMessage());
        }

        CommentMulti commentMulti = commentMultiConvert.createRequest2Entity(commentCreateRequest, userId);

        commentMultiMapper.insert(commentMulti);
        log.info("用户发布评论 userId:{},一级评论id:{},二级评论id:{}", userId, commentId, commentMulti.getId());
    }

    @Override
    public CommentMulti findCommentMultiById(Long commentMultiId) {
        return commentMultiMapper.selectOne(Wrappers.lambdaQuery(CommentMulti.class).eq(CommentMulti::getId, commentMultiId));
    }
}
