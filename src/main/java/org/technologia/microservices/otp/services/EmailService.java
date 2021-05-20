package org.technologia.microservices.otp.services;

import javax.mail.MessagingException;

/**
 * @author Haytham DAHRI
 */
public interface EmailService {

    void sendOtpEmail(String to, String subject, String otp);

}
