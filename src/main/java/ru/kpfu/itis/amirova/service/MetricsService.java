package ru.kpfu.itis.amirova.service;

import org.springframework.stereotype.Service;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MetricsService {
    private final Map<String, MethodMetrics> metricsMap = new ConcurrentHashMap<>();

    private static class MethodMetrics {
        long successCount = 0;
        long failureCount = 0;
    }

    public void recordSuccess(Method method) {
        String key = getKey(method);
        metricsMap.computeIfAbsent(key, k -> new MethodMetrics()).successCount++;
    }

    public void recordFailure(Method method) {
        String key = getKey(method);
        metricsMap.computeIfAbsent(key, k -> new MethodMetrics()).failureCount++;
    }

    public Map<String, Map<String, Long>> getAllMetrics() {
        Map<String, Map<String, Long>> result = new ConcurrentHashMap<>();
        metricsMap.forEach((key, mm) -> {
            result.put(key, Map.of("success", mm.successCount, "failure", mm.failureCount));
        });
        return result;
    }

    private String getKey(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }
}