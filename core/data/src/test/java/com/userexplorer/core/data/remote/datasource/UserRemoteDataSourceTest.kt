package com.userexplorer.core.data.remote.datasource

import com.userexplorer.core.data.remote.UserApi
import com.userexplorer.core.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRemoteDataSourceTest {

    private lateinit var dataSource: UserRemoteDataSource
    private lateinit var api: UserApi

    @Before
    fun setup() {
        api = mockk()
        dataSource = UserRemoteDataSourceImpl(api)
    }

    @Test
    fun `getUsers returns empty list when API returns empty list`() = runTest {
        // Arrange
        coEvery { api.getUsers() } returns emptyList()

        // Act
        val result = dataSource.getUsers()

        // Assert
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { api.getUsers() }
    }

    @Test
    fun `getUsers returns list of users when API call succeeds`() = runTest {
        // Arrange
        val users = listOf(testUser, testUser.copy(id = 2))
        coEvery { api.getUsers() } returns users

        // Act
        val result = dataSource.getUsers()

        // Assert
        assertEquals(users, result)
        assertEquals(2, result.size)
        coVerify(exactly = 1) { api.getUsers() }
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