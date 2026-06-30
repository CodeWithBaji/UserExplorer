package com.userexplorer.core.data.remote

import com.userexplorer.core.data.NetworkConfig
import com.userexplorer.core.domain.model.User
import retrofit2.http.GET

/**
 * Retrofit API interface for user-related network operations.
 * Defines the contract for making HTTP requests to the user endpoints.
 */
interface UserApi {

    /**
     * Fetches a list of users from the remote API.
     *
     * @return A list of [User] objects representing all available users
     */
    @GET(NetworkConfig.USERS_ENDPOINT)
    suspend fun getUsers(): List<User>
}