package com.userexplorer.core.domain.repository

import com.userexplorer.core.domain.model.User
import com.userexplorer.core.domain.util.Result

/**
 * Repository interface for managing user data.
 * Provides methods to fetch and manipulate user information.
 */
interface UserRepository {
    /**
     * Retrieves a list of all users from the data source.
     *
     * @return A [Result] object containing either:
     *         - Success with a list of users
     *         - Error with an error message
     *         - Loading state
     */
    suspend fun getUsers(): Result<List<User>>
} 