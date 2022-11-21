package org.francis.community.modules.article.controller;

import org.francis.community.core.model.LoginUser;
import org.francis.community.modules.article.enums.LikeEnums;
import org.francis.community.modules.article.schedule.ArticleLikeTask;
import org.francis.community.modules.article.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Security;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Franc1s
 * @date 2022/11/20
 * @apiNote
 */
@SpringBootTest
class LikeControllerTest {
    @Autowired
    LikeService likeService;

    @Autowired
    ArticleLikeTask articleLikeTask;

    @BeforeEach
    void before() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    void likeArticle() {
        likeService.likeArticle(1L);
        likeService.likeArticle(2L);
    }

    @Test
    void cancelLike(){
        likeService.cancelLikeArticle(1L);
        likeService.cancelLikeArticle(2L);
    }

    @Test
    void batchLike2mysqlTest(){
        articleLikeTask.articleLikeTask();
    }
}