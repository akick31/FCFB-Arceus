package com.fcfb.arceus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
open class FcfbArceusApplication

fun main(args: Array<String>) {
    System.setProperty("java.awt.headless", "true")
    runApplication<FcfbArceusApplication>(*args)
}
