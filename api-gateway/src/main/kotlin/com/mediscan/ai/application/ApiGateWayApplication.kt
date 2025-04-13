package com.mediscan.ai.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.mediscan.ai","com.hopcape.security","com.hopcape.auth"])
class ApiGateWayApplication

fun main(args: Array<String>) {
	runApplication<ApiGateWayApplication>(*args)
}