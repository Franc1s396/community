package org.francis.community.modules.article.schedule;

import org.francis.community.modules.article.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Franc1s
 * @date 2022/11/19
 * @apiNote
 */
@SpringBootTest
class ArticleScheduleTaskTest {

    @Autowired
    private ArticleScheduleTask articleScheduleTask;

    @Test
    void pageViewTask() {
        articleScheduleTask.pageViewTask();
    }
}