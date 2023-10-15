package io.collective.airquality

import io.collective.database.DatabaseTemplate
import io.collective.database.TransactionManager
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class AirQualityDataGateway(private val dataSource: DataSource) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val template = DatabaseTemplate(dataSource)

    //insert into airquality
    fun create(city: String, date: String, pm10_avg: Int, pm10_max: Int, pm10_min: Int): AirQualityRecord {
        return template.create(
                "insert into airquality (city, date, pm10_avg, pm10_max, pm10_min) values (?, ?, ?, ?, ?)", { id ->
                AirQualityRecord(id, city, date, pm10_avg, pm10_max, pm10_min)
        }, city, date, pm10_avg, pm10_max, pm10_min
        )
    }

    //insert into airqualityanalysis
    fun create2(city: String, avg_pm10_avg: Int, avg_pm10_max: Int, avg_pm10_min: Int,
                p90_pm10_avg: Int, p90_pm10_max: Int, p90_pm10_min: Int): AirQualityAnalysisRecord {
        return template.create(
            "insert into airqualityanalysis (city, avg_pm10_avg, avg_pm10_max, avg_pm10_min, p90_pm10_avg, p90_pm10_max, p90_pm10_min) " +
                    "values (?, ?, ?, ?, ?, ?, ?)", { id ->
                AirQualityAnalysisRecord(id, city, avg_pm10_avg, avg_pm10_max, avg_pm10_min, p90_pm10_avg, p90_pm10_max, p90_pm10_min)
            }, city, avg_pm10_avg, avg_pm10_max, avg_pm10_min, p90_pm10_avg, p90_pm10_max, p90_pm10_min
        )
    }

    fun findAll(): List<AirQualityRecord> {
        return template.findAll("select id, city, date, pm10_avg, pm10_max, pm10_min from airquality order by id") { rs ->
            AirQualityRecord(rs.getLong(1), rs.getString(2), rs.getString(3),
                rs.getInt(4), rs.getInt(5), rs.getInt(6))
        }
    }
//
//    fun findBy(id: Long): AirQualityRecord? {
//        return template.findBy(
//                "select id, name, quantity from products where id = ?", { rs ->
//                AirQualityRecord(rs.getLong(1), rs.getString(2), rs.getInt(3))
//        }, id
//        )
//    }
//
//    fun update(product: AirQualityRecord): AirQualityRecord {
//        template.update(
//                "update products set name = ?, quantity = ? where id = ?",
//                product.name, product.quantity, product.id
//        )
//        return product
//    }



}