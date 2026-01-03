package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.DepartureResponse
import hsrm.mi.campusapp.domain.model.Stop
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object RmvAPI {

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val baseURL = "localhost"
    val rmvApiKey = ApiKeys.RMV

    fun getArrivalBoard(stop: Stop): String {
        return rmvApiKey
    }

    suspend fun getNextArrivals(stop: Stop, numArrivals: Int = 2): List<Departure> {

        println("GETTING ARRIVALS FOR: ${stop.name}")

        val response: DepartureResponse = client.get("https://www.rmv.de/hapi/departureBoard") {
                parameter("accessId", rmvApiKey)
                parameter("id", stop.id)
                parameter("maxJourneys", numArrivals)
                parameter("format", "json")
        }.body()

        return response.departure
    }


}

expect object ApiKeys {
    val RMV: String
}