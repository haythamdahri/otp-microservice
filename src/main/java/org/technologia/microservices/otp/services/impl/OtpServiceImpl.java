package org.technologia.microservices.otp.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.technologia.microservices.otp.bo.Otp;
import org.technologia.microservices.otp.bo.OtpState;
import org.technologia.microservices.otp.dao.OtpDAO;
import org.technologia.microservices.otp.dto.OtpChannel;
import org.technologia.microservices.otp.dto.OtpRequestDTO;
import org.technologia.microservices.otp.dto.OtpResponseDTO;
import org.technologia.microservices.otp.dto.OtpVerificationRequestDTO;
import org.technologia.microservices.otp.exceptions.BusinessException;
import org.technologia.microservices.otp.exceptions.NotFoundException;
import org.technologia.microservices.otp.facades.AuthenticationFacade;
import org.technologia.microservices.otp.generators.OtpGenerator;
import org.technologia.microservices.otp.helpers.DateHelper;
import org.technologia.microservices.otp.mappers.OtpProjection;
import org.technologia.microservices.otp.services.OtpSenderService;
import org.technologia.microservices.otp.services.OtpService;
import org.technologia.microservices.otp.services.UserService;

import javax.transaction.Transactional;
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
    private final DateHelper dateHelper;
    private final OtpSenderService otpSenderService;

    public OtpServiceImpl(OtpDAO otpDAO, AuthenticationFacade authenticationFacade, UserService userService, OtpGenerator otpGenerator, DateHelper dateHelper, OtpSenderService otpSenderService) {
        this.otpDAO = otpDAO;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.otpGenerator = otpGenerator;
        this.dateHelper = dateHelper;
        this.otpSenderService = otpSenderService;
    }

    @Override
    @Transactional
    public Otp getOtp(int id) {
        return this.otpDAO.findById(id).orElseThrow(() -> new NotFoundException("No OTP Found with id " + id));
    }

    @Override
    public Otp getOtpByTransactionNumber(String transactionNumber) {
        return this.otpDAO.findByTransactionNumber(transactionNumber).orElseThrow(() -> new NotFoundException("No OTP Found!"));
    }

    @Override
    public Page<OtpProjection> getCurrentUserOtpOperations(String search, int page, int size) {
        var pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "timestamp");
        // Check if search is present
        if (StringUtils.isEmpty(search)) {
            return this.otpDAO.findByUserUsername(this.authenticationFacade.getAuthentication().getName(), pageRequest);
        }
        return this.otpDAO.findByTransactionNumberAndUserUsername(search, this.authenticationFacade.getAuthentication().getName(), pageRequest);
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
        // Check if email and sms are not provided then throw exception
        if (StringUtils.isEmpty(otpRequest.getPhone()) && StringUtils.isEmpty(otpRequest.getEmail())) {
            throw new BusinessException("No Phone or Email is provided!");
        }
        // Generate Otp
        final String otpCode = this.otpGenerator.generateOtpCode(otpRequest.getTransactionNumber());
        // Send Otp to receiver via desired channel
        this.otpSenderService.sendOtpCode(!StringUtils.isEmpty(otpRequest.getPhone()) ? OtpChannel.SMS : OtpChannel.EMAIL, !StringUtils.isEmpty(otpRequest.getPhone()) ? otpRequest.getPhone() : otpRequest.getEmail(), otpCode);
        // Create new Otp Entry
        var otp = new Otp(0, otpRequest.getTransactionNumber(), OtpState.SENT, otpCode,
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
            case EXPIRED:
                // Throw Exception
                throw new BusinessException("OTP is Expired");
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
        if (!otpVerificationRequest.getOtp().equals(otp.getCode())) {
            // Check attempts
            if (otp.getAttempts() > 3) {
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
            throw new BusinessException("Invalid OTP Code");
        }
        // Check Expiration
        if (!this.dateHelper.toLocalDateTime(otp.getTimestamp()).plusMinutes(5L).isAfter(LocalDateTime.now())) {
            otp.setState(OtpState.EXPIRED);
            // Save OTP
            this.otpDAO.save(otp);
            // Throw Exception
            throw new BusinessException("OTP Code is expired");
        }
        // Set OTP As Verified
        otp.setState(OtpState.VERIFIED);
        // Save Otp
        this.otpDAO.save(otp);
        // Return Response
        return new OtpResponseDTO(otp.getTransactionNumber(), OtpState.VERIFIED, LocalDateTime.now());
    }

    @Override
    public void deleteOtpOperation(int id) {
        this.otpDAO.deleteById(id);
    }
}
