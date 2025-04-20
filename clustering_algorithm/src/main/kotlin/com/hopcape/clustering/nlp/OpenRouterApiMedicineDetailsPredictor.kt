package com.hopcape.clustering.nlp

import com.fasterxml.jackson.databind.ObjectMapper
import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

@Service
class OpenRouterApiMedicineDetailsPredictor(
    @Value("\${qwen.api.key}")
    private val apiKey: String,
    @Value("\${qwen.base.url}")
    private val url: String,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate,
    private val logger: Logger
): MedicineDetailsPredictor {

    private val tag get() =
        this::class.simpleName.toString()

    override fun predictByLabels(labels: String): PredictionResult {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(apiKey)
        }
//        val prompt = """
//    Extract medicine details from the following labels: $labels
//    Provide the details in JSON format:
//    {
//      "name": "Medicine name",
//      "description": "Brief description of the medicine",
//      "usage": "How the medicine is used",
//      "dosages": "Dosage information"
//    }
//""".trimIndent()

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
                tag = tag
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
                    tag = tag
                )
            )
            val json = objectMapper.readTree(response.body)
            val rawContent = json["choices"][0]["message"]["content"].asText().also {
                logger.log(
                    log = Log(
                        status = Log.Status.INFO,
                        message = "Response Content: $it",
                        tag = tag
                    )
                )
            }
            val jsonContent = rawContent
                .trim()
                .removePrefix("```json")
                .removeSuffix("```")
                .trim()
            val parsedResponse =  objectMapper.readValue(jsonContent, PredictionResult::class.java)
            logger.log(
                log = Log(
                    status = Log.Status.SUCCESS,
                    message = "query success: $parsedResponse",
                    tag = tag
                )
            )
            parsedResponse
        }catch (e: Exception){
            logger.log(
                log = Log(
                    tag = tag,
                    message = e.message.toString(),
                    status = Log.Status.FAILURE
                )
            )
            PredictionResult.ERROR
        }
    }

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