package com.userexplorer.feature.users.presentation.detail.ui

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.userexplorer.core.domain.model.User
import com.userexplorer.feature.users.R
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun displaysUserDetails() {
        // Act
        composeTestRule.setContent {
            MaterialTheme {
                UserDetailScreen(user = testUser, onBackClick = {})
            }
        }

        // Assert
        val context = composeTestRule.activity
        composeTestRule.onNodeWithText(context.getString(R.string.user_details_screen_title))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(testUser.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(testUser.email).assertIsDisplayed()
        composeTestRule.onNodeWithText(testUser.company).assertIsDisplayed()
    }

    @Test
    fun invokesOnBackClickWhenBackButtonIsPressed() {
        // Arrange
        var backClicked = false

        // Act
        composeTestRule.setContent {
            MaterialTheme {
                UserDetailScreen(
                    user = testUser,
                    onBackClick = { backClicked = true },
                )
            }
        }
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.back_button)
        ).performClick()

        // Assert
        assertTrue(backClicked)
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
