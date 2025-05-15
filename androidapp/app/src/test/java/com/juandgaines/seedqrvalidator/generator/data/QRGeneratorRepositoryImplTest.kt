package com.juandgaines.seedqrvalidator.generator.data

import com.google.common.truth.Truth.assertThat
import com.juandgaines.seedqrvalidator.core.data.network.SeedApi
import com.juandgaines.seedqrvalidator.core.domain.utils.Result
import com.juandgaines.seedqrvalidator.utils.SeedDaoFake
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

class QRGeneratorRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var seedApi: SeedApi
    private lateinit var seedDao: SeedDaoFake
    private lateinit var repository: QRGeneratorRepositoryImpl

    private val testSeedJson = """
        {
            "seed": "5440b58a257f4f759df40eb4dbbec666",
            "expires_at": "2025-05-15T23:00:15.471590097Z"
        }
    """.trimIndent()

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

        seedDao = SeedDaoFake()
        repository = QRGeneratorRepositoryImpl(seedApi, seedDao)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        seedDao.clear()
    }

    @Test
    fun `getSeed returns success with correct data`() = runTest {
        enqueueMockResponse(200, testSeedJson)

        val result = repository.getSeed(null)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        val data = (result as Result.Success).data

        assertThat(data.seed).isEqualTo("5440b58a257f4f759df40eb4dbbec666")
        assertThat(data.expiresAt).isEqualTo("2025-05-15T23:00:15.471590097Z")

        val request = mockWebServer.takeRequest()
        assertThat(request).isNotNull()
        assertThat(request.path).isEqualTo("/v1/seed")
        assertThat(request.method).isEqualTo("GET")
    }

    @Test
    fun `getSeed returns error on network failure`() = runTest {
        // Arrange
        enqueueMockResponse(500, "Internal Server Error")

        val result = repository.getSeed(null)

        assertThat(result).isInstanceOf(Result.Error::class.java)

    }

    private fun enqueueMockResponse(responseCode: Int, body: String) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(body)
                .setHeader("content-type", "application/json".toMediaType().toString())
        )
    }
}