package com.juandgaines.seedqrvalidator.scanner.data

import com.google.common.truth.Truth.assertThat
import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ScannerRepositoryImplTest {
    
    private lateinit var mockWebServer: MockWebServer
    private lateinit var seedApi: SeedApi
    private lateinit var repository: ScannerRepositoryImpl

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }


        seedApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
             json.asConverterFactory( "application/json; charset=UTF8".toMediaType())
            )
            .build()
            .create(SeedApi::class.java)

        repository = ScannerRepositoryImpl(seedApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `validateSeed returns success when API returns 204`() = runTest {

        val testSeed = "5440b58a257f4f759df40eb4dbbec666"
        val mockResponse = MockResponse()
            .setResponseCode(204)
            .setBody("")

        mockWebServer.enqueue(mockResponse)

        val result = repository.validateSeed(testSeed)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        

        val request = mockWebServer.takeRequest()
        assertThat(request.path).isEqualTo("/v1/seed/validate/$testSeed")
        assertThat(request.method).isEqualTo("GET")
    }


    @Test
    fun `validateSeed returns SERVER_ERROR when API returns 500`() = runTest {

        val testSeed = "5440b58a257f4f759df40eb4dbbec666"
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("Internal Server Error")

        mockWebServer.enqueue(mockResponse)

        val result = repository.validateSeed(testSeed)


        assertThat(result).isInstanceOf(Result.Error::class.java)

    }

}