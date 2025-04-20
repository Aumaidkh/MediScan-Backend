package com.hopcape.clustering

import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import com.hopcape.medicine.management.models.Medicine
import com.hopcape.medicine.management.repository.MedicineRepository
import me.xdrop.fuzzywuzzy.FuzzySearch
import me.xdrop.fuzzywuzzy.model.ExtractedResult
import org.springframework.stereotype.Service
import kotlin.math.min

data class RecognitionResult(
    val predictedMedicine: String,
    val confidenceScore: Double,
    val matchedFields: List<String>
)


@Service
internal class MedicineIdentifierImpl(
    private val medicineRepository: MedicineRepository,
    private val logger: Logger
): MedicineIdentifier {
    override fun identify(labels: List<String>): String? {
        val medicines = medicineRepository.findAll()
        logger.log(
            log = Log(message = "Labels: $labels", tag = "CustomTag")
        )
        val rankedMatches = medicines.map { medicine ->
            val scoreBreakdown = mutableListOf<String>()
            var score = 0.0

            labels.forEach { label ->
                if (medicine.name.contains(label, ignoreCase = true)) {
                    score += 3.0
                    scoreBreakdown.add("name: $label")
                }

                if (medicine.short_composition1?.contains(label, ignoreCase = true) == true) {
                    score += 2.0
                    scoreBreakdown.add("composition1: $label")
                }

                if (medicine.short_composition2?.contains(label, ignoreCase = true) == true) {
                    score += 2.0
                    scoreBreakdown.add("composition2: $label")
                }

                if (medicine.manufacturer_name?.contains(label, ignoreCase = true) == true) {
                    score += 1.0
                    scoreBreakdown.add("manufacturer: $label")
                }
            }

            Triple(medicine, score, scoreBreakdown)
        }.filter { it.second > 0 }
            .sortedByDescending { it.second }

        val bestMatch = rankedMatches.firstOrNull()

//        bestMatch?.let { (medicine, _, _) ->
//            val labelMatchedInName = labels.any { medicine.name.contains(it, ignoreCase = true) }
//            if (labelMatchedInName) {
//                return medicine.name
//            }
//
//            val labelMatchedInComposition1 = labels.firstOrNull { medicine.short_composition1?.contains(it, ignoreCase = true) == true }
//            if (labelMatchedInComposition1 != null) {
//                return labelMatchedInComposition1
//            }
//
//            val labelMatchedInComposition2 = labels.firstOrNull { medicine.short_composition2?.contains(it, ignoreCase = true) == true }
//            if (labelMatchedInComposition2 != null) {
//                return labelMatchedInComposition2
//            }
//        }

        return getBestMedicineName(
            labels = labels,
            knownMedicineNames = medicines.map { it.name }.toSet(),
            knownSalts = medicines.mapNotNull { it.short_composition1 }.toSet(),
            knownManufacturers = medicines.mapNotNull { it.manufacturer_name }.toSet()
        )
        // [cofsils, 15, COUGH, -, -]
        // [Tablets, PARACIP-500, Paracetamol, IP, Cipla]
        // [10, tag_id:, View, 101, 19800]
        // [Ltd., Pvt., Stelon, (SR), Sodium]

//            RecognitionResult(
//                predictedMedicine = it.first.name,
//                confidenceScore = it.second / (labels.size * 3),
//                matchedFields = it.third
//            )
//        } ?: RecognitionResult("Unknown", 0.0, emptyList())
    }

    //    override fun identify(labels: List<String>): String? {
//
//        val processedMedicines = medicineRepository.findAll()
//
//        val processedLabels = labels.map { it.lowercase(Locale.getDefault()) }.also {
//            logger.log(Log(tag = "CustomTag", message = "Identification result $it"))
//        }
//
//        return findBestMatchWithSalts(processedLabels,processedMedicines,80).also {
//            logger.log(Log(tag = "CustomTag", message = "Identification result $it"))
//        }
//    }

    fun findBestMatch(labels: List<String>, knownMedicines: List<String>, threshold: Int = 80): String {
        // Iterate through all labels and find the best match
        for (label in labels) {
            val results: List<ExtractedResult> = FuzzySearch.extractAll(label, knownMedicines)
            val bestMatch = results.maxByOrNull { it.score }

            // If the best match exceeds the threshold, return it
            if (bestMatch != null && bestMatch.score >= threshold) {
                return bestMatch.string
            }
        }
        // If no match is found, return "unknown"
        return "unknown"
    }

    fun findBestMatchWithSalts(
        labels: List<String>,
        knownMedicines: List<Medicine>,
        threshold: Int = 80
    ): String {
        var bestMatchScore = 0
        var bestMatchMedicine = "unknown"

        for (medicine in knownMedicines) {
            // Match the medicine name
            val nameMatches = FuzzySearch.extractAll(medicine.name, labels)
            val bestNameMatch = nameMatches.maxByOrNull { it.score }?.score ?: 0

            // Match the salts
            val saltScores = medicine.short_composition1?.map { salt ->
                FuzzySearch.extractAll(salt.toString(), labels).maxByOrNull { it.score }?.score ?: 0
            }
            val bestSaltMatch = saltScores?.maxOrNull() ?: 0

            // Combine scores (e.g., weighted average)
            val combinedScore = (bestNameMatch + bestSaltMatch) / 2

            // Update the best match if this one is better
            if (combinedScore > bestMatchScore && combinedScore >= threshold) {
                bestMatchScore = combinedScore
                bestMatchMedicine = medicine.name
            }
        }

        return bestMatchMedicine
    }


    fun getBestMedicineName(
        labels: List<String>,
        knownMedicineNames: Set<String>,
        knownSalts: Set<String>,
        knownManufacturers: Set<String>
    ): String? {
        val normalizedLabels = labels.map { it.trim().lowercase() }

        // 1. Exact match - Branded name
        for (label in normalizedLabels) {
            if (knownMedicineNames.contains(label)) {
                return capitalizeLabel(label)
            }
        }

        // 2. Partial or fuzzy match - Branded name
        val fuzzyBrandMatch = knownMedicineNames
            .map { medName -> medName to bestFuzzyMatchScore(medName.lowercase(), normalizedLabels) }
            .filter { it.second >= 0.8 } // only accept close matches
            .maxByOrNull { it.second }

        if (fuzzyBrandMatch != null) {
            return capitalizeLabel(fuzzyBrandMatch.first)
        }

        // 3. Exact salt match
        for (label in normalizedLabels) {
            if (knownSalts.contains(label)) {
                return capitalizeLabel(label)
            }
        }

        // 4. Fuzzy salt match
        val fuzzySaltMatch = knownSalts
            .map { salt -> salt to bestFuzzyMatchScore(salt.lowercase(), normalizedLabels) }
            .filter { it.second >= 0.8 }
            .maxByOrNull { it.second }

        if (fuzzySaltMatch != null) {
            return capitalizeLabel(fuzzySaltMatch.first)
        }

        // 5. Manufacturer match (only as last resort)
        for (label in normalizedLabels) {
            if (knownManufacturers.contains(label)) {
                return capitalizeLabel(label)
            }
        }

        return null
    }

    // Capitalizes first letter (simple version)
    private fun capitalizeLabel(label: String): String =
        label.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    // Returns best similarity score [0.0 to 1.0] for a target word against a list of labels
    private fun bestFuzzyMatchScore(target: String, labels: List<String>): Double {
        return labels.maxOf { label -> similarity(target, label) }
    }

    // Simple Levenshtein-based similarity function
    private fun similarity(s1: String, s2: String): Double {
        val distance = levenshtein(s1, s2)
        val maxLen = maxOf(s1.length, s2.length)
        return if (maxLen == 0) 1.0 else 1.0 - distance.toDouble() / maxLen
    }

    // Levenshtein distance implementation
    private fun levenshtein(lhs: String, rhs: String): Int {
        val lhsLen = lhs.length
        val rhsLen = rhs.length
        val dp = Array(lhsLen + 1) { IntArray(rhsLen + 1) }

        for (i in 0..lhsLen) dp[i][0] = i
        for (j in 0..rhsLen) dp[0][j] = j

        for (i in 1..lhsLen) {
            for (j in 1..rhsLen) {
                dp[i][j] = min(
                    min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + if (lhs[i - 1] == rhs[j - 1]) 0 else 1
                )
            }
        }
        return dp[lhsLen][rhsLen]
    }


}