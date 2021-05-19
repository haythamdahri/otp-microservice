package org.technologia.microservices.otp.services.impl;

import org.springframework.stereotype.Service;
import org.technologia.microservices.otp.bo.Otp;
import org.technologia.microservices.otp.bo.OtpState;
import org.technologia.microservices.otp.dao.OtpDAO;
import org.technologia.microservices.otp.dto.OtpRequestDTO;
import org.technologia.microservices.otp.dto.OtpResponseDTO;
import org.technologia.microservices.otp.dto.OtpVerificationRequestDTO;
import org.technologia.microservices.otp.exceptions.BusinessException;
import org.technologia.microservices.otp.exceptions.NotFoundException;
import org.technologia.microservices.otp.facades.AuthenticationFacade;
import org.technologia.microservices.otp.generators.OtpGenerator;
import org.technologia.microservices.otp.services.OtpService;
import org.technologia.microservices.otp.services.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Service
public class OtpServiceImpl implements OtpService {

    private final OtpDAO otpDAO;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final OtpGenerator otpGenerator;

    public OtpServiceImpl(OtpDAO otpDAO, AuthenticationFacade authenticationFacade, UserService userService, OtpGenerator otpGenerator) {
        this.otpDAO = otpDAO;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.otpGenerator = otpGenerator;
    }

    /**
     * Send OTP to the provided phone number
     *
     * @param otpRequest: OTP Request Object
     * @return OtpResponseDTO
     */
    @Override
    public OtpResponseDTO sendOtp(OtpRequestDTO otpRequest) {
        Optional<Otp> otpOptional = this.otpDAO.findByTransactionNumber(otpRequest.getTransactionNumber());
        // Check if Transaction Number Does Not Exists
        if (otpOptional.isPresent()) {
            throw new BusinessException("Transaction already exists in " + otpOptional.get().getState() + " state");
        }
        // Send SMS to Phone
        // TODO: SEND LATER AFTER RESOLVING ISSUE WITH TWILLIO
        // Create new Otp Entry
        var otp = new Otp(0, otpRequest.getTransactionNumber(), OtpState.SENT, this.otpGenerator.generateOtpCode(otpRequest.getTransactionNumber()),
                this.userService.getUserByUsername(this.authenticationFacade.getAuthentication().getName()));
        otp = this.otpDAO.save(otp);
        // Return response
        return new OtpResponseDTO(otp.getTransactionNumber(), otp.getState(), LocalDateTime.now());
    }

    @Override
    public OtpResponseDTO verifyOtp(OtpVerificationRequestDTO otpVerificationRequest) {
        var otp = this.otpDAO.findByTransactionNumber(otpVerificationRequest.getTransactionNumber()).orElseThrow(() -> new NotFoundException("No OTP Transaction found with number " + otpVerificationRequest.getTransactionNumber()));
        // Check if otp is not blocked
        switch (otp.getState()) {
            case BLOCKED:
                // Throw Exception
                throw new BusinessException("OTP is Blocked");
            case VERIFIED:
                // Set ALREADY_VERIFIED
                otp.setState(OtpState.ALREADY_VERIFIED);
                // Save OTP
                this.otpDAO.save(otp);
                // Return Response
                return new OtpResponseDTO(otp.getTransactionNumber(), OtpState.ALREADY_VERIFIED, LocalDateTime.now());
            case ALREADY_VERIFIED:
                return new OtpResponseDTO(otp.getTransactionNumber(), OtpState.ALREADY_VERIFIED, LocalDateTime.now());
        }
        // Check if code is not valid
        if( !otpVerificationRequest.getOtp().equals(otp.getCode()) ) {
            // Check attempts
            if( otp.getAttempts() > 3 ) {
                otp.setState(OtpState.BLOCKED);
                // Save OTP
                this.otpDAO.save(otp);
                // Throw Exception
                throw new BusinessException("OTP Transaction is Blocked due to excessive attempts");
            }
            // Increment attempts
            otp.setAttempts(otp.getAttempts() + 1);
            // Save OTP
            this.otpDAO.save(otp);
            // Throw Exception
            throw new BusinessException("Invalid OTP Verification code");
        }
        // Set OTP As Verified
        otp.setState(OtpState.VERIFIED);
        // Save Otp
        this.otpDAO.save(otp);
        // Return Response
        return new OtpResponseDTO(otp.getTransactionNumber(), OtpState.VERIFIED, LocalDateTime.now());
    }
}
