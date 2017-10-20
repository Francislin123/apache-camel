package com.walmart.feeds.api.resources.schedule;

import com.walmart.feeds.api.core.service.scheduler.FeedScheduler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(ScheduleController.V1_SCHEDULER)
public class ScheduleController {
    public static final String V1_SCHEDULER = "/v1/scheduler";

    @Autowired
    private FeedScheduler feedScheduler;

    @ApiOperation(value = "Create new schedule",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful new schedule"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void createSchedule(@RequestBody SchedulerRequest schedulerRequest){
        try {
            feedScheduler.createFeedScheduler(schedulerRequest.getName(), schedulerRequest.getGroup());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
