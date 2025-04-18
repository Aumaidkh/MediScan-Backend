package com.hopcape.clustering

import com.hopcape.medicine.management.repository.MedicineRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class MedicineIdentifierImpl(
    private val medicineRepository: MedicineRepository
): MedicineIdentifier {

    override fun identify(labels: List<String>): String? {

        val processedMedicines = medicineRepository.findAll().map { it.name.lowercase(Locale.getDefault()) }

        val processedLabels = labels.map { it.lowercase(Locale.getDefault()) }

        return processedMedicines.firstOrNull{ processedLabels.contains(it) }
    }

}