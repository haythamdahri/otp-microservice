package org.technologia.microservices.otp.facades;

import org.springframework.security.core.Authentication;

/**
 * @author Haytham DAHRI
 */
public interface IAuthenticationFacade {

    Authentication getAuthentication();

}
