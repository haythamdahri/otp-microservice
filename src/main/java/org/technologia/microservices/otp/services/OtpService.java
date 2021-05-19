package org.technologia.microservices.otp.services;

import org.technologia.microservices.otp.dto.OtpRequestDTO;
import org.technologia.microservices.otp.dto.OtpResponseDTO;

/**
 * @author Haytham DAHRI
 */
public interface OtpService {

    OtpResponseDTO sendOtp(OtpRequestDTO otpRequest);

}
