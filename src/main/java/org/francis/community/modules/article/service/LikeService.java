package org.francis.community.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.francis.community.modules.article.model.Like;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author francis
 * @since 2022-11-20
 */
public interface LikeService extends IService<Like> {

    /**
     * 点赞帖子
     * @param articleId 帖子id
     */
    void likeArticle(Long articleId);

    void cancelLikeArticle(Long articleId);

    boolean isLike(Long articleId);

    void updateBatchLike(List<Like> likeList);
}
