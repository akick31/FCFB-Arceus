package com.fcfb.arceus.utils

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): Map<String, String> {
        return mapOf("error" to (e.message ?: "An unexpected error occurred"))
    }
}
