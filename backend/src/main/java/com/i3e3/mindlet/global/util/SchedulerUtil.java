package com.i3e3.mindlet.global.util;

import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerUtil {

    private final DandelionRepository dandelionRepository;

    @Transactional
    @Scheduled(cron = "0 0/10 * * * *")
    public void checkHoldDandelion() {
        long elapsedMinute = 30L;
        dandelionRepository.updateHoldingDandelionToFlying(elapsedMinute);
    }

    @Transactional
    @Scheduled(cron = "0 25 23 * * *")
    public void readyDandelion() {
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
    }
}
