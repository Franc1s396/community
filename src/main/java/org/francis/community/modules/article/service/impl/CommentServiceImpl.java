package org.francis.community.modules.article.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.enums.CodeEnums;
import org.francis.community.core.exception.ServiceException;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.modules.article.mapper.ArticleMapper;
import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.model.Comment;
import org.francis.community.modules.article.mapper.CommentMapper;
import org.francis.community.modules.article.model.request.CommentQueryRequest;
import org.francis.community.modules.article.service.CommentService;
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
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;

    private final ArticleMapper articleMapper;

    @Override
    public IPage<Comment> findCommentPageList(PageQueryRequest pageQueryRequest, CommentQueryRequest commentQueryRequest) {
        Page<Comment> commentPage = new Page<>(pageQueryRequest.getPage(), pageQueryRequest.getLimit());

        Long articleId = commentQueryRequest.getArticleId();
        Long userId = commentQueryRequest.getUserId();

        return commentMapper.selectPage(commentPage, Wrappers.lambdaQuery(Comment.class)
                .eq(Objects.nonNull(articleId), Comment::getArticleId, articleId)
                .eq(Objects.nonNull(userId), Comment::getUserId, userId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createComment(Long articleId, String content, Long userId) {
        boolean paramValidation = Objects.nonNull(articleId) && StringUtils.hasText(content) && Objects.nonNull(userId);
        if (!paramValidation) {
            throw new ServiceException(CodeEnums.PARAM_ERROR.getCode(), CodeEnums.PARAM_ERROR.getMessage());
        }

        Article article = articleMapper.selectOne(Wrappers.lambdaQuery(Article.class).eq(Article::getId, articleId));
        if (Objects.isNull(article)) {
            throw new ServiceException(CodeEnums.ARTICLE_NOT_FOUND.getCode(), CodeEnums.ARTICLE_NOT_FOUND.getMessage());
        }

        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setContent(content);
        comment.setUserId(userId);

        commentMapper.insert(comment);
        log.info("用户发布评论 userId:{},文章id:{},评论id:{}", userId, articleId, comment.getId());
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return commentMapper.selectOne(Wrappers.lambdaQuery(Comment.class).eq(Comment::getId, commentId));
    }
}
