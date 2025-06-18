package com.feature.toggle.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class FeatureToggleAspect {
    private final FeatureToggleService service;

    @Around("@annotation(featureToggle)")
    public Object around(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        if (service.isFeatureEnabled(featureToggle.value())) {
            return joinPoint.proceed();
        }
        throw new IllegalStateException("Feature " + featureToggle.value() + " is disabled.");
    }
}
