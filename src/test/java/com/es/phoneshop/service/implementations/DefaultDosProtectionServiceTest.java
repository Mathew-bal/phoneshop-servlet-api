package com.es.phoneshop.service.implementations;

import com.es.phoneshop.service.DosProtectionService;
import com.es.phoneshop.service.implementations.DefaultDosProtectionService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultDosProtectionServiceTest  {

    private DosProtectionService dosProtectionService;

    private static final long THRESHOLD = 20;

    private static final long TIMEOUT_MILLISECONDS = 60000;

    private static final String IP = "192.168.56.1";

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        dosProtectionService = DefaultDosProtectionService.getInstance();
        Field map = DefaultDosProtectionService.class.getDeclaredField("requestCountMap");
        map.setAccessible(true);
        Map<String, Long> requests = (Map<String, Long>) map.get(dosProtectionService);
        requests.put(IP, 0L);
    }

    @Test
    public void testRestrictAtThreshold() {
        for (int i = 0; i < THRESHOLD; i++) {
            assertTrue(dosProtectionService.isAllowed(IP));
        }
        assertFalse(dosProtectionService.isAllowed(IP));
    }

    @Test
    public void testRestrictTimeout() throws InterruptedException {
        for (int i = 0; i < THRESHOLD; i++) {
            assertTrue(dosProtectionService.isAllowed(IP));
        }
        assertFalse(dosProtectionService.isAllowed(IP));

        Thread.sleep(TIMEOUT_MILLISECONDS);

        assertTrue(dosProtectionService.isAllowed(IP));
    }
}
