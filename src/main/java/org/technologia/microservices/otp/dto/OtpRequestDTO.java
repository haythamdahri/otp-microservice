package org.technologia.microservices.otp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequestDTO implements Serializable {
    private static final long serialVersionUID = 6097832813671038370L;

    @JsonProperty("transactionNumber")
    private String transactionNumber;

    @JsonProperty("phone")
    private String phone;

}
