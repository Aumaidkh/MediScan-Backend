package com.hopcape.logging.api

data class Log(
    val message: String,
    val tag: String = "Unknown",
    val status: Status = Status.INFO
){
    enum class Status {
        SUCCESS,
        FAILURE,
        WARNING,
        INFO
    }
}
