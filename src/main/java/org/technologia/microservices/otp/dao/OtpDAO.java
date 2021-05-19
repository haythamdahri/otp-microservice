package org.technologia.microservices.otp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.technologia.microservices.otp.bo.Otp;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
public interface OtpDAO extends JpaRepository<Otp, Integer> {

    Optional<Otp> findByTransactionNumber(@Param("transactionNumber") String transactionNumber);

}