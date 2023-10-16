package io.collective.start

import freemarker.cache.ClassTemplateLoader
import io.collective.airquality.AirQualityDataGateway
import io.collective.airquality.AirQualityRecord
import io.collective.database.createDatasource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import java.util.*

fun Application.module() {
    val logger = LoggerFactory.getLogger(this.javaClass)
    val jdbcUrl="jdbc:postgresql://localhost:5432/airquality_development"
    val username="airquality"
    val password="airquality"

    val dataSource = createDatasource(jdbcUrl, username, password)
    val gateway = AirQualityDataGateway(dataSource)
    val allRows = mutableListOf<List<Any>>()

    val gatewayList = gateway.findAll()
    val rootMp = mutableMapOf<String, Any>()
    var rowStr : StringBuffer
    rowStr = StringBuffer("Analysed Air Quality Index Data \n\n")

    gatewayList.forEach { gateway ->
        val oneRow = mutableListOf<Any>()
        oneRow.add(gateway.id)
        oneRow.add(gateway.city)
        oneRow.add(gateway.date)
        oneRow.add(gateway.pm10_avg)
        oneRow.add(gateway.pm10_max)
        oneRow.add(gateway.pm10_min)
        rootMp[gateway.id.toString()] = oneRow
        logger.info(">>>>>>>>>>>>>>>>$oneRow")
        allRows.add(oneRow)
        rowStr.append(oneRow.toString()).append("|")
    }
    logger.info("Root map Size : ")
    logger.info(rootMp.size.toString())

    install(DefaultHeaders)
    install(CallLogging)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(Routing) {
        get("/health-check") {
            call.respondText("hi!", ContentType.Text.Html)
        }
        get("/") {
            //call.respond(FreeMarkerContent("index.ftl", mapOf("headers" to headers())))
            call.respond(FreeMarkerContent("index.ftl", mapOf("headers" to allRows)))
        }
        static("images") { resources("images") }
        static("style") { resources("style") }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.headers(): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    call.request.headers.entries().forEach { entry ->
        headers[entry.key] = entry.value.joinToString()
    }
    return headers
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8888
    embeddedServer(Netty, port, watchPaths = listOf("basic-server"), module = { module() }).start()
}
