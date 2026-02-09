package hsrm.mi.campusapp.data.api.openmeteo

import hsrm.mi.campusapp.domain.model.Campus
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.maplibre.spatialk.geojson.Position
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenMeteoApiMockTests {

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @BeforeTest
    fun setup() {

        val mockEngine = MockEngine { request ->
            respond(
                content = """{"latitude":50.0,"longitude":7.9999995,"generationtime_ms":0.10883808135986328,"utc_offset_seconds":0,"timezone":"GMT","timezone_abbreviation":"GMT","elevation":109.0,"current_units":{"time":"iso8601","interval":"seconds","temperature_2m":"°C","is_day":"","rain":"mm","cloud_cover":"%","wind_speed_10m":"km/h"},"current":{"time":"2026-02-09T18:00","interval":900,"temperature_2m":4.2,"is_day":0,"rain":0.00,"cloud_cover":98,"wind_speed_10m":3.6}}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        OpenMeteoAPI.client = HttpClient(mockEngine)
    }

    @AfterTest
    fun tearDownAfterTest() {
        OpenMeteoAPI.client.close()
    }

    @Test
    fun testGetCurrentWeather() = runBlocking {
        val campus = Campus(
            name = "Test Campus",
            center = Position(8.0, 50.0),
            tilt = 20.0,
            jsonPath = "Test Path"
        )
        
        val expected = WeatherDTO(
            temperature = 4.2f,
            windSpeed = 3.6f,
            rain = 0.0f,
            cloudCover = 98,
            isDayNum = 0
        )

        val result = OpenMeteoAPI.getCurrentWeather(campus)
        
        assertEquals(expected, result)

    }

}