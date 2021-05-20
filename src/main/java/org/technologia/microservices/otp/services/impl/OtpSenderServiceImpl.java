package org.technologia.microservices.otp.services.impl;

import org.springframework.stereotype.Service;
import org.technologia.microservices.otp.constants.OtpConstants;
import org.technologia.microservices.otp.dto.OtpChannel;
import org.technologia.microservices.otp.services.EmailService;
import org.technologia.microservices.otp.services.OtpSenderService;

/**
 * @author Haytham DAHRI
 */
@Service
public class OtpSenderServiceImpl implements OtpSenderService {

    private final EmailService emailService;

    public OtpSenderServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendOtpCode(OtpChannel otpChannel, String to, String otp) {
        switch (otpChannel) {
            case SMS:
                // TODO: To Implement later when having this channel available
                break;
            case EMAIL:
                // Send OTP Email
                this.emailService.sendOtpEmail(to, OtpConstants.OTP_EMAIL_SUBJECT, otp);
        }
    }
}
