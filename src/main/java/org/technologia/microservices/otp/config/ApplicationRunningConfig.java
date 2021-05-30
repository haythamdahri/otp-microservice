package org.technologia.microservices.otp.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.technologia.microservices.otp.bo.User;
import org.technologia.microservices.otp.dao.UserDAO;

/**
 * @author Haytham DAHRI
 */
@Configuration
public class ApplicationRunningConfig {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApplicationRunningConfig(UserDAO userDAO, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDAO = userDAO;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void onApplicationStart() {
        // Add mock user if not exists
        if (this.userDAO.findByUsername("haytham").isEmpty()) {
            this.userDAO.save(new User("haytham", "haytham.dahri@gmail.com", this.bCryptPasswordEncoder.encode("OTP@microservice2520")));
        }
    }

}
