package com.hopcape.clustering.qwen

import com.fasterxml.jackson.databind.ObjectMapper
import com.hopcape.clustering.api.MedicineDetailsPredictor
import com.hopcape.clustering.api.PredictionResult
import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

@Service
@Deprecated(
    message = "Doesn't return the dosage in the intended format",
    replaceWith = ReplaceWith(
        expression = "GeminiMedicineDetailsPredictor",
        imports = ["com.hopcape.clustering.gemini.GeminiMedicineDetailsPredictor"]
    ),
    level = DeprecationLevel.WARNING
)
/**
 * A service implementation of [MedicineDetailsPredictor] that uses the OpenRouter API (with the Qwen model) to extract
 * medicine details from labels detected on a medicine package. The service sends a structured prompt to the API and
 * processes the response to return structured medicine details in JSON format.
 *
 * This class is designed to interact with the OpenRouter API using a specific model (`nvidia/llama-3.1-nemotron-nano-8b-v1:free`)
 * for generating responses. It ensures robust handling of API responses by cleaning Markdown syntax and parsing JSON content.
 *
 * ### Key Features:
 * - Sends a prompt to the OpenRouter API with detected medicine labels.
 * - Extracts structured medicine details (name, description, usage, dosages) in JSON format.
 * - Handles API responses robustly by cleaning Markdown syntax and parsing JSON content.
 * - Logs errors and warnings for debugging and monitoring purposes.
 *
 * ### Configuration Requirements:
 * - `qwen.api.key`: API key for authenticating requests to the OpenRouter API.
 * - `qwen.base.url`: Base URL for the OpenRouter API endpoint.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using the OpenRouterApiMedicineDetailsPredictor in a Spring application
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
 * ### Response Format:
 * The `predictByLabels` method expects the OpenRouter API to return a JSON object in the following format:
 * ```json
 * {
 *   "name": "Medicine name",
 *   "description": "Brief description of the medicine",
 *   "usage": "How the medicine is used",
 *   "dosages": "Dosage information"
 * }
 * ```
 * If the response contains Markdown syntax (e.g., triple backticks), the method cleans the response before parsing.
 *
 * ### Error Handling:
 * - If the API response is malformed or does not contain valid JSON, the method logs the error and returns `PredictionResult.ERROR`.
 * - If the OpenRouter API fails to provide a valid candidate or content, the method logs the issue and returns `PredictionResult.ERROR`.
 *
 * @property apiKey The API key used for authenticating requests to the OpenRouter API.
 * @property url The base URL of the OpenRouter API endpoint.
 * @property objectMapper An instance of [ObjectMapper] for parsing JSON responses.
 * @property restTemplate An instance of [RestTemplate] for making HTTP requests to the OpenRouter API.
 * @property logger An instance of [Logger] for logging messages and errors.
 *
 * @constructor Creates an instance of [OpenRouterApiMedicineDetailsPredictor].
 */
internal class OpenRouterApiMedicineDetailsPredictor(
    @Value("\${qwen.api.key}")
    private val apiKey: String,
    @Value("\${qwen.base.url}")
    private val url: String,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate,
    private val logger: Logger
) : MedicineDetailsPredictor {

    /**
     * Predicts medicine details based on the provided labels.
     *
     * This method constructs a prompt using the input labels, sends it to the OpenRouter API, and processes the response
     * to extract structured medicine details in JSON format.
     *
     * @param labels A string containing labels detected from a medicine package (e.g., "Cipla, Paracetamol, 500mg").
     * @return A [PredictionResult] object containing the extracted medicine details (name, description, usage, dosages).
     *         Returns [PredictionResult.ERROR] if the API response is invalid or an error occurs.
     *
     * @sample
     * ```kotlin
     * val predictor = OpenRouterApiMedicineDetailsPredictor(apiKey, url, objectMapper, restTemplate, logger)
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
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(apiKey)
        }

        val prompt = """
            The following text contains labels extracted from a medicine package: [ $labels ]
            Extract the medicine details and provide them in JSON format:
            {
              "name": "Medicine name",
              "description": "Brief description of the medicine",
              "usage": "How the medicine is used",
              "dosages": "Dosage information"
            }
            Please just return the text in the above json format only, no other text in starting or the ending
        """.trimIndent()

        logger.log(
            log = Log(
                message = "Prompt: $prompt",
                tag = this::class.simpleName.toString()
            )
        )

        val requestBody = createQwenRequestBodyFrom(prompt)
        val entity = HttpEntity(requestBody, headers)

        return try {
            val response = restTemplate.postForEntity(url, entity, String::class.java)
            logger.log(
                log = Log(
                    status = Log.Status.INFO,
                    message = "Response: $response",
                    tag = this::class.simpleName.toString()
                )
            )
            val json = objectMapper.readTree(response.body)
            val rawContent = json["choices"][0]["message"]["content"].asText().also {
                logger.log(
                    log = Log(
                        status = Log.Status.INFO,
                        message = "Response Content: $it",
                        tag = this::class.simpleName.toString()
                    )
                )
            }
            val jsonContent = rawContent
                .trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()
            val parsedResponse = objectMapper.readValue(jsonContent, PredictionResult::class.java)
            logger.log(
                log = Log(
                    status = Log.Status.SUCCESS,
                    message = "query success: $parsedResponse",
                    tag = this::class.simpleName.toString()
                )
            )
            parsedResponse
        } catch (e: Exception) {
            logger.log(
                log = Log(
                    tag = this::class.simpleName.toString(),
                    message = e.message.toString(),
                    status = Log.Status.FAILURE
                )
            )
            PredictionResult.ERROR
        }
    }

    /**
     * Creates a request body for the OpenRouter API based on the provided prompt.
     *
     * @param prompt The prompt to send to the OpenRouter API.
     * @return A map representing the request body, including the model name and structured messages.
     */
    private fun createQwenRequestBodyFrom(prompt: String): Map<String, Any> {
        return mapOf(
            "model" to "nvidia/llama-3.1-nemotron-nano-8b-v1:free", // Specify the model name here
            "messages" to listOf(
                mapOf(
                    "role" to "user",
                    "content" to listOf(
                        mapOf(
                            "type" to "text",
                            "text" to prompt // Dynamically insert the search query/prompt
                        )
                    )
                )
            )
        )
    }
}