package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.DepartureResponse
import hsrm.mi.campusapp.domain.model.Stop
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

object RmvAPI {

    const val SEARCH_TIMEFRAME_MINUTES = 180
    private val json = Json {
        ignoreUnknownKeys = true
    }


    val client = HttpClient()

    val baseURL = "localhost"
    val rmvApiKey = ApiKeys.RMV

    fun getArrivalBoard(stop: Stop): String {
        return rmvApiKey
    }

    suspend fun getNextArrivals(stop: Stop, numArrivals: Int = 2): List<Departure> {
        println("GETTING ARRIVALS FOR: ${stop.name}")

        val jsonResponse: String = client.get("https://www.rmv.de/hapi/departureBoard") {
            parameter("accessId", rmvApiKey)
            parameter("id", stop.id)
            parameter("duration", SEARCH_TIMEFRAME_MINUTES)
            parameter("maxJourneys", numArrivals)
            parameter("format", "json")
        }.bodyAsText()

        println("RAW Response: $jsonResponse")

        return try {
            val response: DepartureResponse = json.decodeFromString(jsonResponse)
            response.departure
        } catch (e: Exception) {
            println("Error deserializing DepartureResponse: ${e.message}")
            emptyList()
        }
    }


}

expect object ApiKeys {
    val RMV: String
}