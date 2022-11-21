package org.francis.community.modules.article.listener;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.modules.attention.model.Attention;
import org.francis.community.modules.attention.service.AttentionService;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Franc1s
 * @date 2022/11/21
 * @apiNote
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticlePublishListener {

    private static final String FANS_PUBLISH_KEY_PREFIX = "community:article:fans:feed:";

    private final StringRedisTemplate redisTemplate;

    private final AttentionService attentionService;

    /**
     * 为粉丝收件箱推送帖子id
     *
     * @param event 事件
     */
//    @Async(value = "threadPoolTaskExecutor")
    @EventListener(ArticlePublishEvent.class)
    public void publish(ArticlePublishEvent event) {
        Long userId = event.getUserId();
        Long articleId = event.getArticleId();

        List<Attention> fansList = attentionService.findFansList(userId);
        Set<Long> idSet = fansList.stream().map(Attention::getFansId).collect(Collectors.toSet());

        log.info("尝试给粉丝推送消息,预计推送{}条", idSet.size());
        AtomicInteger count = new AtomicInteger();
        idSet.forEach(aLong -> {
            String key = FANS_PUBLISH_KEY_PREFIX + aLong.toString();
            Boolean add = redisTemplate.opsForZSet().add(key, articleId.toString(), System.currentTimeMillis());
            if (Boolean.TRUE.equals(add)) {
                count.incrementAndGet();
            }
        });
        log.info("完成给粉丝推送消息,实际推送{}条", count);
    }
}
