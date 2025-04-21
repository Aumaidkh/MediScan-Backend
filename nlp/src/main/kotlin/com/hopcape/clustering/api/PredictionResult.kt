package com.hopcape.clustering.api
/**
 * A data class representing the result of a medicine details prediction. This class encapsulates structured information
 * about a medicine, such as its name, description, usage, dosages, and confidence level of the prediction.
 *
 * ### Key Features:
 * - Provides a standardized structure for representing medicine details extracted from labels or other sources.
 * - Includes a default confidence value of `0` to indicate low or unknown confidence in the prediction.
 * - Supports nullable fields to handle cases where certain details (e.g., name, description) may not be available.
 * - Includes a companion object with a predefined `ERROR` instance to represent failed predictions.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Creating a PredictionResult object
 * val result = PredictionResult(
 *     confidence = 95,
 *     name = "PARACIP-500",
 *     description = "Paracetamol Tablets IP manufactured by Cipla...",
 *     usage = "Paracetamol is used to relieve mild to moderate pain...",
 *     dosages = "The usual adult dose is 500 mg to 1000 mg every 4 to 6 hours..."
 * )
 *
 * println(result)
 * // Output:
 * // PredictionResult(confidence=95, name=PARACIP-500, description=Paracetamol Tablets IP manufactured by Cipla..., usage=Paracetamol is used to relieve mild to moderate pain..., dosages=The usual adult dose is 500 mg to 1000 mg every 4 to 6 hours...)
 *
 * // Example: Using the ERROR instance for failed predictions
 * val errorResult = PredictionResult.ERROR
 * println(errorResult)
 * // Output:
 * // PredictionResult(confidence=0, name=null, description=null, usage=null, dosages=null)
 * ```
 *
 * ### Properties:
 * - [confidence]: An integer representing the confidence level of the prediction (default is `0`).
 * - [name]: The name of the medicine (nullable).
 * - [description]: A brief description of the medicine, including salts, allergies, or precautions (nullable).
 * - [usage]: Information on how the medicine is used and what it treats (nullable).
 * - [dosages]: Dosage recommendations for the medicine (nullable).
 *
 * ### Companion Object:
 * - [ERROR]: A predefined instance of [PredictionResult] with all fields set to default values (`null` or `0`),
 *   representing a failed prediction.
 */
data class PredictionResult(
    /**
     * An integer representing the confidence level of the prediction. A higher value indicates greater confidence
     * in the accuracy of the extracted medicine details. Default value is `0`.
     */
    val confidence: Int = 0,

    /**
     * The name of the medicine. This field is nullable to handle cases where the name cannot be determined.
     */
    val name: String? = null,

    /**
     * A brief description of the medicine, including information about its composition, potential allergies,
     * or precautions. This field is nullable to handle cases where the description is unavailable.
     */
    val description: String? = null,

    /**
     * Information on how the medicine is used and what it treats. This field is nullable to handle cases where
     * usage details are unavailable.
     */
    val usage: String? = null,

    /**
     * Dosage recommendations for the medicine, including frequency and quantity. This field is nullable to handle
     * cases where dosage information is unavailable.
     */
    val dosages: String? = null
) {

    /**
     * A companion object providing a predefined instance of [PredictionResult] to represent failed predictions.
     *
     * ### Example Usage:
     * ```kotlin
     * val errorResult = PredictionResult.ERROR
     * println(errorResult)
     * // Output:
     * // PredictionResult(confidence=0, name=null, description=null, usage=null, dosages=null)
     * ```
     */
    companion object {
        /**
         * A predefined instance of [PredictionResult] with all fields set to default values (`null` or `0`),
         * representing a failed prediction.
         */
        val ERROR = PredictionResult(0)
    }
}