package test.collective.start.collector

import io.collective.start.collector.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.mockk.*
import okhttp3.*
import okhttp3.OkHttpClient


class AppKtTest {

    @Before
    fun setUp() {
        mockkStatic(OkHttpClient::class)

        // Mock OkHttpClient and its dependencies
        val client = mockk<OkHttpClient>()
        val response = mockk<Response>()
        val responseBody = mockk<ResponseBody>()

        every { OkHttpClient() } returns client
        //every { client.newCall(any()) } returns response
        every { response.isSuccessful } returns true
        every { response.body } returns responseBody
        every { responseBody.string() } returns "Sample Response Data"
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    //@Test
    fun testEmptyHome() = testApp {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)
            assertTrue(response.content!!.contains("hi!"))
        }
    }

    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }) { callback() }
    }
}

//private infix fun Any.returns(response: Response) {
//
//}
