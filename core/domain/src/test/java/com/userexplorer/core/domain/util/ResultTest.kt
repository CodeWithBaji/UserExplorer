package com.userexplorer.core.domain.util

import org.junit.Assert.assertEquals
import org.junit.Test

class ResultTest {
    @Test
    fun `success result holds correct data`() {
        // Arrange
        val data = "test data"
        
        // Act
        val result = Result.Success(data)
        
        // Assert
        assertEquals(data, result.data)
    }

    @Test
    fun `error result holds correct message`() {
        // Arrange
        val message = "error message"
        
        // Act
        val result = Result.Error(message)
        
        // Assert
        assertEquals(message, result.message)
    }
}