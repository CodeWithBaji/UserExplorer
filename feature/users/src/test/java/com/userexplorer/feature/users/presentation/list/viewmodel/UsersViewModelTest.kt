package com.userexplorer.feature.users.presentation.list.viewmodel

import com.userexplorer.core.domain.model.User
import com.userexplorer.core.domain.repository.UserRepository
import com.userexplorer.core.domain.util.Result
import com.userexplorer.feature.users.presentation.list.contract.UsersContract
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private val repository: UserRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Success(emptyList())

        // Act
        val viewModel = UsersViewModel(repository)

        // Assert
        assertTrue(viewModel.uiState.value is UsersContract.State.Loading)
    }

    @Test
    fun `loads users successfully`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Success(listOf(testUser))

        // Act
        val viewModel = UsersViewModel(repository)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as UsersContract.State.Success
        assertEquals(listOf(testUser), state.users)
    }

    @Test
    fun `shows error when load fails`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Error("Network error")

        // Act
        val viewModel = UsersViewModel(repository)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as UsersContract.State.Error
        assertEquals("Network error", state.message)
    }

    @Test
    fun `retry loads users after error`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Error("Network error") andThen
            Result.Success(listOf(testUser))
        val viewModel = UsersViewModel(repository)
        advanceUntilIdle()

        // Act
        viewModel.handleIntent(UsersContract.Intent.Retry)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as UsersContract.State.Success
        assertEquals(listOf(testUser), state.users)
    }

    @Test
    fun `findUser returns matching user`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Success(listOf(testUser))
        val viewModel = UsersViewModel(repository)
        advanceUntilIdle()

        // Act
        val result = viewModel.findUser(testUser.id)

        // Assert
        assertEquals(testUser, result)
    }

    @Test
    fun `findUser returns null for unknown id`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Success(listOf(testUser))
        val viewModel = UsersViewModel(repository)
        advanceUntilIdle()

        // Act
        val result = viewModel.findUser(999)

        // Assert
        assertNull(result)
    }

    @Test
    fun `handles empty list`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Success(emptyList())

        // Act
        val viewModel = UsersViewModel(repository)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as UsersContract.State.Success
        assertTrue(state.users.isEmpty())
    }

    @Test
    fun `retry calls repository again`() = runTest {
        // Arrange
        coEvery { repository.getUsers() } returns Result.Error("Error")
        val viewModel = UsersViewModel(repository)
        advanceUntilIdle()

        // Act
        viewModel.handleIntent(UsersContract.Intent.Retry)
        advanceUntilIdle()
        viewModel.handleIntent(UsersContract.Intent.Retry)
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 3) { repository.getUsers() }
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