package com.example.donttouchme.common.aop.logtracer;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class LogAspect {

    private final LogTracer logTracer;

    @Pointcut("execution(* com.example.donttouchme..*Controller.*(..)) || execution(* com.example.donttouchme..*Service.*(..)) || " +
            "execution(* com.example.donttouchme..*Repository.*(..)) || execution(* com.example.donttouchme..*Scheduler.*(..))")
    public void everyRequest() { }

    @Around("everyRequest()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        boolean hasException = false;
        try {
            status = logTracer.begin(" Method : " + getKeySignature(joinPoint),
                    Arrays.deepToString(joinPoint.getArgs()));
            return joinPoint.proceed();
        } catch (Exception ex) {
            logTracer.handleException(status, ex);
            hasException = true;
            throw ex;
        } finally {
            if(!hasException) logTracer.end(status);
        }
    }

    private String getKeySignature(ProceedingJoinPoint joinPoint) {
        String[] split = joinPoint.getSignature().toString().split("\\.");
        int length = split.length;
        String[] arr = Arrays.copyOfRange(split, length-3, length);
        return arr[1] + "." + arr[2];
    }
}
