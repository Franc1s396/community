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
import org.francis.community.modules.article.mapper.TagMapper;
import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.mapper.ArticleMapper;
import org.francis.community.modules.article.model.Tag;
import org.francis.community.modules.article.model.request.ArticleQueryRequest;
import org.francis.community.modules.article.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
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
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private final StringRedisTemplate redisTemplate;

    private final ArticleMapper articleMapper;

    private final TagMapper tagMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createArticle(String title, String content, Long tagId, Long userId) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setTagId(tagId);
        article.setUserId(userId);
        articleMapper.insert(article);
        log.info("创建帖子,用户id:{},帖子id:{},帖子标题:{}", userId, article.getUserId(), title);
    }

    @Override
    public IPage<Article> findArticlePageList(PageQueryRequest pageQueryRequest, ArticleQueryRequest articleQueryRequest) {
        // 分页参数
        Page<Article> articlePage = new Page<>(pageQueryRequest.getPage(), pageQueryRequest.getLimit());

        Long id = articleQueryRequest.getId();
        Long tagId = articleQueryRequest.getTagId();
        Long userId = articleQueryRequest.getUserId();
        String title = articleQueryRequest.getTitle();

        return articleMapper.selectPage(articlePage, Wrappers.lambdaQuery(Article.class)
                .eq(Objects.nonNull(id), Article::getId, id)
                .eq(Objects.nonNull(tagId), Article::getTagId, tagId)
                .eq(Objects.nonNull(userId), Article::getUserId, userId)
                .like(StringUtils.hasText(title), Article::getTitle, title));
    }

    @Override
    public Article findArticleById(Long articleId) {
        return articleMapper.selectOne(Wrappers.lambdaQuery(Article.class).eq(Article::getId, articleId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Long articleId, String title, String content, Long tagId) {
        Article article = findArticleById(articleId);
        if (Objects.isNull(article)) {
            throw new ServiceException(CodeEnums.ARTICLE_NOT_FOUND.getCode(), CodeEnums.ARTICLE_NOT_FOUND.getMessage());
        }

        Tag tag = tagMapper.selectOne(Wrappers.lambdaQuery(Tag.class).eq(Tag::getId, tagId));
        if (Objects.isNull(tag)) {
            throw new ServiceException(CodeEnums.TAG_NOT_FOUND.getCode(), CodeEnums.TAG_NOT_FOUND.getMessage());
        }

        Long userId = SecurityUtils.getUserId();
        String username = SecurityUtils.getUsername();
        articleMapper.update(article, Wrappers.lambdaUpdate(Article.class)
                .eq(Article::getId, articleId)
                .set(StringUtils.hasText(title), Article::getTitle, title)
                .set(StringUtils.hasText(content), Article::getContent, content)
                .set(Objects.nonNull(tagId), Article::getTagId, tagId));

        log.info("用户id:{},username:{},更新了帖子 id:{}", userId, username, articleId);
    }

    @Override
    public void incrementCommentCount(Long articleId) {
        update(Wrappers.lambdaUpdate(Article.class)
                .eq(Article::getId, articleId)
                .setSql("comment_count=comment_count+1"));
    }

}
