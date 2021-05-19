package org.technologia.microservices.otp.services;

import org.technologia.microservices.otp.bo.User;

/**
 * @author Haytham DAHRI
 */
public interface UserService {

    User getUserByUsername(String username);

}
