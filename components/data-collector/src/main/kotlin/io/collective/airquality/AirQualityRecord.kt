package io.collective.airquality

data class AirQualityRecord(val id: Long, val city: String, val date: String,
                            val pm10_avg: Int, val pm10_max: Int,
                            val pm10_min: Int)