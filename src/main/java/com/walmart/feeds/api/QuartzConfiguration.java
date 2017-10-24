package com.walmart.feeds.api;

import com.walmart.feeds.api.core.service.scheduler.QuartzJobFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class QuartzConfiguration {

    private static final String INSTANCE_NAME = "feeds-qtz";

    @Value("${org.quartz.scheduler.instanceId}")
    private String instanceId;

    @Value("${org.quartz.threadPool.threadCount}")
    private String threadCount;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext){
        QuartzJobFactory jobFactory = new QuartzJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext){
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);
        factory.setJobFactory(jobFactory(applicationContext));
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName",INSTANCE_NAME);
        properties.setProperty("org.quartz.scheduler.instanceId",instanceId);
        properties.setProperty("org.quartz.threadPool.threadCount",threadCount);
        properties.setProperty("org.quartz.jobStore.isClustered", "true");

        factory.setDataSource(dataSource);

        factory.setQuartzProperties(properties);

        return factory;

    }
}
