package com.userexplorer.feature.users.presentation.list.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.userexplorer.core.domain.model.User
import com.userexplorer.core.domain.repository.UserRepository
import com.userexplorer.core.domain.util.Result
import com.userexplorer.feature.users.R
import com.userexplorer.feature.users.presentation.list.viewmodel.UsersViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class UsersScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun displaysUsersWhenLoadSucceeds() {
        // Arrange
        val repository = mockk<UserRepository>()
        coEvery { repository.getUsers() } returns Result.Success(listOf(testUser))

        // Act
        launchScreen(repository)

        // Assert
        composeTestRule.waitUntilAtLeastOneExists(hasText(testUser.name), 5_000)
        composeTestRule.onNodeWithText(testUser.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(testUser.email).assertIsDisplayed()
    }

    @Test
    fun displaysErrorMessageAndRetryButtonWhenLoadFails() {
        // Arrange
        val repository = mockk<UserRepository>()
        coEvery { repository.getUsers() } returns Result.Error("Network error")

        // Act
        launchScreen(repository)

        // Assert
        composeTestRule.waitUntilAtLeastOneExists(hasText("Network error"), 5_000)
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.retry_button)
        ).assertIsDisplayed()
    }

    @Test
    fun invokesOnUserClickWhenUserCardIsClicked() {
        // Arrange
        val repository = mockk<UserRepository>()
        coEvery { repository.getUsers() } returns Result.Success(listOf(testUser))
        var clickedUserId: Int? = null

        // Act
        launchScreen(repository) { clickedUserId = it.id }
        composeTestRule.waitUntilAtLeastOneExists(hasText(testUser.name), 5_000)
        composeTestRule.onNodeWithText(testUser.name).performClick()

        // Assert
        assert(clickedUserId == testUser.id)
    }

    @Test
    fun displaysEmptyStateWhenNoUsersAreReturned() {
        // Arrange
        val repository = mockk<UserRepository>()
        coEvery { repository.getUsers() } returns Result.Success(emptyList())

        // Act
        launchScreen(repository)

        // Assert
        val emptyMessage = composeTestRule.activity.getString(R.string.no_users_found)
        composeTestRule.waitUntilAtLeastOneExists(hasText(emptyMessage), 5_000)
        composeTestRule.onNodeWithText(emptyMessage).assertIsDisplayed()
    }

    @Test
    fun retryButtonReloadsUsersAfterError() {
        // Arrange
        val repository = mockk<UserRepository>()
        coEvery { repository.getUsers() } returns Result.Error("Network error") andThen
            Result.Success(listOf(testUser))

        // Act
        launchScreen(repository)
        composeTestRule.waitUntilAtLeastOneExists(hasText("Network error"), 5_000)
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.retry_button)
        ).performClick()

        // Assert
        composeTestRule.waitUntilAtLeastOneExists(hasText(testUser.name), 5_000)
        composeTestRule.onNodeWithText(testUser.name).assertIsDisplayed()
    }

    private fun launchScreen(
        repository: UserRepository,
        onUserClick: (User) -> Unit = {},
    ) {
        composeTestRule.setContent {
            MaterialTheme {
                UsersScreen(
                    viewModel = UsersViewModel(repository),
                    onUserClick = onUserClick,
                )
            }
        }
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