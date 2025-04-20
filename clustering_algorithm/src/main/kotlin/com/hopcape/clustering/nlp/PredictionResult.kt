package com.hopcape.clustering.nlp

data class PredictionResult(
    val confidence: Int = 0,
    val name: String? = null,
    val description: String? = null,
    val usage: String? = null,
    val dosages: String? = null
){

    companion object {
        val ERROR = PredictionResult(0)
    }
}
