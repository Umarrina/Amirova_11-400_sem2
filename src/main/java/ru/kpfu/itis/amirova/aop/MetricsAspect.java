package ru.kpfu.itis.amirova.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.amirova.service.MetricsService;

@Aspect
@Component
public class MetricsAspect {
    private final MetricsService metricsService;

    public MetricsAspect(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Around("@annotation(Metrics)")
    public Object measureMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        try {
            Object result = joinPoint.proceed();
            metricsService.recordSuccess(signature.getMethod());
            return result;
        } catch (Throwable t) {
            metricsService.recordFailure(signature.getMethod());
            throw t;
        }
    }
}