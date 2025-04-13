package com.hopcape.auth.application.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.hopcape.auth.database.repositories"])
class ServiceConfig