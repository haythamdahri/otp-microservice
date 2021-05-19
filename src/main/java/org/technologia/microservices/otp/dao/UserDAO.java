package org.technologia.microservices.otp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.technologia.microservices.otp.bo.User;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(@Param("username") String username);

}