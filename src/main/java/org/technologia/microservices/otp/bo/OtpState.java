package org.technologia.microservices.otp.bo;

/**
 * @author Haytham DAHRI
 */
public enum OtpState {

    SENT,
    VERIFIED,
    ALREADY_VERIFIED,
    INVALID,
    BLOCKED,
    EXPIRED

}
