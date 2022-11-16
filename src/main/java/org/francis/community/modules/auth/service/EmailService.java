package org.francis.community.modules.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.enums.CodeEnums;
import org.francis.community.core.exception.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author Franc1s
 * @date 2022/11/15
 * @apiNote
 */
@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendMailCode(String email, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        try {
            messageHelper.setFrom(mailProperties.getUsername());
            messageHelper.setTo(email);
            messageHelper.setSentDate(new Date());
            messageHelper.setSubject("DH人人社区邮箱验证码");
            Context context = new Context();
            context.setVariable("emailCode", code);
            String mail = templateEngine.process("mail", context);
            messageHelper.setText(mail, true);
            javaMailSender.send(message);
            log.info("to:{} 邮箱验证码发送成功", email);
        } catch (MessagingException e) {
            log.error("to:{} 邮箱验证码发送失败" + e.getMessage(), email);
            throw new EmailException(CodeEnums.EMAIL_ERROR.getCode(),CodeEnums.EMAIL_ERROR.getMessage());
        }
    }
}
