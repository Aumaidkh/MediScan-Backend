package com.hopcape.auth.application.config

import com.hopcape.auth.application.AuthService
import com.hopcape.auth.application.AuthServiceImpl
import com.hopcape.auth.domain.repositories.UserEntityRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.hopcape.auth.domain.repositories"])
class ServiceConfig