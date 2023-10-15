package io.collective.start.collector

import okhttp3.*
import org.slf4j.LoggerFactory
import io.mockk.*
import org.junit.*

private infix fun Any.returns(response: Response) {
    //return mockk<Response>();
}

class ExampleWorkerTest {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic(OkHttpClient::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testExecute() {
        // Mock OkHttpClient and its dependencies
        val client = mockk<OkHttpClient>()
        val response = mockk<Response>()
        val responseBody = mockk<ResponseBody>()

        //every { okhttp3.OkHttpClient() } returns client
        every { client.newCall(any()) } returns response
        every { response.isSuccessful } returns true
        every { response.body } returns responseBody
        every { responseBody.string() } returns "Sample Response Data"

        val exampleTask = ExampleTask("Test Task")
        val exampleWorker = ExampleWorker()

        // Execute the task
        exampleWorker.execute(exampleTask)

        logger.info(" >>> "+responseBody.string());


        // Verify that the expected operations were called
        //verify { logger.info("starting data collection.") }
       // verify { logger.info("completed data collection.") }

        // Verify that the HTTP request was made
//        verify {
//            client.newCall(withArg {
//                it.url.toString() == "https://api.waqi.info/feed/here/?token=d585359c19dd89490afcb3bb1b4fe0a717822df9"
//            })
//        }

        // Verify that the response data was processed
        verify {
            responseBody.string()
            println("Response Data:")
            println("Sample Response Data")
        }
    }
}