package com.userexplorer.core.data

/**
 * Centralized network configuration.
 * Contains base URL and API endpoint paths.
 */
object NetworkConfig {
    /** Base URL for the API */
    const val BASE_URL = "https://fake-json-api.mock.beeceptor.com/"

    /** Endpoint path for fetching users */
    const val USERS_ENDPOINT = "users"

    /** Network client connect timeout in seconds */
    const val CONNECT_TIMEOUT_SECONDS = 60L

    /** Network client read timeout in seconds */
    const val READ_TIMEOUT_SECONDS = 60L

    /** Media type used for JSON request and response bodies */
    const val JSON_MEDIA_TYPE = "application/json"
}