package com.userexplorer.core.data.repository

import com.userexplorer.core.data.remote.datasource.UserRemoteDataSource
import com.userexplorer.core.domain.model.User
import com.userexplorer.core.domain.repository.UserRepository
import com.userexplorer.core.domain.util.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of [UserRepository] that coordinates data operations between
 * different data sources (currently only remote).
 * 
 * @property remoteDataSource The data source for remote operations
 */
class UserRepositoryImpl(private val remoteDataSource: UserRemoteDataSource) : UserRepository {
    
    override suspend fun getUsers(): Result<List<User>> = withContext(Dispatchers.IO) {
        try {
            val users = remoteDataSource.getUsers()
            Result.Success(users)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
} 