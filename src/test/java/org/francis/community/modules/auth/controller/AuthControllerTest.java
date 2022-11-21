package org.francis.community.modules.auth.controller;

import org.francis.community.core.model.LoginUser;
import org.francis.community.modules.auth.model.request.PasswordRegisterRequest;
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
class AuthControllerTest {

    @Autowired
    AuthController authController;


    @Test
    void passwordRegister() {
        for (int i = 2; i < 12; i++) {
            PasswordRegisterRequest request = new PasswordRegisterRequest();
            request.setUsername("Test"+i);
            request.setPassword("123456");
            request.setNickname("Test"+i);
            authController.passwordRegister(request);
        }
    }
}