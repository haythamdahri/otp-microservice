package org.technologia.microservices.otp.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.technologia.microservices.otp.bo.Otp;
import org.technologia.microservices.otp.mappers.OtpProjection;

import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
public interface OtpDAO extends JpaRepository<Otp, Integer> {

    Optional<Otp> findByTransactionNumber(@Param("transactionNumber") String transactionNumber);

    @Query(value = "SELECT o FROM Otp o WHERE o.user.username = :username")
    Page<OtpProjection> findByUserUsername(@Param("username") String username, @PageableDefault Pageable pageable);

    @Query(value = "SELECT o FROM Otp o WHERE o.transactionNumber = :transactionNumber AND o.user.username = :username")
    Page<OtpProjection> findByTransactionNumberAndUserUsername(@Param("transactionNumber") String transactionNumber,
                                                               @Param("username") String username, @PageableDefault Pageable pageable);

}