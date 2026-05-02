package io.github.devcavin.wattwise.userservice.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {
    private val log = LoggerFactory.getLogger(LoggingAspect::class.java)

    @Pointcut("execution(* io.github.devcavin.wattwise.userservice.service.*.*(..))")
    fun serviceMethodsLogging() {}

    @Before("serviceMethodsLogging()")
    fun logBefore(joinPoint: JoinPoint) {
        log.info(
            "Called method: ${joinPoint.signature.name} with args: ${joinPoint.args.contentToString()}"
        )
    }

    @AfterReturning(pointcut = "serviceMethodsLogging()", returning = "result")
    fun logAfterReturning(joinPoint: JoinPoint, result: Any?) {
        log.info(
            "Method ${joinPoint.signature.name} returned: $result"
        )
    }

    @AfterThrowing(pointcut = "serviceMethodsLogging()", throwing = "ex")
    fun logAfterThrowing(joinPoint: JoinPoint, ex: Throwable) {
        log.error(
            "Method ${joinPoint.signature.name} threw exception: ${ex.message}",
            ex
        )
    }

    @Around("serviceMethodsLogging()")
    fun measureTime(joinPoint: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()

        val result = joinPoint.proceed()

        val time = System.currentTimeMillis() - start
        log.info("${joinPoint.signature.name} took ${time}ms")

        return result
    }

    @After("serviceMethodsLogging()")
    fun logAfter(joinPoint: JoinPoint) {
        log.info("Method ${joinPoint.signature.name} completed")
    }
}