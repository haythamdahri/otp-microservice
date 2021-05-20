package org.technologia.microservices.otp.services.impl;

import org.springframework.stereotype.Service;
import org.technologia.microservices.otp.constants.OtpConstants;
import org.technologia.microservices.otp.services.MailContentBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * @author Haytham DAHRI
 */
@Service
public class MailContentBuilderImpl implements MailContentBuilder {

    private final SpringTemplateEngine templateEngine;

    public MailContentBuilderImpl(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String buildOtpEmail(String otp) {
        var context = new Context();
        context.setVariable(OtpConstants.OTP, otp);
        return templateEngine.process("mailing/otp-code", context);
    }

}
