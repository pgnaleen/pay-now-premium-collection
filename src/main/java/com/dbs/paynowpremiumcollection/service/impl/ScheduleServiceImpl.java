package com.dbs.paynowpremiumcollection.service.impl;

import com.dbs.paynowpremiumcollection.service.ScheduleService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@EnableAsync
public class ScheduleServiceImpl implements ScheduleService {

    @Async
//    @Scheduled(fixedRate = 1000)
//    @Scheduled(cron = "0 15 10 15 * ?")
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println(
                "Fixed rate task async - " + System.currentTimeMillis() / 1000);
        Thread.sleep(2000);
    }
}
