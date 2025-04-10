package com.es.phoneshop.service.implementations;

import com.es.phoneshop.service.DosProtectionService;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {

    private static final long THRESHOLD = 20;

    private static final long TIMEOUT_MILLISECONDS = 60000;

    private Map<String, Long> requestCountMap;

    Timer countResetTimer;

    private static DosProtectionService instance;

    public static DosProtectionService getInstance() {
        if (instance == null) {
            instance = new DefaultDosProtectionService();
        }
        return instance;
    }

    private DefaultDosProtectionService() {
        requestCountMap = new ConcurrentHashMap<>();
        countResetTimer = new Timer(true);

        countResetTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestCountMap.clear();
            }
        }, 0, TIMEOUT_MILLISECONDS);
    }

    @Override
    public boolean isAllowed(String ip) {
        long count = requestCountMap.getOrDefault(ip, 0L);
        count++;
        requestCountMap.put(ip, count);

        return count <= THRESHOLD;
    }
}
