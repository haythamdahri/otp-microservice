package org.technologia.microservices.otp.services;

import org.springframework.data.domain.Page;
import org.technologia.microservices.otp.bo.Otp;
import org.technologia.microservices.otp.dto.OtpRequestDTO;
import org.technologia.microservices.otp.dto.OtpResponseDTO;
import org.technologia.microservices.otp.dto.OtpVerificationRequestDTO;
import org.technologia.microservices.otp.mappers.OtpProjection;

/**
 * @author Haytham DAHRI
 */
public interface OtpService {

    Otp getOtp(int id);

    Otp getOtpByTransactionNumber(String transactionNumber);

    Page<OtpProjection> getCurrentUserOtpOperations(String search, int page, int size);

    OtpResponseDTO sendOtp(OtpRequestDTO otpRequest);

    OtpResponseDTO verifyOtp(OtpVerificationRequestDTO otpVerificationRequest);

    void deleteOtpOperation(int id);

}
