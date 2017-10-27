package com.walmart.feeds.api.core.notifications;

import com.walmart.feeds.api.client.gossip.GossipClient;
import com.walmart.feeds.api.core.repository.mailconf.model.MailConfEntity;
import com.walmart.feeds.api.core.repository.maillog.model.GossipEntity;
import com.walmart.feeds.api.core.repository.maillog.model.MailLogEntity;
import com.walmart.feeds.api.core.service.mail.MailConfService;
import com.walmart.feeds.api.core.service.maillog.MailLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private MailConfService mailConfService;

    @Autowired
    private MailLogService mailLogService;

    @Value("${gossip.template}")
    private String templateName;

    @Autowired
    private GossipClient gossipClient;

    private static final String DEFAULT_CONF_SLUG = "default_slug";

    @Override
    public void sendMail(String feedSlug, String partnerSlug, String message) {

        MailConfEntity mailConf = mailConfService.fetchBySlug(DEFAULT_CONF_SLUG);

        MailLogEntity mailLog = createLogEntity(feedSlug, partnerSlug, message,  mailConf);

        Map<String, String> tags = new HashMap<>();
        tags.put("feedSlug", feedSlug);
        tags.put("partnerSlug", partnerSlug);
        tags.put("error", message);

        gossipClient.sendEmail(GossipEntity.builder().toMail(mailConf.getTo())
                .templateName(templateName).tags(tags).build());

        mailLogService.log(mailLog);

    }

    private MailLogEntity createLogEntity(String feedSlug, String partnerSlug, String message, MailConfEntity mailConfEntity){

        StringBuilder bodyMsg = new StringBuilder();

        bodyMsg.append("Error on generate feed : " + feedSlug);
        bodyMsg.append(" for partner : " + partnerSlug);
        bodyMsg.append("; \n Error(s): \n " + message);

        return MailLogEntity.builder()
                .bodyMessage(bodyMsg.toString())
                .sentTo(mailConfEntity.getTo())
                .build();

    }
}
