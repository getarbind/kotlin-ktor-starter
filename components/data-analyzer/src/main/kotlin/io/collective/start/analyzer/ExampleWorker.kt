package io.collective.start.analyzer

import io.collective.airquality.AirQualityAnalysisRecord
import io.collective.workflow.Worker
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import io.collective.airquality.AirQualityDataGateway
import io.collective.airquality.AirQualityRecord
import io.collective.database.createDatasource
import io.collective.database.DatabaseTemplate

class ExampleWorker(override val name: String = "data-analyzer") : Worker<ExampleTask> {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun execute(task: ExampleTask) {
        runBlocking {
            logger.info("starting data analysis.")

            // todo - data analysis happens here
            val logger = LoggerFactory.getLogger(this.javaClass)
            val jdbcUrl="jdbc:postgresql://localhost:5432/airquality_development"
            val username="airquality"
            val password="airquality"

            val dataSource = createDatasource(jdbcUrl, username, password)
            val gateway = AirQualityDataGateway(dataSource)


            val analysisRecord : AirQualityAnalysisRecord
            val records: List<AirQualityRecord> = gateway.findAll()
            val city = records.map { it.city }.get(0)
            logger.info("City : $city")

            // Calculate the average of pm10_avg
            val pm10AvgValues = records.map { it.pm10_avg }
            val averagePm10Avg = pm10AvgValues.average()

            // Calculate the maximum and minimum pm10_avg
            val maxPm10Avg = pm10AvgValues.maxOrNull()
            val minPm10Avg = pm10AvgValues.minOrNull()

            // log the result
            logger.info("Average pm10_avg: $averagePm10Avg")
            logger.info("Maximum pm10_avg: $maxPm10Avg")
            logger.info("Minimum pm10_avg: $minPm10Avg")


            // Calculate the average of pm10_max values
            val pm10MaxValues = records.map { it.pm10_max }
            val averagePm10Max = pm10MaxValues.average()

            // Calculate the maximum and minimum pm10_avg values
            val maxPm10Max = pm10MaxValues.maxOrNull()
            val minPm10Max = pm10MaxValues.minOrNull()

            // Log the results
            logger.info("Average pm10_max: $averagePm10Max")
            logger.info("Maximum pm10_max: $maxPm10Max")
            logger.info("Minimum pm10_max: $minPm10Max")

            // Calculate the average of pm10_max
            val pm10MinValues = records.map { it.pm10_min }
            val averagePm10Min = pm10MinValues.average()

            // Calculate the max and min of pm10_avg
            val maxPm10Min = pm10MinValues.maxOrNull()
            val minPm10Min = pm10MinValues.minOrNull()

            // Log the results
            logger.info("Average pm10_min: $averagePm10Min")
            logger.info("Maximum pm10_min: $maxPm10Min")
            logger.info("Minimum pm10_min: $minPm10Min")

            // Specify the desired percentile
            val percentile = 90

            // Sort the records by pm10_avg in ascending order
            val sortedRecords = records.sortedBy { it.pm10_avg }

            // Calculate the position of the percentile value
            val position = (percentile.toDouble() / 100.0) * sortedRecords.size

            // Calculate the percentile value
            val percentileValue = if (position % 1.0 == 0.0) {
                // If the position is an integer, take the average of the values at that position and the next
                val lowerIndex = position.toInt() - 1
                val upperIndex = position.toInt()
                (sortedRecords[lowerIndex].pm10_avg + sortedRecords[upperIndex].pm10_avg) / 2.0
            } else {
                // If the position is not an integer, round to the nearest integer
                val index = position.toInt()
                sortedRecords[index].pm10_avg
            }

            logger.info("$percentile-th percentile pm10_avg value: $percentileValue")

            // Sort the records by pm10_max in ascending order
            val sortedRecords1 = records.sortedBy { it.pm10_max }

            // Calculate the position of the percentile value
            val position1 = (percentile.toDouble() / 100.0) * sortedRecords1.size

            // Calculate the percentile value
            val percentileValue1 = if (position1 % 1.0 == 0.0) {
                // If the position is an integer, take the average of the values at that position and the next
                val lowerIndex = position1.toInt() - 1
                val upperIndex = position1.toInt()
                (sortedRecords1[lowerIndex].pm10_max + sortedRecords1[upperIndex].pm10_max) / 2.0
            } else {
                // If the position is not an integer, round to the nearest integer
                val index = position1.toInt()
                sortedRecords1[index].pm10_max
            }

            logger.info("$percentile-th percentile pm10_max value: $percentileValue1")

            // Sort the records by pm10_min in ascending order
            val sortedRecords2 = records.sortedBy { it.pm10_min }

            // Calculate the position of the percentile value
            val position2 = (percentile.toDouble() / 100.0) * sortedRecords2.size

            // Calculate the percentile value
            val percentileValue2 = if (position2 % 1.0 == 0.0) {
                // If the position is an integer, take the average of the values at that position and the next
                val lowerIndex = position2.toInt() - 1
                val upperIndex = position2.toInt()
                (sortedRecords2[lowerIndex].pm10_min + sortedRecords2[upperIndex].pm10_min) / 2.0
            } else {
                // If the position is not an integer, round to the nearest integer
                val index = position1.toInt()
                sortedRecords2[index].pm10_min
            }

            logger.info("$percentile-th percentile pm10_min value: $percentileValue2")

            gateway.create2(city, averagePm10Avg.toInt(), averagePm10Max.toInt(), averagePm10Min.toInt(),
                percentileValue.toInt(), percentileValue1.toInt(), percentileValue2.toInt())

            logger.info("Analysed data committed")

            logger.info("completed data analysis.")
        }
    }
}