package io.collective.start.collector

//import com.google.gson.*
import io.collective.workflow.Worker
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import io.collective.airquality.*
import io.collective.database.*
import io.collective.database.TransactionManager;


class ExampleWorker(override val name: String = "data-collector") : Worker<ExampleTask> {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun execute(task: ExampleTask) {
        runBlocking {
            logger.info("starting data collection.")

            val jdbcUrl = System.getenv("JDBC_DATABASE_URL")
            val username = System.getenv("JDBC_DATABASE_USERNAME")
            val password = System.getenv("JDBC_DATABASE_USERNAME")

            val jdbcUrl1="jdbc:postgresql://localhost:5432/airquality_development"
            val username1="airquality"
            val password1="airquality"

            val dataSource = createDatasource(jdbcUrl1, username1, password1)

            // todo - data collection happens here
            println(">>> Executing Task: $task ")

            // Initialize OkHttpClient
            val client = OkHttpClient()

            // Define the API URL you want to fetch data from
            val apiUrl = "https://api.waqi.info/feed/here/?token=d585359c19dd89490afcb3bb1b4fe0a717822df9"

            // Create a GET request
            val request = Request.Builder()
                .url(apiUrl)
                .build()

            // Send the request and handle the response
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("Failed to fetch data: ${response.code}")
                    //return
                }

                // Get the response body as a string
                val responseBody = response.body?.string()

                // Print the response or process it further as needed
                println("Response Data:")
                println(responseBody)

                val gson = com.google.gson.Gson()
                val airQualityData = gson.fromJson(responseBody, AirQualityData::class.java)

                val allRows = mutableListOf<List<Any>>()

                var id = 1
                val gateway = AirQualityDataGateway(dataSource)
                for (measurement in airQualityData.data.forecast.daily.pm10) {
                    gateway.create(airQualityData.data.city.name, measurement.day, measurement.avg, measurement.max, measurement.min)

                    allRows.add(listOf(id, airQualityData.data.city.name, measurement.day, measurement.avg, measurement.max, measurement.min))
                    id++
                }
                logger.info(allRows.toString())

                logger.info("From Database : - ")
                val allRows1 = mutableListOf<List<Any>>()

                val gatewayList = gateway.findAll()
                gatewayList.forEach { gateway ->
                    val oneRow = mutableListOf<Any>()
                    oneRow.add(gateway.id)
                    oneRow.add(gateway.city)
                    oneRow.add(gateway.date)
                    oneRow.add(gateway.pm10_avg)
                    oneRow.add(gateway.pm10_max)
                    oneRow.add(gateway.pm10_min)
                    logger.info(">>>>>>>>>>>>>>>>$oneRow")
                    allRows1.add(oneRow)
                }

            }

            logger.info("completed data collection.")

        }
    }
}