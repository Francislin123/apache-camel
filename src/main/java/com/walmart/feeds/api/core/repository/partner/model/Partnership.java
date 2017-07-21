package com.walmart.feeds.api.core.repository.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * Domain used to specify how is the partnership between Walmart and Partners.
 * </p>
 *
 * <p>The mapped partnerships are:</p>
 * <pre>
 *  COMPARADORES
 *  SEM
 *  AFILIADOS
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
    private String name;

}
