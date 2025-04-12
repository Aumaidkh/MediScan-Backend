package com.medscan.ai.application.config

import com.medscan.ai.application.AuthService
import com.medscan.ai.application.AuthServiceImpl
import com.medscan.ai.domain.repositories.UserEntityRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.medscan.ai.domain.repositories"])
class ServiceConfig {

    @Bean
    fun provideAuthService(repository: UserEntityRepository): AuthService {
        return AuthServiceImpl(repository)
    }
}