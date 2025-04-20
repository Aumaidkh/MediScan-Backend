package com.hopcape.clustering.nlp

fun interface MedicineDetailsPredictor {

    fun predictByLabels(labels: String): PredictionResult
}