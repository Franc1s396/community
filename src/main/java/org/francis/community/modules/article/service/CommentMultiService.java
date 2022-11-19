package org.francis.community.modules.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.modules.article.model.CommentMulti;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
public interface CommentMultiService extends IService<CommentMulti> {

    /**
     * 分页查询二级评论
     * @param pageQueryRequest 分页参数
     * @param commentId 二级评论id
     * @return 二级评论列表
     */
    IPage<CommentMulti> findCommentMultiPageList(PageQueryRequest pageQueryRequest, Long commentId);

    void createCommentMulti(Long commentId, String content, Long userId);

    CommentMulti findCommentMultiById(Long commentMultiId);
}
