package org.technologia.microservices.otp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.technologia.microservices.otp.dto.AuthResponseDTO;
import org.technologia.microservices.otp.dto.UserAuthRequestDTO;
import org.technologia.microservices.otp.utils.JwtTokenUtils;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserDetailsService userDetailsService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody UserAuthRequestDTO authRequest) {
        this.authenticate(authRequest.getUsername(), authRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = this.jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    /**
     * @param username:    User username
     * @param password: User Password
     */
    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }

}
