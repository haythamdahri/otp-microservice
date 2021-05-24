package org.technologia.microservices.otp.mappers;

import java.util.Date;

/**
 * @author Haytham DAHRI
 */
public interface OtpProjection {
    Integer getId();
    Date getTimestamp();
    Date getUpdateTimestamp();
    Integer getAttempts();
    String getTransactionNumber();
    String getState();
}
