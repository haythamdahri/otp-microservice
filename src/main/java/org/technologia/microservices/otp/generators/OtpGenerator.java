package org.technologia.microservices.otp.generators;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Haytham DAHRI
 */
@Component
public class OtpGenerator {

    private static final Random RANDOM = new Random();

    public String generateOtpCode(String transactionNumber) {
        var number = RANDOM.nextInt(999999 + transactionNumber.length());
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

}
