package com.walmart.feeds.api.core.notifications;

import com.walmart.feeds.api.core.repository.mailconf.model.MailConfEntity;
import com.walmart.feeds.api.core.repository.maillog.model.MailLogEntity;
import com.walmart.feeds.api.core.service.mailConf.MailConfService;
import com.walmart.feeds.api.core.service.maillog.MailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private MailConfService mailConfService;

    @Autowired
    private MailLogService mailLogService;

    private static final String DEFAULT_CONF_SLUG = "default_slug";

    @Override
    public void sendMail(String feedSlug, String partnerSlug, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();

        MailConfEntity mailConf = mailConfService.fetchBySlug(DEFAULT_CONF_SLUG);

        MailLogEntity mailLog = createLogEntity(feedSlug, partnerSlug, message,  mailConf);

        mail.setFrom(mailConf.getFrom());
        mail.setTo(mailLog.getSentTo());
        mail.setText(mailLog.getBodyMessage());
        mail.setSubject(mailLog.getSubject());

        sender.send(mail);

        mailLogService.log(mailLog);



    }

    private MailLogEntity createLogEntity(String feedSlug, String partnerSlug, String message, MailConfEntity mailConfEntity){

        StringBuilder bodyMsg = new StringBuilder();

        bodyMsg.append(mailConfEntity.getBodyConf());
        bodyMsg.append("Error on generate feed : " + feedSlug);
        bodyMsg.append(" for partner : " + partnerSlug);
        bodyMsg.append("; \n Error(s): \n " + message);

        MailLogEntity mailLog = MailLogEntity.builder()
                .bodyMessage(bodyMsg.toString())
                .sentTo(mailConfEntity.getTo())
                .subject(String.format(mailConfEntity.getSubject(),
                        feedSlug))
                .build();

        return mailLog;
    }
}
