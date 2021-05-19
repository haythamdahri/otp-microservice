package org.technologia.microservices.otp.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.technologia.microservices.otp.dto.OtpRequestDTO;
import org.technologia.microservices.otp.dto.OtpResponseDTO;
import org.technologia.microservices.otp.dto.OtpVerificationRequestDTO;
import org.technologia.microservices.otp.services.OtpService;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping(path = "/checking")
    public ResponseEntity<OtpResponseDTO> sendOtpToDevice(@RequestBody OtpRequestDTO otpRequest) {
        return ResponseEntity.ok(this.otpService.sendOtp(otpRequest));
    }

    @PostMapping(path = "/verification")
    public ResponseEntity<OtpResponseDTO> verifyOtp(@RequestBody OtpVerificationRequestDTO otpVerificationRequest) {
        return ResponseEntity.ok(this.otpService.verifyOtp(otpVerificationRequest));
    }

}
