package ru.kpfu.itis.amirova.service;

import org.springframework.stereotype.Service;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BenchmarkService {
    private final Map<String, List<Long>> timingsMap = new ConcurrentHashMap<>();

    public void recordTiming(Method method, long nanos) {
        String key = getKey(method);
        timingsMap.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(nanos);
    }

    public Map<String, Object> getStatistics(String methodKey, Integer percentile) {
        List<Long> timings = timingsMap.get(methodKey);
        if (timings == null || timings.isEmpty()) {
            return Map.of("error", "No data for method: " + methodKey);
        }
        List<Long> sorted = new ArrayList<>(timings);
        Collections.sort(sorted);
        long total = sorted.size();
        long sum = sorted.stream().mapToLong(Long::longValue).sum();
        double avgMs = sum / (double) total / 1_000_000.0;
        long minNs = sorted.get(0);
        long maxNs = sorted.get(sorted.size() - 1);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalCalls", total);
        stats.put("averageMs", avgMs);
        stats.put("minMs", minNs / 1_000_000.0);
        stats.put("maxMs", maxNs / 1_000_000.0);

        if (percentile != null && percentile >= 0 && percentile <= 100) {
            double percentileMs = calculatePercentile(sorted, percentile) / 1_000_000.0;
            stats.put(percentile + "thPercentileMs", percentileMs);
        }
        return stats;
    }

    public Set<String> getAllMethodKeys() {
        return timingsMap.keySet();
    }

    private long calculatePercentile(List<Long> sorted, int percentile) {
        if (sorted.isEmpty()) return -1;
        if (percentile == 100) return sorted.get(sorted.size() - 1);
        double rank = (percentile / 100.0) * (sorted.size() - 1);
        int lower = (int) Math.floor(rank);
        int upper = (int) Math.ceil(rank);
        if (lower == upper) {
            return sorted.get(lower);
        }
        double fraction = rank - lower;
        return (long) (sorted.get(lower) * (1 - fraction) + sorted.get(upper) * fraction);
    }

    private String getKey(Method method) {
        return method.getDeclaringClass().getName() + "." + method.getName();
    }
}