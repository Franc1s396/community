package org.francis.community.modules.article.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.modules.article.enums.LikeEnums;
import org.francis.community.modules.article.model.Article;
import org.francis.community.modules.article.model.Like;
import org.francis.community.modules.article.service.ArticleService;
import org.francis.community.modules.article.service.LikeService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Franc1s
 * @date 2022/11/20
 * @apiNote
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleLikeTask {
    /**
     * 记录所有点赞或取消点赞记录
     */
    private static final String LIKE_HASH_KEY = "community:article:like";
    /**
     * 记录帖子点赞计数
     */
    private static final String LIKE_COUNT_HASH_KEY_PREFIX = "community:article:like:count";

    private final StringRedisTemplate redisTemplate;

    private final LikeService likeService;

    private final ArticleService articleService;

    /**
     * redis定时落盘到MySQL
     * 时间随便定的
     */
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void articleLikeTask() {
        log.info("定时任务持久化点赞数据开始");
        // 获取hash所有数据
        Map<Object, Object> likeMap = redisTemplate.opsForHash().entries(LIKE_HASH_KEY);
        redisTemplate.opsForHash().delete(LIKE_HASH_KEY);
        // 如果为空直接返回不需要持久化
        if (likeMap.isEmpty()) {
            return;
        }
        batchLike2mysql(likeMap);
        batchCount2mysql();
        log.info("定时任务持久化点赞数据结束");
    }

    private void batchCount2mysql() {
        List<Article> articleList = new ArrayList<>();
        try (Cursor<Map.Entry<Object, Object>> cursor =
                     redisTemplate.opsForHash().scan(LIKE_COUNT_HASH_KEY_PREFIX, ScanOptions.NONE)) {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                String articleId = (String) entry.getKey();
                String count = (String) entry.getValue();
                Article article = new Article();
                article.setId(Long.parseLong(articleId));
                article.setLikeCount(Integer.parseInt(count));
                articleList.add(article);
            }
        }
        redisTemplate.opsForHash().delete(LIKE_COUNT_HASH_KEY_PREFIX);
        articleService.updateBatchLikeCount(articleList);
    }

    private void batchLike2mysql(Map<Object, Object> likeMap) {
        // 插入列表
        List<Like> insertRecordList = new ArrayList<>();
        // 更新列表
        List<Like> updateRecordList = new ArrayList<>();

        Set<Map.Entry<Object, Object>> set = likeMap.entrySet();
        for (Map.Entry<Object, Object> entry : set) {
            String key = (String) entry.getKey();
            String code = (String) entry.getValue();
            String[] split = key.split("::");

            Long articleId = Long.parseLong(split[0]);
            Long userId = Long.parseLong(split[1]);
            Like like = new Like();
            like.setArticleId(articleId);
            like.setUserId(userId);

            if (LikeEnums.LIKE.getCode().equals(Integer.parseInt(code))) {
                like.setStatus(true);
                insertRecordList.add(like);
            } else {
                updateRecordList.add(like);
            }
        }

        // 批量插入点赞数据
        likeService.saveBatch(insertRecordList);
        // 把需要取消点赞的数据先把状态更新为0，然后再开一个定时任务把状态为0的点赞数据删除
        likeService.updateBatchLike(updateRecordList);
    }
}
