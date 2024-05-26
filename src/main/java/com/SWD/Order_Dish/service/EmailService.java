package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.enums.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(
            String to,
            String account,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String validationCode,
            String subject
    ) throws MessagingException {
        String templateName = emailTemplate.name();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("account", account);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("validationCode", validationCode);
        Context context = new Context();
        context.setVariables(properties);
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);

        String html = templateEngine.process(templateName, context);
        helper.setText(html, true);
        mailSender.send(mimeMessage);
    }
}
