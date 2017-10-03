package com.walmart.feeds.api.resources.notifications;

import com.walmart.feeds.api.core.notifications.SendMailService;
import com.walmart.feeds.api.resources.notifications.request.GossipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(NotificationController.MAIL_NOTIFICATION_URI)
public class NotificationController {

    public static final String MAIL_NOTIFICATION_URI = "/v1/notifications";

    @Autowired
    private SendMailService gossipClient;

    // No Swagger Documentation. It is for the exclusive use of feed generator
    @RequestMapping(method = RequestMethod.POST, value="mail")
    public ResponseEntity sendMail(@RequestBody GossipRequest gossipRequest) {

        if (gossipRequest != null) {
            gossipClient.sendMail(gossipRequest.getFeedSlug(), gossipRequest.getPartnerSlug(), gossipRequest.getMessage());
        }

        return ResponseEntity.ok().build();
    }

}
