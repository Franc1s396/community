package org.francis.community.modules.article.listener;


import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/21
 * @apiNote
 */
@Data
public class ArticlePublishEvent {
    /**
     * 作者id
     */
    private Long userId;

    /**
     * 文章id
     */
    private Long articleId;
}
