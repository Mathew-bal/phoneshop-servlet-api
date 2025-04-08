package com.es.phoneshop.web;

import com.es.phoneshop.service.implementations.DefaultDosProtectionService;
import com.es.phoneshop.service.DosProtectionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {

    private static final long THRESHOLD = 20;

    private static final long TIMEOUT_MILLISECONDS = 60000;

    private static final String IP = "192.168.56.1";

    private static final int STATUS_TOO_MANY_REQUESTS = 429;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterConfig config;
    @Mock
    private FilterChain filterChain;

    private DosFilter dosFilter = new DosFilter();

    private DosProtectionService dosProtectionService;

    Map<String, Long> requests;

    @Before
    public void setup() throws ServletException, NoSuchFieldException, IllegalAccessException {
        dosProtectionService = DefaultDosProtectionService.getInstance();
        dosFilter.init(config);

        Field map = DefaultDosProtectionService.class.getDeclaredField("requestCountMap");
        map.setAccessible(true);
        requests = (Map<String, Long>) map.get(dosProtectionService);
        requests.put(IP, 0L);

        when(request.getRemoteAddr()).thenReturn(IP);
    }

    @Test
    public void testFilter() throws ServletException, IOException {
        dosFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(any(), any());
    }

    @Test
    public void testFilterRestrict() throws ServletException, IOException {
        requests.put(IP, THRESHOLD);

        dosFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(eq(STATUS_TOO_MANY_REQUESTS));
    }

    @Test
    public void testFilterRestrictTimeOut() throws ServletException, IOException, InterruptedException {
        requests.put(IP, THRESHOLD);
        Thread.sleep(TIMEOUT_MILLISECONDS);

        dosFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(any(), any());
    }
}
