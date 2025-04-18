package com.hopcape.logging.logger

import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.stereotype.Service

@Service
internal class ConsoleLogger: Logger {
    override fun log(log: Log) {
        with(log){
            println("TAG: $tag, STATUS: ${status.name} -> $message")
        }
    }
}