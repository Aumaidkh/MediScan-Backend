package com.hopcape.common.api

const val VERSION_PREFIX = "/v"
const val CURRENT_VERSION = 1
const val API_PATH = "/api"
const val FULL_API_PATH = "$API_PATH$VERSION_PREFIX$CURRENT_VERSION"

// Auth End Points
object AuthResource {
    const val ROOT = "/auth"
    const val LOGIN_ENDPOINT = "/login"
    const val REGISTER_ENDPOINT = "/register"
    const val REFRESH_TOKEN_ENDPOINT = "/refresh"

}

// Medicines
object MedicineResource {
    const val ROOT = "/medicines"
    const val IDENTIFY_ENDPOINT = "/identify"
}



