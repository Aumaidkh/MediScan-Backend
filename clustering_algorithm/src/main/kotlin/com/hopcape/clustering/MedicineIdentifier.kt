package com.hopcape.clustering

interface MedicineIdentifier {

    fun identify(labels: List<String>): String?

}