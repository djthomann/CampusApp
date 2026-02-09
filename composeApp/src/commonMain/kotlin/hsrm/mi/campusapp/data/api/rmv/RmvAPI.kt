package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Stop
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.json.Json

object RmvAPI {

    lateinit var client: HttpClient

    fun init(c : HttpClient) {
        client = c
    }

    /* TODO() Work with result types instead to mirror images when fetching */

    const val SEARCH_TIMEFRAME_MINUTES = 180

    const val DEFAULT_NUM_DEPARTURES = 3

    const val DEFAULT_NUM_LOCATIONS = 4

    const val ARRIVAL_MINUTES_BEFORE_COURSE_STARTS = 10

    const val ARRIVAL_SECONDS_BEFORE_COURSE_STARTS = ARRIVAL_MINUTES_BEFORE_COURSE_STARTS * 60
    private val json = Json {
        ignoreUnknownKeys = true
    }

    const val BASE_URL = "https://www.rmv.de/hapi"
    val rmvApiKey = ApiKeys.RMV

    suspend fun getNextDepartures(stop: Stop, numDepartures: Int = DEFAULT_NUM_DEPARTURES): List<DepartureDTO> {
        println("GETTING ARRIVALS FOR: ${stop.name}")

        val jsonResponse: String = client.get("$BASE_URL/departureBoard") {
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
        println("GETTING JOURNEY DETAILS FOR: $journeyId")

        val jsonResponse: String = client.get("$BASE_URL/journeyDetail") {
            parameter("accessId", rmvApiKey)
            parameter("id", journeyId)
            parameter("format", "json")
            stop?.let { parameter("fromId", stop.id) }
        }.bodyAsText()

        // println("RAW Response: $jsonResponse")

        return try {
            json.decodeFromString<JourneyDTO>(jsonResponse)
        } catch (e: Exception) {
            println("Error deserializing Journey Response: ${e.message}")
            JourneyDTO(StopsWrapper(emptyList()))
        }
    }

    suspend fun getArrivalTripFromStopToCampus(stop: Stop, latitude: Double, longitude: Double, arrivalDateTime: LocalDateTime): List<TripDTO> {
        println("GETTING TRIP FROM $stop TO $latitude|$longitude")

        // Arrive earlier than strictly necessary
        val originalTime = arrivalDateTime.time
        val newSeconds = (originalTime.toSecondOfDay() - ARRIVAL_SECONDS_BEFORE_COURSE_STARTS + 86400) % 86400
        val bufferedTime = LocalTime.fromSecondOfDay(newSeconds)

        // print("NEW ARRIVAL: $bufferedTime")

        val jsonResponse: String = client.get("$BASE_URL/trip") {
            parameter("accessId", rmvApiKey)
            parameter("originId", stop.id)
            parameter("numF", 1)
            parameter("numB", 3)
            parameter("destCoordLat", latitude)
            parameter("destCoordLong", longitude)
            parameter("date", arrivalDateTime.date.toString())
            parameter("time", bufferedTime.toString())
            parameter("searchForArrival", 1)
            parameter("withFreq", 0)
            parameter("trafficMessages", 0)
            parameter("tariff", 0)
            parameter("format", "json")
        }.bodyAsText()

        // println("RAW Response: $jsonResponse")

        return try {
            val tripResponse = json.decodeFromString<TripResponse>(jsonResponse)
            tripResponse.trips
        } catch (e: Exception) {
            println("Error deserializing Trip Response: ${e.message}")
            emptyList()
        }
    }

    suspend fun searchStopByName(input: String, numSearchResults: Int = DEFAULT_NUM_LOCATIONS): List<StopLocationDTO> {
        println("SEARCHING FOR LOCATION WITH SEARCH: $input")

        val jsonResponse: String = client.get("$BASE_URL/location.name") {
            parameter("accessId", rmvApiKey)
            parameter("format", "json")
            parameter("input", input)
            parameter("maxNo", numSearchResults)
            parameter("type", "S")
            parameter("withEquivalentLocations", 0)
            parameter("restrictSelection", "S")
            parameter("withProducts", 0)
            parameter("productRepresentatives", 1)
            parameter("r", 1000)
            parameter("filterMode", "DIST_PERI")
            parameter("withMastNames", 1)
        }.bodyAsText()

        println("RAW Response: $jsonResponse")

        return try {
            val locationResponse = json.decodeFromString<LocationResponse>(jsonResponse)

            println("RESPONSE DECODE: $locationResponse")


            locationResponse.stopsWrapper.map { it.stopLocation }
        } catch (e: Exception) {
            println("Error deserializing Location Response: ${e.message}")
            emptyList()
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