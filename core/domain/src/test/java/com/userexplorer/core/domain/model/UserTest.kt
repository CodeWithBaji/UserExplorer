package com.userexplorer.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {
    @Test
    fun `create user with valid data`() {
        // Arrange
        val id = 1
        val name = "Test User"
        val username = "tester"
        val email = "test@example.com"
        val phone = "1234567890"
        val company = "Test Company"
        val address = "Test Address"
        val zip = "12345"
        val state = "Test State"
        val country = "Test Country"
        val photo = "https://example.com/photo.jpg"

        // Act
        val user = User(
            id = id,
            name = name,
            username = username,
            email = email,
            phone = phone,
            company = company,
            address = address,
            zip = zip,
            state = state,
            country = country,
            photo = photo
        )

        // Assert
        assertEquals(id, user.id)
        assertEquals(name, user.name)
        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(phone, user.phone)
        assertEquals(company, user.company)
        assertEquals(address, user.address)
        assertEquals(zip, user.zip)
        assertEquals(state, user.state)
        assertEquals(country, user.country)
        assertEquals(photo, user.photo)
    }

    @Test
    fun `user data class equals works correctly`() {
        // Arrange
        val user1 = User(
            id = 1,
            name = "Test User",
            username = "tester",
            email = "test@example.com",
            phone = "1234567890",
            company = "Test Company",
            address = "Test Address",
            zip = "12345",
            state = "Test State",
            country = "Test Country",
            photo = "https://example.com/photo.jpg"
        )

        // Act
        val user2 = User(
            id = 1,
            name = "Test User",
            username = "tester",
            email = "test@example.com",
            phone = "1234567890",
            company = "Test Company",
            address = "Test Address",
            zip = "12345",
            state = "Test State",
            country = "Test Country",
            photo = "https://example.com/photo.jpg"
        )

        // Assert
        assertEquals(user1, user2)
    }

    @Test
    fun `user data class copy works correctly`() {
        // Arrange
        val originalUser = User(
            id = 1,
            name = "Test User",
            username = "tester",
            email = "test@example.com",
            phone = "1234567890",
            company = "Test Company",
            address = "Test Address",
            zip = "12345",
            state = "Test State",
            country = "Test Country",
            photo = "https://example.com/photo.jpg"
        )

        // Act
        val copiedUser = originalUser.copy(
            name = "Updated Name",
            email = "updated@example.com"
        )

        // Assert
        assertEquals(originalUser.id, copiedUser.id)
        assertEquals("Updated Name", copiedUser.name)
        assertEquals("updated@example.com", copiedUser.email)
        assertEquals(originalUser.username, copiedUser.username)
        assertEquals(originalUser.phone, copiedUser.phone)
        assertEquals(originalUser.company, copiedUser.company)
        assertEquals(originalUser.address, copiedUser.address)
        assertEquals(originalUser.zip, copiedUser.zip)
        assertEquals(originalUser.state, copiedUser.state)
        assertEquals(originalUser.country, copiedUser.country)
        assertEquals(originalUser.photo, copiedUser.photo)
    }
} 