package com.fcfb.arceus.aspect

import com.fcfb.arceus.domain.AuditLog
import com.fcfb.arceus.repositories.AuditLogRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest


@Aspect
@Component
class AuditLogAspect(private val auditLogRepository: AuditLogRepository) {

    @Around("execution(* com.fcfb.arceus.controllers.*.post*(..))")
    fun logRequest(joinPoint: ProceedingJoinPoint): Any {
        val signature = joinPoint.signature
        val methodName = signature.name
        val className = signature.declaringTypeName
        val endpoint = "$className.$methodName"

        val httpRequest = joinPoint.args[0] as? HttpServletRequest
        val method = httpRequest?.method ?: "UNKNOWN"
        val body = httpRequest?.reader?.lines()?.collect(Collectors.joining())
        val username = SecurityContextHolder.getContext().authentication?.name ?: "Anonymous"

        // Create an audit log entry
        val auditLog = AuditLog(
            endpoint = endpoint,
            httpMethod = method,
            body = body,
            transactionDate = LocalDateTime.now().toString(),
            username = username
        )

        // Save the audit log to the database
        auditLogRepository.save(auditLog)

        // Continue with the original method execution
        return joinPoint.proceed()
    }
}