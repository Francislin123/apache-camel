package com.walmart.feeds.api.unit.core.service.scheduler;

import com.walmart.feeds.api.core.service.scheduler.FeedScheduler;
import com.walmart.feeds.api.core.service.scheduler.FeedSchedulerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedSchedulerImplTest {

    @Mock
    private SchedulerFactoryBean schedulerFactoryBean;

    @Mock
    private StdScheduler stdScheduler;

    @InjectMocks
    private FeedScheduler feedScheduler =  new FeedSchedulerImpl();

    @Test
    public void createScheduleTest() throws SchedulerException {
        when(schedulerFactoryBean.getScheduler()).thenReturn(stdScheduler);
        feedScheduler.createFeedScheduler("name", "group", FeedSchedulerImpl.DEFAULT_CRON_INTERVAL);
        verify(stdScheduler, times(1)).scheduleJob(any(JobDetail.class), any(Trigger.class));
    }
    @Test
    public void deleteScheduleTest() throws SchedulerException {
        when(schedulerFactoryBean.getScheduler()).thenReturn(stdScheduler);
        feedScheduler.deleteJob("name", "group");
        verify(stdScheduler, times(1)).deleteJob(any(JobKey.class));
    }
}