package org.example.test.service;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class RequestCounterService {
    private final AtomicInteger requestCount = new AtomicInteger(-1);

    public void requestIncrement() {
        requestCount.incrementAndGet();
    }

    public int getRequestCount() {
        return requestCount.get();
    }
}