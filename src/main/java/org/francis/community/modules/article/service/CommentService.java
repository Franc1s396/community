package org.francis.community.modules.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.modules.article.model.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.francis.community.modules.article.model.request.CommentCreateRequest;
import org.francis.community.modules.article.model.request.CommentQueryRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询一级评论
     * @param pageQueryRequest 分页条件
     * @param commentQueryRequest 查询条件
     * @return 评论分页
     */
    IPage<Comment> findCommentPageList(PageQueryRequest pageQueryRequest, CommentQueryRequest commentQueryRequest);


    /**
     * 根据id查询一级评论
     * @param commentId 一级评论id
     * @return 一级评论
     */
    Comment findCommentById(Long commentId);

    void createComment(CommentCreateRequest commentCreateRequest);
}
