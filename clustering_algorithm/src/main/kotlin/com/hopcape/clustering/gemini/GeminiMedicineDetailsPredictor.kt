package com.hopcape.clustering.gemini

import com.fasterxml.jackson.databind.ObjectMapper
import com.hopcape.clustering.nlp.MedicineDetailsPredictor
import com.hopcape.clustering.nlp.PredictionResult
import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@Qualifier("GeminiMedicineDetailsPredictor")
class GeminiMedicineDetailsPredictor(
    @Value("\${gemini.api.key}") private val apiKey: String,
    @Value("\${gemini.api.url}") private val geminiUrl: String,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate,
    private val logger: Logger
): MedicineDetailsPredictor {
    private val tag get() = this::class.java.name.toString()
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
            val response = restTemplate.postForEntity(urlWithKey, request, Map::class.java).also {
                log(
                    message = "Response: $it"
                )
            }

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
            log(
                message = "Response String: $jsonContent"
            )
            return objectMapper.readValue(jsonContent, PredictionResult::class.java)
        } catch (e: Exception) {
            log(
                message = "Error Fetching Details [${e.message}]",
                status = Log.Status.FAILURE
            )
            return PredictionResult.ERROR
        }
    }

    private fun log(
        message: String,
        status: Log.Status = Log.Status.INFO
    ){
        logger.log(
            log = Log(
                message = message,
                tag = tag,
                status = status
            )
        )
    }
}