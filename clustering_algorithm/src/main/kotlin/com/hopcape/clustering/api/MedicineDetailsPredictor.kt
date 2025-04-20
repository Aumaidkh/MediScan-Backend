package com.hopcape.clustering.api

/**
 * A functional interface defining the contract for predicting medicine details based on labels extracted from a
 * medicine package. This interface is designed to be implemented by various services that interact with APIs, models,
 * or other mechanisms to extract structured medicine information.
 *
 * The `predictByLabels` method takes a string of labels (e.g., text extracted from a medicine package) and returns a
 * [PredictionResult] object containing structured details about the medicine, such as its name, description, usage,
 * and dosages.
 *
 * ### Key Features:
 * - Simplifies the integration of different prediction mechanisms (e.g., AI models, mock services, or external APIs).
 * - Provides a consistent interface for extracting medicine details regardless of the underlying implementation.
 * - Enables dependency injection in Spring applications by allowing multiple implementations to be injected based on
 *   qualifiers or configurations.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using a MedicineDetailsPredictor implementation in a Spring application
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
 * ### Implementations:
 * - **Real Implementations**: Classes like [OpenRouterApiMedicineDetailsPredictor] or [GeminiMedicineDetailsPredictor]
 *   that interact with external APIs or models to extract medicine details.
 * - **Mock Implementations**: Classes like [FakeMedicinePredictorService] that provide predefined responses for
 *   testing and development purposes.
 *
 * ### Method:
 * - [predictByLabels]: Predicts medicine details based on the provided labels.
 * */
fun interface MedicineDetailsPredictor {

    /**
     * Predicts medicine details based on the provided labels.
     *
     * @param labels A string containing labels detected from a medicine package (e.g., "Cipla, Paracetamol, 500mg").
     *               These labels may include the medicine name, manufacturer, ingredients, dosage, or other relevant
     *               information.
     * @return A [PredictionResult] object containing the extracted medicine details (name, description, usage, dosages).
     *         Implementations may return [PredictionResult.ERROR] if an error occurs during prediction.
     *
     * @sample
     * ```kotlin
     * val predictor: MedicineDetailsPredictor = OpenRouterApiMedicineDetailsPredictor(apiKey, url, objectMapper, restTemplate, logger)
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
    fun predictByLabels(labels: String): PredictionResult
}