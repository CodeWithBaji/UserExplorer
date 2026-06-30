package com.userexplorer.core.data.remote.datasource

import com.userexplorer.core.data.remote.UserApi
import com.userexplorer.core.domain.model.User

/**
 * Interface defining the contract for remote user data operations.
 * This abstraction allows for easier testing and potential implementation changes.
 */
interface UserRemoteDataSource {
    /**
     * Fetches users from the remote data source.
     * @return List of users from the remote source
     * @throws Exception if the fetch operation fails
     */
    suspend fun getUsers(): List<User>
}

/**
 * Implementation of [UserRemoteDataSource] that uses Retrofit API to fetch user data.
 * @property api The Retrofit API interface for user-related network calls
 */
class UserRemoteDataSourceImpl(private val api: UserApi) : UserRemoteDataSource {

    override suspend fun getUsers(): List<User> = api.getUsers()
} 