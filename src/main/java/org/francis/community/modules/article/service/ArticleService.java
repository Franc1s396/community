package org.francis.community.modules.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.modules.article.model.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.francis.community.modules.article.model.request.ArticleQueryRequest;
import org.francis.community.modules.article.model.request.CreateArticleRequest;
import org.francis.community.modules.article.model.request.UpdateArticleRequest;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author francis
 * @since 2022-11-17
 */
public interface ArticleService extends IService<Article> {

    /**
     * 创建帖子
     */
    Article createArticle(CreateArticleRequest createArticleRequest);

    /**
     * 分页查询帖子
     *
     * @param pageQueryRequest    分页参数
     * @param articleQueryRequest 帖子查询参数
     * @return 帖子分页
     */
    IPage<Article> findArticlePageList(PageQueryRequest pageQueryRequest, ArticleQueryRequest articleQueryRequest);

    /**
     * 根据id查询帖子
     *
     * @param articleId 帖子id
     * @return 帖子信息
     */
    Article findArticleById(Long articleId);

    /**
     * 更新帖子评论数量
     *
     * @param articleId 帖子id
     * @return 更新结果
     */
    void incrementCommentCount(Long articleId);

    /**
     * 删除帖子
     *
     * @param articleId 帖子id
     */
    void removeArticle(Long articleId);

    void updateBatchLikeCount(List<Article> articleList);

    void updateArticle(UpdateArticleRequest updateArticleRequest);
}
