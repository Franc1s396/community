package org.francis.community.modules.article.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.core.utils.SecurityUtils;
import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.mapper.ArticleMapper;
import org.francis.community.modules.article.model.request.ArticleQueryRequest;
import org.francis.community.modules.article.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createArticle(String title, String content, Long tagId, Long userId) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setTagId(tagId);
        article.setUserId(userId);
        boolean result = save(article);
        if (result) {
            log.info("创建帖子,用户id:{},帖子id:{},帖子标题:{}", userId, article.getUserId(), title);
        }
        return result;
    }

    @Override
    public IPage<Article> findArticlePageList(PageQueryRequest pageQueryRequest, ArticleQueryRequest articleQueryRequest) {
        // 分页参数
        Page<Article> articlePage = new Page<>(pageQueryRequest.getPage(), pageQueryRequest.getLimit());
        Long id = articleQueryRequest.getId();
        Long tagId = articleQueryRequest.getTagId();
        Long userId = articleQueryRequest.getUserId();
        String title = articleQueryRequest.getTitle();
        return page(articlePage,
                Wrappers.lambdaQuery(Article.class)
                        .eq(Objects.nonNull(id), Article::getId, id)
                        .eq(Objects.nonNull(tagId), Article::getTagId, tagId)
                        .eq(Objects.nonNull(userId), Article::getUserId, userId)
                        .like(StringUtils.hasText(title), Article::getTitle, title));
    }

    @Override
    public Article findArticleById(Long articleId) {
        return getOne(Wrappers.lambdaQuery(Article.class).eq(Article::getId, articleId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticle(Long articleId, String title, String content, Long tagId) {
        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        boolean result = update(Wrappers.lambdaUpdate(Article.class)
                .eq(Article::getId, articleId)
                .set(StringUtils.hasText(title), Article::getTitle, title)
                .set(StringUtils.hasText(content), Article::getContent, content)
                .set(Objects.nonNull(tagId), Article::getTagId, tagId));
        if (result) {
            log.info("用户id:{},username:{},更新了帖子 id:{}", userId, username, articleId);
        }
        return result;
    }
}
