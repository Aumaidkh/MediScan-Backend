package com.hopcape.clustering.gemini

import com.fasterxml.jackson.databind.ObjectMapper
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
 * from labels detected on a medicine package. The service sends a structured prompt to the Gemini API and processes
 * the response to return structured medicine details in JSON format.
 *
 * This class is annotated with `@Service` and `@Qualifier(GOOGLE_GEMINI)` to enable Spring dependency injection.
 *
 * ### Key Features:
 * - Sends a prompt to the Gemini API with detected medicine labels.
 * - Extracts structured medicine details (name, description, usage, dosages) in JSON format.
 * - Handles API responses robustly by cleaning Markdown syntax and parsing JSON content.
 * - Logs errors and warnings for debugging and monitoring purposes.
 *
 * ### Configuration Requirements:
 * - `gemini.api.key`: API key for authenticating requests to the Gemini API.
 * - `gemini.api.url`: Base URL for the Gemini API endpoint.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using the GeminiMedicineDetailsPredictor in a Spring application
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
 * //   description = "Paracetamol Tablets IP manufactured by Cipla. Each tablet contains 500 mg of Paracetamol.",
 * //   usage = "Paracetamol is used to relieve mild to moderate pain and to reduce fever...",
 * //   dosage = "The usual adult dose is 500 mg to 1000 mg every 4 to 6 hours as needed..."
 * // )
 * ```
 *
 * ### Response Format:
 * The `predictByLabels` method expects the Gemini API to return a JSON object in the following format:
 * ```json
 * {
 *   "name": "Medicine name",
 *   "description": "Brief description of the medicine, salts, allergies, precautions",
 *   "usage": "How the medicine is prepared and what it is used to treat",
 *   "dosages": "Ideal dosage (e.g., 1 tablet after lunch and dinner for adults)"
 * }
 * ```
 * If the response contains Markdown syntax (e.g., triple backticks), the method cleans the response before parsing.
 *
 * ### Error Handling:
 * - If the API response is malformed or does not contain valid JSON, the method logs the error and returns `PredictionResult.ERROR`.
 * - If the Gemini API fails to provide a valid candidate or content, the method logs the issue and returns `PredictionResult.ERROR`.
 *
 * @property apiKey The API key used for authenticating requests to the Gemini API.
 * @property geminiUrl The base URL of the Gemini API endpoint.
 * @property objectMapper An instance of [ObjectMapper] for parsing JSON responses.
 * @property restTemplate An instance of [RestTemplate] for making HTTP requests to the Gemini API.
 * @property logger An instance of [Logger] for logging messages and errors.
 *
 * @constructor Creates an instance of [GeminiMedicineDetailsPredictor].
 */
@Service
@Qualifier(GOOGLE_GEMINI)
internal class GeminiMedicineDetailsPredictor(
    @Value("\${gemini.api.key}") private val apiKey: String,
    @Value("\${gemini.api.url}") private val geminiUrl: String,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate,
    private val logger: Logger
): MedicineDetailsPredictor {

    /**
     * Predicts medicine details based on the provided labels.
     *
     * This method constructs a prompt using the input labels, sends it to the Gemini API, and processes the response
     * to extract structured medicine details in JSON format.
     *
     * @param labels A string containing labels detected from a medicine package (e.g., "Cipla, Paracetamol, 500mg").
     * @return A [PredictionResult] object containing the extracted medicine details (name, description, usage, dosages).
     *         Returns [PredictionResult.ERROR] if the API response is invalid or an error occurs.
     *
     * @sample
     * ```kotlin
     * val predictor = GeminiMedicineDetailsPredictor(apiKey, geminiUrl, objectMapper, restTemplate, logger)
     * val labels = "Cipla Paracetamol Tablets IP PARACIP-500, Cipla, Paracetamol, Tablets, PARACIP, 500"
     * val result = predictor.predictByLabels(labels)
     *
     * println(result)
     * // Output:
     * // PredictionResult(
     * //   name = "PARACIP-500",
     * //   description = "Paracetamol Tablets IP manufactured by Cipla...",
     * //   usage = "Paracetamol is used to relieve mild to moderate pain...",
     * //   dosage = "The usual adult dose is 500 mg to 1000 mg every 4 to 6 hours..."
     * // )
     * ```
     */
    override fun predictByLabels(labels: String): PredictionResult {
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
    ){
        logger.log(
            log = Log(
                message = message,
                tag = this::class.simpleName.toString(),
                status = status
            )
        )
    }
}