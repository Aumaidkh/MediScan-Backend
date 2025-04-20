package com.hopcape.cache.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories(basePackages = ["com.hopcape.cache.predictions.repository","com.hopcape.cache.recognition.repository"])
class CacheConfig