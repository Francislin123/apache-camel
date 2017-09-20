package com.walmart.feeds.api.core.repository.maillog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mail_log")
@Entity
public class MailLogEntity {

    @Id
    @GeneratedValue(generator = "maillog_uuid_generator")
    @GenericGenerator(name = "maillog_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "sent_to")
    private String sentTo;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body_msg")
    private String bodyMessage;

    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    @PrePersist
    protected void prePersist() {
        this.creationDate = LocalDateTime.now();
    }
}
