package org.technologia.microservices.otp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.technologia.microservices.otp.bo.OtpState;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponseDTO implements Serializable {
    private static final long serialVersionUID = 6742315664419307343L;

    @JsonProperty("transactionNumber")
    private String transactionNumber;

    @JsonProperty("state")
    private OtpState state;

    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
