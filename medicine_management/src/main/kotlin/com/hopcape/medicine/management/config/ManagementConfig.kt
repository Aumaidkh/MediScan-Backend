package com.hopcape.medicine.management.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.hopcape.medicine.management.repository"])
class ManagementConfig