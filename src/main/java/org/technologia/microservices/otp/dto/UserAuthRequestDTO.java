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
public class UserAuthRequestDTO implements Serializable {
    private static final long serialVersionUID = -5093980727595605101L;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
