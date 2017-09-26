package com.walmart.feeds.api.core.repository.mailconf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mail_conf")
@Getter
public class MailConfEntity {


    @Id
    @GeneratedValue(generator = "mailconf_uuid_generator")
    @GenericGenerator(name = "mailconf_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "mail_to")
    private String to;

}
