package io.collective.airquality

data class AirQualityAnalysisRecord(val id: Long, val city: String,
                                    val avg_pm10_avg: Int, val avg_pm10_max: Int, val avg_pm10_min: Int,
                                    val p90_pm10_avg: Int, val p90_pm10_max: Int, val p90_pm10_min: Int)