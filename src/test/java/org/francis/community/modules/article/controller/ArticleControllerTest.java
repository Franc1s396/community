package org.francis.community.modules.article.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.core.model.LoginUser;
import org.francis.community.modules.article.model.request.CreateArticleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Franc1s
 * @date 2022/11/20
 * @apiNote
 */
@SpringBootTest
class ArticleControllerTest {
    @Autowired
    ArticleController articleController;

    @BeforeEach
    void before() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(3L);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    void createArticle(){
        CreateArticleRequest request = new CreateArticleRequest();
        request.setTitle("测试发布帖子和推送服务");
        request.setContent("测试发布帖子和推送服务");
        request.setTagId(1L);
        articleController.createArticle(request);
    }

}