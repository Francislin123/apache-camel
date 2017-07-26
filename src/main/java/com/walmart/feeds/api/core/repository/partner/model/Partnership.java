package com.walmart.feeds.api.core.repository.partner.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>
 * Domain used to specify how is the partnership between Walmart and Partners.
 * </p>
 *
 * <p>The mapped partnerships are:</p>
 * <pre>
 *  COMPARADOR
 *  SEM
 *  AFILIADO
 *  EMAIL MARKETING
 *  RETARGETING
 *  B2B
 * </pre>
 */
@Entity
@Table(name = "partnership")
@Data
public class Partnership {

    @Id
    @GeneratedValue(generator = "partnership_uuid_generator")
    @GenericGenerator(name = "partnership_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    private String reference;

    private String name;


    public Partnership() {
    }

    public Partnership(String reference) {
        this.reference = reference;
    }
}
