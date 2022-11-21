package org.francis.community.modules.attention.controller;

import org.francis.community.core.model.AjaxResult;
import org.francis.community.core.model.LoginUser;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.modules.attention.model.request.AttentionFollowRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Franc1s
 * @date 2022/11/21
 * @apiNote
 */
@SpringBootTest
class AttentionControllerTest {

    @Autowired
    AttentionController attentionController;

    @BeforeEach
    void before() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(2L);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    void findFollowList() {
        PageQueryRequest pageQueryRequest = new PageQueryRequest();
        AjaxResult result = attentionController.findFollowList(pageQueryRequest, 1L);
        System.out.println(result);
    }

    @Test
    void findFansList() {
        PageQueryRequest pageQueryRequest = new PageQueryRequest();
        AjaxResult result = attentionController.findFansList(pageQueryRequest, 2L);
        System.out.println(result);
    }

    @Test
    void findCommonAttention() {
        attentionController.findCommonAttention(1L);
    }

    @Test
    void followUser() {
            AttentionFollowRequest request = new AttentionFollowRequest();
            request.setIsFollow(true);
            request.setUserId(3L);
            attentionController.followUser(request);
    }
}