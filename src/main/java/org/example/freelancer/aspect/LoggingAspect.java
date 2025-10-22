package org.example.freelancer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // pointcut: controllers & service package (tweak packages as needed)
    @Around("execution(* org.example.freelancer..controller..*(..)) || execution(* org.example.freelancer..service..*(..))")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String method = sig.getDeclaringTypeName() + "." + sig.getName();
        Object[] args = pjp.getArgs();
        log.info("ENTER {} with args={}", method, args);
        long start = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("EXIT  {} returning={}, timeMs={}", method, result, elapsed);
            return result;
        } catch (Throwable t) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("ERROR {} after {}ms : {}", method, elapsed, t.getMessage(), t);
            throw t;
        }
    }
}
