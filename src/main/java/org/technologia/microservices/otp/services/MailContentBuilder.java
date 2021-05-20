package org.technologia.microservices.otp.services;

/**
 * @author Haytham DAHRI
 */
public interface MailContentBuilder {

    String buildOtpEmail(String otp);

}
