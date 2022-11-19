package org.francis.community.modules.auth.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.constant.LoginConstants;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.modules.auth.service.EmailService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Api(tags = "登录邮箱模块")
@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {
    private final StringRedisTemplate redisTemplate;

    private final Producer producer;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final EmailService emailService;

    @GetMapping("/email/send-code")
    @ApiOperation("邮箱验证码发送")
    public AjaxResult sendCode(@RequestParam String email) {
        // 验证是否存在code
        String code = redisTemplate.opsForValue().get(LoginConstants.EMAIL_CODE_KEY + email);
        if (StringUtils.hasText(code)) {
            return AjaxResult.error("60秒内不可重复发送验证码!");
        }
        // 生成验证码
        String verifyCode = producer.createText();
        // redis存验证码 ttl:60s
        redisTemplate.opsForValue().set(LoginConstants.EMAIL_CODE_KEY + email, verifyCode, 60, TimeUnit.SECONDS);
        // 向邮箱发送邮件
        CompletableFuture.runAsync(() -> {
            emailService.sendMailCode(email, verifyCode);
        }, threadPoolTaskExecutor);

        return AjaxResult.success("发送成功!");
    }
}
