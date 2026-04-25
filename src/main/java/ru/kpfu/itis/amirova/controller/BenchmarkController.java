package ru.kpfu.itis.amirova.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.amirova.service.BenchmarkService;

import java.util.Map;

@RestController
@RequestMapping("/benchmark")
public class BenchmarkController {
    private final BenchmarkService benchmarkService;

    public BenchmarkController(BenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    @GetMapping
    public Map<String, Object> getStatistics(
            @RequestParam("method") String methodKey,
            @RequestParam(value = "percentile", required = false) Integer percentile) {
        return benchmarkService.getStatistics(methodKey, percentile);
    }

    @GetMapping("/methods")
    public Map<String, Object> listMethods() {
        return Map.of("methods", benchmarkService.getAllMethodKeys());
    }
}