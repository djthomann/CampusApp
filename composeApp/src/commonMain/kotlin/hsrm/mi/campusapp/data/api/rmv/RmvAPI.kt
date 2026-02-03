package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Stop
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

object RmvAPI {

    const val SEARCH_TIMEFRAME_MINUTES = 180

    const val DEFAULT_NUM_DEPARTURES = 3
    private val json = Json {
        ignoreUnknownKeys = true
    }

    val client = HttpClient()

    const val BASE_URL = "https://www.rmv.de/hapi/departureBoard"
    val rmvApiKey = ApiKeys.RMV

    suspend fun getNextDepartures(stop: Stop, numDepartures: Int = DEFAULT_NUM_DEPARTURES): List<DepartureDTO> {
        println("GETTING ARRIVALS FOR: ${stop.name}")

        val jsonResponse: String = client.get(BASE_URL) {
            parameter("accessId", rmvApiKey)
            parameter("id", stop.id)
            parameter("duration", SEARCH_TIMEFRAME_MINUTES)
            parameter("maxJourneys", numDepartures)
            parameter("format", "json")
        }.bodyAsText()

        println("RAW Response: $jsonResponse")

        return try {
            val response: DepartureResponse = json.decodeFromString(jsonResponse)
            response.departures
        } catch (e: Exception) {
            println("Error deserializing DepartureResponse: ${e.message}")
            emptyList()
        }
    }

    suspend fun getJourneyDetails(journeyId: String): JourneyDTO {
        return getJourneyDetailsFromStop(journeyId, null)
    }

    suspend fun getJourneyDetailsFromStop(journeyId: String, stop: Stop?): JourneyDTO {
        println("GETTING JOURNEY DETAILS FOR: ${journeyId}")

        val jsonResponse: String = client.get("https://www.rmv.de/hapi/journeyDetail") {
            parameter("accessId", rmvApiKey)
            parameter("id", journeyId)
            parameter("format", "json")
            stop?.let { parameter("fromId", stop.id) }
        }.bodyAsText()

        println("RAW Response: $jsonResponse")

        return try {
            json.decodeFromString<JourneyDTO>(jsonResponse)
        } catch (e: Exception) {
            println("Error deserializing Journey Response: ${e.message}")
            JourneyDTO(StopsWrapper(emptyList()))
        }
    }

    fun String.normalizeRmvId(): String {
        // Removes point in time info
        return if (this.contains("@p=")) {
            this.substringBefore("@p=") + "@"
        } else {
            this
        }
    }


}

expect object ApiKeys {
    val RMV: String
}