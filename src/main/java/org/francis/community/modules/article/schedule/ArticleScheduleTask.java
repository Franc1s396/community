package org.francis.community.modules.article.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.modules.article.service.ArticleService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Franc1s
 * @date 2022/11/18
 * @apiNote
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleScheduleTask {

    private final StringRedisTemplate redisTemplate;

    private final ArticleService articleService;

    /**
     * pv定时任务，将Redis上的pv数据持久化到MySQL中.
     * Scheduled(cron = "")
     */
    public void pageViewTask() {
        //TODO 从Redis上获取所有pv数据

        //TODO 持久化
    }
}
