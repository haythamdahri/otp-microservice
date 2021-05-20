package org.technologia.microservices.otp.services;

import org.technologia.microservices.otp.dto.OtpChannel;

import javax.mail.MessagingException;

/**
 * @author Haytham DAHRI
 */
public interface OtpSenderService {

    void sendOtpCode(OtpChannel otpChannel, String otp, String to);

}
