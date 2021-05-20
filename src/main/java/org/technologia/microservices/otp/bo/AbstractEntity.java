package org.technologia.microservices.otp.bo;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@MappedSuperclass
@Data
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    protected Integer id;

    @CreationTimestamp
    @Column(name = "TIMESTAMP")
    protected Date timestamp;

    @Version
    @Column(name = "UPDATE_TIMESTAMP")
    protected Date updateTimestamp;

}
