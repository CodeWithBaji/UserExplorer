package com.userexplorer.core.data

import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkConfigTest {

    @Test
    fun `network config has expected values`() {
        // Assert
        assertEquals("https://fake-json-api.mock.beeceptor.com/", NetworkConfig.BASE_URL)
        assertEquals("users", NetworkConfig.USERS_ENDPOINT)
        assertEquals(60L, NetworkConfig.CONNECT_TIMEOUT_SECONDS)
        assertEquals(60L, NetworkConfig.READ_TIMEOUT_SECONDS)
        assertEquals("application/json", NetworkConfig.JSON_MEDIA_TYPE)
    }
}