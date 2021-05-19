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
public class AuthResponseDTO implements Serializable {
    private static final long serialVersionUID = 5117386036714191828L;

    @JsonProperty("token")
    private String token;

}
