package org.technologia.microservices.otp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "OTP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Otp extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 4598744356652139825L;

    @Column(name = "ATTEMPTS")
    private int attempts;

    @Column(name = "TRANSACTION_NUMBER")
    private String transactionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private OtpState state;

    @Column(name = "CODE", updatable = false)
    @JsonIgnore
    private String code;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "FK_USERID")
    private User user;

}
