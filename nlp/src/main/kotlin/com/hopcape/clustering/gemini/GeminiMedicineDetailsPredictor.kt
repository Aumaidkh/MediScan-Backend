package com.hopcape.clustering.gemini

import com.fasterxml.jackson.databind.ObjectMapper
import com.hopcape.cache.api.Cache
import com.hopcape.clustering.api.MedicineDetailsPredictor
import com.hopcape.clustering.api.PredictionResult
import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

const val GOOGLE_GEMINI = "GeminiMedicineDetailsPredictor"
/**
 * A service implementation of [MedicineDetailsPredictor] that uses the Google Gemini API to extract medicine details
 * from labels. It integrates with a caching mechanism ([Cache]) to optimize repeated requests and reduce API calls.
 *
 * ### Key Features:
 * - Sends structured prompts to the Gemini API for extracting medicine details (name, description, usage, dosages).
 * - Uses a cache to store and retrieve previously fetched results, improving performance.
 * - Handles API responses robustly by cleaning Markdown syntax and parsing JSON content.
 * - Logs errors and warnings for debugging purposes.
 *
 * ### Example Usage:
 * ```kotlin
 * @Autowired
 * private lateinit var medicineDetailsPredictor: MedicineDetailsPredictor
 *
 * fun fetchMedicineDetails(labels: String): PredictionResult {
 *     return medicineDetailsPredictor.predictByLabels(labels)
 * }
 *
 * // Sample Input
 * val labels = "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10, Cipla, Paracetamol, Tablets, PARACIP, 500, रासप"
 * val result = fetchMedicineDetails(labels)
 *
 * // Sample Output
 * println(result)
 * // Output:
 * // PredictionResult(
 * //   name = "PARACIP-500",
 * //   description = "Paracetamol Tablets IP manufactured by Cipla...",
 * //   usage = "Paracetamol is used to relieve mild to moderate pain...",
 * //   dosage = "The usual adult dose is 500 mg to 1000 mg every 4 to 6 hours..."
 * // )
 * ```
 *
 * @property apiKey The API key for authenticating Gemini API requests.
 * @property geminiUrl The base URL of the Gemini API endpoint.
 * @property objectMapper Used for parsing JSON responses.
 * @property restTemplate Used for making HTTP requests to the Gemini API.
 * @property logger Used for logging messages and errors.
 * @property cache Used to store and retrieve cached medicine details.
 */
@Service
@Qualifier(GOOGLE_GEMINI)
internal class GeminiMedicineDetailsPredictor(
    @Value("\${gemini.api.key}") private val apiKey: String,
    @Value("\${gemini.api.url}") private val geminiUrl: String,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate,
    private val logger: Logger,
    private val cache: Cache
) : MedicineDetailsPredictor {

    /**
     * Predicts medicine details based on the provided labels.
     *
     * This method first checks the cache for existing results. If a cache hit occurs, it returns the cached result.
     * Otherwise, it sends a request to the Gemini API, processes the response, and updates the cache with the new result.
     *
     * @param labels A string containing labels detected from a medicine package (e.g., "Cipla, Paracetamol, 500mg").
     * @return A [PredictionResult] object containing the extracted medicine details (name, description, usage, dosages).
     *         Returns [PredictionResult.ERROR] if the API response is invalid or an error occurs.
     */
    override fun predictByLabels(labels: String): PredictionResult {
        // Return from cache when cache hit
        cache.get(labels)?.let { resultsJson ->
            log(
                message = "Cache Hit: $resultsJson"
            )
            return objectMapper.readValue(resultsJson, PredictionResult::class.java)
        }

        val prompt = """
            The following text contains labels detected from a medicine package: [ $labels ]

            Based on the label and general medical knowledge, extract the medicine details and provide them in this exact JSON format:
            {
              "name": "Medicine name",
              "description": "Brief description of the medicine, salts, allergies, precautions",
              "usage": "How the medicine is prepared and what it is used to treat",
              "dosages": "Ideal dosage (e.g., 1 tablet after lunch and dinner for adults)"
            }

            If dosage is not explicitly mentioned in the text, infer the commonly recommended dosage from known medical sources.

            Do NOT include any extra commentary or notes—just return the JSON.
        """.trimIndent()

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val requestBody = mapOf(
            "contents" to listOf(
                mapOf("parts" to listOf(mapOf("text" to prompt)))
            )
        )

        val request = HttpEntity(requestBody, headers)
        val urlWithKey = "$geminiUrl?key=$apiKey"

        try {
            val response = restTemplate.postForEntity(urlWithKey, request, Map::class.java)
            val candidates = response.body?.get("candidates") as? List<*>
            val firstCandidate = candidates?.firstOrNull() as? Map<*, *>
            val content = firstCandidate?.get("content") as? Map<*, *>
            val parts = content?.get("parts") as? List<*>
            val text = (parts?.firstOrNull() as? Map<*, *>)?.get("text") as? String
            val jsonContent = text
                ?.trim()
                ?.removePrefix("```json")
                ?.removeSuffix("```")
                ?.trim()
            // Update cache
            jsonContent?.let {
                log(
                    message = "Cache Miss: $it"
                )
                cache.update(
                    key = labels,
                    data = jsonContent
                )
            }
            return objectMapper.readValue(jsonContent, PredictionResult::class.java)
        } catch (e: Exception) {
            log(
                message = "Error Fetching Details [${e.message}]",
                status = Log.Status.FAILURE
            )
            return PredictionResult.ERROR
        }
    }

    /**
     * Logs a message with the specified status.
     *
     * @param message The message to log.
     * @param status The log status (default is [Log.Status.INFO]).
     */
    private fun log(
        message: String,
        status: Log.Status = Log.Status.INFO
    ) {
        logger.log(
            log = Log(
                message = message,
                tag = this::class.simpleName.toString(),
                status = status
            )
        )
    }
}