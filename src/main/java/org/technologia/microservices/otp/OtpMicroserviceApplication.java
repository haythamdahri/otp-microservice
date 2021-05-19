package org.technologia.microservices.otp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class OtpMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtpMicroserviceApplication.class, args);
    }

}
