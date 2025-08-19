package com.fcfb.arceus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = ["com.fcfb.arceus"])
open class FcfbArceusApplication

fun main(args: Array<String>) {
    System.setProperty("java.awt.headless", "true")
    runApplication<FcfbArceusApplication>(*args)
}
