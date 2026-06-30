package com.userexplorer.core.data.repository

import com.userexplorer.core.data.remote.datasource.UserRemoteDataSource
import com.userexplorer.core.domain.model.User
import com.userexplorer.core.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl
    private lateinit var remoteDataSource: UserRemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk()
        repository = UserRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getUsers returns Success with list of users when data source call succeeds`() = runTest {
        // Arrange
        val users = listOf(testUser)
        coEvery { remoteDataSource.getUsers() } returns users

        // Act
        val result = repository.getUsers()

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(users, (result as Result.Success).data)
    }

    @Test
    fun `getUsers returns Error with message when data source call fails with exception`() = runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { remoteDataSource.getUsers() } throws Exception(errorMessage)

        // Act
        val result = repository.getUsers()

        // Assert
        assertTrue(result is Result.Error)
        assertEquals(errorMessage, (result as Result.Error).message)
    }

    @Test
    fun `getUsers returns Error with default message when data source call fails with null message`() = runTest {
        // Arrange
        coEvery { remoteDataSource.getUsers() } throws RuntimeException()

        // Act
        val result = repository.getUsers()

        // Assert
        assertTrue(result is Result.Error)
        assertEquals("An unknown error occurred", (result as Result.Error).message)
    }

    @Test
    fun `getUsers returns Success with empty list when data source returns empty list`() = runTest {
        // Arrange
        val emptyList = emptyList<User>()
        coEvery { remoteDataSource.getUsers() } returns emptyList()

        // Act
        val result = repository.getUsers()

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(emptyList, (result as Result.Success).data)
    }

    @Test
    fun `getUsers returns Success with single user when data source returns single user`() = runTest {
        // Arrange
        val users = listOf(testUser)
        coEvery { remoteDataSource.getUsers() } returns users

        // Act
        val result = repository.getUsers()

        // Assert
        assertTrue(result is Result.Success)
        assertEquals(users, (result as Result.Success).data)
        assertEquals(1, result.data.size)
        assertEquals(testUser, result.data.first())
    }

    private val testUser = User(
        id = 2,
        name = "Tony Stark",
        username = "ironman",
        email = "tony.stark@example.com",
        phone = "+1 555 0102",
        company = "Stark Industries",
        address = "10880 Malibu Point",
        zip = "90265",
        state = "California",
        country = "USA",
        photo = "",
    )
}