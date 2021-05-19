package org.technologia.microservices.otp.services.impl;

import org.springframework.stereotype.Service;
import org.technologia.microservices.otp.bo.Otp;
import org.technologia.microservices.otp.bo.OtpState;
import org.technologia.microservices.otp.dao.OtpDAO;
import org.technologia.microservices.otp.dto.OtpRequestDTO;
import org.technologia.microservices.otp.dto.OtpResponseDTO;
import org.technologia.microservices.otp.exceptions.BusinessException;
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
        var otp = new Otp(otpRequest.getTransactionNumber(), OtpState.SENT, this.otpGenerator.generateOtpCode(otpRequest.getTransactionNumber()),
                this.userService.getUserByUsername(this.authenticationFacade.getAuthentication().getName()));
        otp = this.otpDAO.save(otp);
        // Return response
        return new OtpResponseDTO(otp.getTransactionNumber(), otp.getState(), LocalDateTime.now());
    }
}
