package com.hopcape.clustering.fake

import com.fasterxml.jackson.databind.ObjectMapper
import com.hopcape.cache.api.Cache
import com.hopcape.clustering.api.MedicineDetailsPredictor
import com.hopcape.clustering.api.PredictionResult
import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

const val FAKE_PREDICTOR = "FakeMedicinePredictorService"

/**
 * A mock implementation of [MedicineDetailsPredictor] that simulates the behavior of a real medicine details predictor.
 * This service is intended for testing and development purposes, providing predefined medicine details in JSON format
 * without requiring an external API or service.
 *
 * The class is annotated with `@Service` and `@Qualifier(FAKE_PREDICTOR)` to enable Spring dependency injection.
 *
 * ### Key Features:
 * - Simulates the extraction of medicine details (name, description, usage, dosages) from labels.
 * - Returns a hardcoded JSON response containing fake medicine details.
 * - Useful for unit testing, integration testing, and local development when external APIs are unavailable.
 *
 * ### Configuration Requirements:
 * - No external configuration is required since this is a mock service.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using the FakeMedicinePredictorService in a Spring application
 * @Autowired
 * private lateinit var medicineDetailsPredictor: MedicineDetailsPredictor
 *
 * fun fetchMedicineDetails(labels: String): PredictionResult {
 *     return medicineDetailsPredictor.predictByLabels(labels)
 * }
 *
 * // Sample Input
 * val labels = "Cofsils Cough Syrup, Cofsils, Cough, Syrup"
 * val result = fetchMedicineDetails(labels)
 *
 * // Sample Output
 * println(result)
 * // Output:
 * // PredictionResult(
 * //   name = "Cofsils Cough Syrup",
 * //   description = "Cofsils cough syrup is a medication used to relieve cough symptoms...",
 * //   usage = "Used to treat cough associated with cold, flu, or other respiratory infections...",
 * //   dosage = "Adults: 10-15 ml three to four times a day..."
 * // )
 * ```
 *
 * ### Response Format:
 * The `predictByLabels` method always returns a predefined JSON object in the following format:
 * ```json
 * {
 *   "name": "Cofsils Cough Syrup",
 *   "description": "Cofsils cough syrup is a medication used to relieve cough symptoms...",
 *   "usage": "Used to treat cough associated with cold, flu, or other respiratory infections...",
 *   "dosages": "Adults: 10-15 ml three to four times a day..."
 * }
 * ```
 * This ensures consistent and predictable results for testing purposes.
 *
 * ### Use Cases:
 * - **Unit Testing**: Simulate API responses without making actual API calls.
 * - **Integration Testing**: Test the interaction between components without relying on external dependencies.
 * - **Local Development**: Enable developers to test functionality locally without configuring external services.
 *
 * @property objectMapper An instance of [ObjectMapper] for parsing JSON responses.
 *
 * @constructor Creates an instance of [FakeMedicinePredictorService].
 */
@Service
@Qualifier(FAKE_PREDICTOR)
internal class FakeMedicinePredictorService(
    private val objectMapper: ObjectMapper,
    private val logger: Logger,
    private val cache: Cache
) : MedicineDetailsPredictor {

    /**
     * Predicts medicine details based on the provided labels.
     *
     * This method ignores the input labels and always returns a predefined set of medicine details in JSON format.
     * It is intended for testing and development purposes only.
     *
     * @param labels A string containing labels detected from a medicine package (e.g., "Cofsils, Cough, Syrup").
     *               Note: The input labels are ignored in this mock implementation.
     * @return A [PredictionResult] object containing predefined medicine details (name, description, usage, dosages).
     *
     * @sample
     * ```kotlin
     * val fakePredictor = FakeMedicinePredictorService(objectMapper)
     * val labels = "Cofsils Cough Syrup, Cofsils, Cough, Syrup"
     * val result = fakePredictor.predictByLabels(labels)
     *
     * println(result)
     * // Output:
     * // PredictionResult(
     * //   name = "Cofsils Cough Syrup",
     * //   description = "Cofsils cough syrup is a medication used to relieve cough symptoms...",
     * //   usage = "Used to treat cough associated with cold, flu, or other respiratory infections...",
     * //   dosage = "Adults: 10-15 ml three to four times a day..."
     * // )
     * ```
     */
    override fun predictByLabels(labels: String): PredictionResult {
        cache.get(labels)?.let {
            val json = objectMapper.readValue(it, PredictionResult::class.java)
            return json.also { resp ->
                log(
                    message = "Cache Hit -> $resp"
                )
            }
        } ?: run {
            val fakeJson = """
            {
              "name": "Cofsils Cough Syrup",
              "description": "Cofsils cough syrup is a medication used to relieve cough symptoms. It may contain ingredients to suppress cough, thin mucus, or soothe the throat. Consult a doctor or pharmacist for potential allergies and precautions.",
              "usage": "Used to treat cough associated with cold, flu, or other respiratory infections.",
              "dosages": "Adults: 10-15 ml three to four times a day. Children (6-12 years): 5-10 ml three to four times a day. Children (2-6 years): 2.5-5 ml three to four times a day. Consult a doctor for children under 2 years."
            }
        """.trimIndent()
            val json = objectMapper.readValue(fakeJson, PredictionResult::class.java)
            cache.update(
                key = labels,
                data = fakeJson
            )
            return json.also {
                log(
                    message = "Cache Miss -> $it"
                )
            }
        }
    }

    private fun log(message: String,status: Log.Status = Log.Status.INFO){
        logger.log(
            log = Log(
                message = message,
                tag = this::class.simpleName.toString(),
                status = status
            )
        )
    }
}