package com.mediscan.ai.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = ["com.mediscan.ai", "com.hopcape.security", "com.hopcape.auth", "com.hopcape.medicine.management", "com.hopcape.clustering", "com.hopcape.image", "com.hopcape.logging","com.hopcape.cache"])
class ApiGateWayApplication

fun main(args: Array<String>) {
    runApplication<ApiGateWayApplication>(*args)
}