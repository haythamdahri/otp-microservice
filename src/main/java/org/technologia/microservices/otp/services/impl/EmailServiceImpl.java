package org.technologia.microservices.otp.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.technologia.microservices.otp.services.EmailService;
import org.technologia.microservices.otp.services.MailContentBuilder;
import org.technologia.microservices.otp.utils.OtpUtils;

/**
 * @author Haytham DAHRI
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Value("${spring.mail.username}")
    private String from;

    public EmailServiceImpl(JavaMailSender mailSender, MailContentBuilder mailContentBuilder) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    @Async
    @Override
    public void sendOtpEmail(String to, String subject, String otp){
        var message = this.mailSender.createMimeMessage();
        String templateText = this.mailContentBuilder.buildOtpEmail(otp);
        var helper = OtpUtils.buildMimeMessageHelper(this.from, to, subject, templateText, message, true);
        this.mailSender.send(helper.getMimeMessage());
    }
}
