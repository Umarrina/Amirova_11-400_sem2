package ru.kpfu.itis.amirova.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.amirova.service.BenchmarkService;

@Aspect
@Component
public class BenchmarkAspect {
    private final BenchmarkService benchmarkService;

    public BenchmarkAspect(BenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    @Around("@annotation(Benchmark)")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.nanoTime() - start;
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            benchmarkService.recordTiming(signature.getMethod(), duration);
        }
    }
}