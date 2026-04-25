package ru.kpfu.itis.amirova.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.amirova.service.MetricsService;

import java.util.Map;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public Map<String, Map<String, Long>> getAllMetrics() {
        return metricsService.getAllMetrics();
    }
}