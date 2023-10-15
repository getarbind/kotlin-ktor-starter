package io.collective.start.collector

data class AirQualityData(
    val status: String,
    val data: AirQualityDataContent,
    val id: Int? = null
)

data class AirQualityDataContent(
    val aqi: Int,
    val idx: Int,
    val city: City,
    val forecast: Forecast
)

data class City(
    val name: String
)

data class Forecast(
    val daily: Daily
)

data class Daily(
    val pm10: List<AirQualityMeasurement>
)

data class AirQualityMeasurement(
    val day: String,
    val avg: Int,
    val max: Int,
    val min: Int
)

