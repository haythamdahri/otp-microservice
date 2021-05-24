package org.technologia.microservices.otp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.technologia.microservices.otp.dto.OtpRequestDTO;
import org.technologia.microservices.otp.dto.OtpResponseDTO;
import org.technologia.microservices.otp.dto.OtpVerificationRequestDTO;
import org.technologia.microservices.otp.mappers.OtpProjection;
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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteOtpOperation(@PathVariable(name = "id") int id) {
        this.otpService.deleteOtpOperation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/checking")
    public ResponseEntity<OtpResponseDTO> sendOtpToDevice(@RequestBody OtpRequestDTO otpRequest) {
        return ResponseEntity.ok(this.otpService.sendOtp(otpRequest));
    }

    @PostMapping(path = "/verification")
    public ResponseEntity<OtpResponseDTO> verifyOtp(@RequestBody OtpVerificationRequestDTO otpVerificationRequest) {
        return ResponseEntity.ok(this.otpService.verifyOtp(otpVerificationRequest));
    }

    @GetMapping(path = "/currentuser/operations")
    public ResponseEntity<Page<OtpProjection>> getCurrentUserOtpOperations(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                           @RequestParam(name = "size", defaultValue = "20") int size) throws InterruptedException {
        Thread.sleep(5200L);
        return ResponseEntity.ok(this.otpService.getCurrentUserOtpOperations(page, size));
    }

}
