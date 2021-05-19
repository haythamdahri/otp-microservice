package org.technologia.microservices.otp.services.impl;

import org.springframework.stereotype.Service;
import org.technologia.microservices.otp.bo.User;
import org.technologia.microservices.otp.dao.UserDAO;
import org.technologia.microservices.otp.exceptions.NotFoundException;
import org.technologia.microservices.otp.services.UserService;

/**
 * @author Haytham DAHRI
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userDAO.findByUsername(username).orElseThrow(() -> new NotFoundException("No user found with username " + username));
    }
}
