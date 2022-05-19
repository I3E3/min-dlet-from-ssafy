package com.i3e3.mindlet.global.util;

import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerUtil {

    private final DandelionRepository dandelionRepository;

    @Transactional
    @Scheduled(cron = "0 0/10 * * * *")
    public void checkHoldDandelion() {
        System.out.println("스케줄러 checkHoldDandelion : " + LocalDateTime.now());
        long elapsedMinute = 30L;
        dandelionRepository.updateHoldingDandelionToFlying(elapsedMinute);
    }

    @Transactional
    @Scheduled(cron = "0 45 23 * * *")
    public void readyDandelion() {
        System.out.println("스케줄러 readyDandelion : " + LocalDateTime.now());
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
    }

    @Transactional
    @Scheduled(cron = "0 59 23 * * *")
    public void changeReadyToReturn() {
        System.out.println("스케줄러 changeReadfyToReturn : " + LocalDateTime.now());
        dandelionRepository.updateReadyDandelionToReturn();
    }
}
