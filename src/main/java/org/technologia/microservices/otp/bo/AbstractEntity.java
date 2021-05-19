package org.technologia.microservices.otp.bo;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@MappedSuperclass
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    protected Integer id;

    @CreationTimestamp
    protected Date timestamp;

    @Version
    protected Date updateTimestamp;


}
