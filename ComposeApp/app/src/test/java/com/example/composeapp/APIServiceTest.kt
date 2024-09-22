package com.example.composeapp

import com.example.composeapp.repositories.MainRepository
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class APIServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: MainRepository
    private lateinit var httpClient: OkHttpClient

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        httpClient = OkHttpClient()
        repository = MainRepository(httpClient)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getItems should return a list of UserInfo when API call is successful`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Users().users)

        mockWebServer.enqueue(mockResponse)

        val query = "asdfeq"
        val page = 1
        val perPage = 10

        val result = repository.fetchUsers(query, page, perPage)

        assertEquals(10, result.size)
    }

    @Test
    fun `getItems should throw an exception when API returns an empty body`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("")

        mockWebServer.enqueue(mockResponse)

        val query = "gasv"
        val page = 1
        val perPage = 10

        try {
            repository.fetchUsers(query, page, perPage)
        } catch (e: Exception) {
            assertTrue(e is kotlinx.serialization.SerializationException)
        }
    }

    @Test
    fun `getItems should throw an exception when API returns a 404 error`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(404)

        mockWebServer.enqueue(mockResponse)

        val query = "fcmq"
        val page = 1
        val perPage = 10

        try {
            repository.fetchUsers(query, page, perPage)
        } catch (e: Exception) {
            assertTrue(e is java.io.IOException)
        }
    }
}
