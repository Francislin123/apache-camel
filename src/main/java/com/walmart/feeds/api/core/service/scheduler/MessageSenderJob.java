package com.walmart.feeds.api.core.service.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class MessageSenderJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderJob.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${amqp.generator-queue-name}")
    private String queueName;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Job Triggered by -> keyName " + context.getTrigger().getJobKey().getName()
                + " and keyGroup: " + context.getTrigger().getJobKey().getGroup());

        rabbitTemplate.convertAndSend("feed-exchange", "generate",
                createMessage(context.getTrigger().getJobKey().getGroup(), context.getTrigger().getJobKey().getName() ));

    }
    private byte[] createMessage(String group, String name){
        StringBuilder message = new StringBuilder();
        message.append(group);
        message.append(";");
        message.append(name);
        message.append(";");
        message.append(0);

        LOGGER.info("message to be sent : " + message.toString());

        return message.toString().getBytes();
    }
}
