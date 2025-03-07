package org.kmuradoff.openschooljava.application.domain.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.kmuradoff.openschooljava.application.domain.exception.CustomAspectException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TaskServiceAspect {

    @Pointcut("execution(* org.kmuradoff.openschooljava.application.domain.service.TaskServiceImpl.*(..))")
    public void taskServiceMethods() {
    }

    @Before("taskServiceMethods()")
    public void beforeTaskMethod(JoinPoint joinPoint) {
        log.info("[Before] --- Проверяем доступ для метода: {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "taskServiceMethods()", throwing = "ex")
    public void afterThrowingException(JoinPoint joinPoint, Throwable ex) {
        log.error("[AfterThrowing] --- Метод {} выбросил исключение: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }

    @AfterReturning(pointcut = "taskServiceMethods()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        log.info("[AfterReturning] --- Метод {} успешно завершён. Результат: {}", joinPoint.getSignature().getName(), result);
    }

    @Around("taskServiceMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint proceedingJoinPoint) {
        long startTime = System.currentTimeMillis();
        Object retVal;

        try {
            retVal = proceedingJoinPoint.proceed();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("[Around] --- Метод {} выполнялся {} мс", proceedingJoinPoint.getSignature().getName(), duration);

            return retVal;
        } catch (Throwable e) {
            throw new CustomAspectException("Ошибка при выполнении метода " + proceedingJoinPoint.getSignature().getName(), e);
        }
    }
}
