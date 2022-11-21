package org.francis.community.modules.article.service.impl;

import org.francis.community.modules.article.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Franc1s
 * @date 2022/11/17
 * @apiNote
 */
@SpringBootTest
class ArticleServiceTest {
    @Autowired
    ArticleService articleService;

    @Test
    void createArticle() {

    }
}