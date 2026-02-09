package hsrm.mi.campusapp.data.api.openmeteo

import hsrm.mi.campusapp.domain.model.Campus
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

object OpenMeteoAPI {

    lateinit var client: HttpClient

    fun init(c : HttpClient) {
        client = c
    }
    private val json = Json {
        ignoreUnknownKeys = true
    }

    const val BASE_URL = "https://api.open-meteo.com/v1"

    suspend fun getCurrentWeather(campus: Campus, getIsRaining: Boolean = true, getIsDay: Boolean = true, getCloudCoverage: Boolean = true, getWindSpeed: Boolean = true): WeatherDTO? {
        println("GETTING FORECAST FOR: $campus")

        val currentParameterValues = StringBuilder("temperature_2m")
        if(getIsRaining) currentParameterValues.append(",rain")
        if (getIsDay) currentParameterValues.append(",is_day")
        if (getCloudCoverage) currentParameterValues.append(",cloud_cover")
        if (getWindSpeed) currentParameterValues.append(",wind_speed_10m")

        println("CURRENT PARAMETER VALUES: $currentParameterValues")

        val jsonResponse: String = client.get("$BASE_URL/forecast") {
            parameter("latitude", campus.center.latitude)
            parameter("longitude", campus.center.longitude)
            parameter("current", currentParameterValues)
        }.bodyAsText()

        println("RAW Response: $jsonResponse")

        return try {
            val response: ForecastResponse = json.decodeFromString(jsonResponse)
            response.current
            response.current
        } catch (e: Exception) {
            println("Error deserializing ForecastResponse: ${e.message}")
            null
        }

    }

}