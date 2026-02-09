package hsrm.mi.campusapp.data.api.rmv

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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RmvApiMockTests {

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
                content = """{"stopLocationOrCoordLocation":[{"StopLocation":{"LocationNotes":{"LocationNote":[{"value":"","key":"WS","type":"A","txtN":""},{"value":"","key":"V0","type":"A","txtN":""},{"value":"","key":"IS","type":"A","txtN":""},{"value":"","key":"HT","type":"A","txtN":""},{"value":"","key":"HE","type":"A","txtN":""},{"value":"","key":"HB","type":"A","txtN":""},{"value":"de:06412:1020","key":"IF","type":"I","txtN":"de:06412:1020"},{"value":"06412000","key":"GK","type":"I","txtN":"06412000"}]},"altId":["de:06412:1020"],"timezoneOffset":60,"id":"A=1@O=F Zuckschwerdt-/Bauhofstraße@X=8551843@Y=50103500@U=80@L=3001020@p=1770403781@","extId":"3001020","name":"F Zuckschwerdt-/Bauhofstraße","lon":8.551879,"lat":50.103482,"weight":163,"products":96}},{"StopLocation":{"LocationNotes":{"LocationNote":[{"value":"","key":"WS","type":"A","txtN":""},{"value":"","key":"HT","type":"A","txtN":""},{"value":"","key":"HE","type":"A","txtN":""},{"value":"","key":"HB","type":"A","txtN":""},{"value":"de:07315:32060","key":"IF","type":"I","txtN":"de:07315:32060"},{"value":"07315000","key":"GK","type":"I","txtN":"07315000"}]},"altId":["de:07315:32060"],"timezoneOffset":60,"id":"A=1@O=MZ Bauhofstraße/Landesmuseum@X=8268826@Y=50004043@U=80@L=3025242@p=1770403781@","extId":"3025242","name":"MZ Bauhofstraße/Landesmuseum","lon":8.268826,"lat":50.004043,"weight":163,"products":64}},{"StopLocation":{"LocationNotes":{"LocationNote":[{"value":"","key":"WS","type":"A","txtN":""},{"value":"","key":"IS","type":"A","txtN":""},{"value":"","key":"HT","type":"A","txtN":""},{"value":"","key":"HE","type":"A","txtN":""},{"value":"","key":"HB","type":"A","txtN":""},{"value":"de:06435:13681","key":"IF","type":"I","txtN":"de:06435:13681"},{"value":"06435014","key":"GK","type":"I","txtN":"06435014"}]},"altId":["de:06435:13681"],"timezoneOffset":60,"id":"A=1@O=HU Neuhofstraße@X=8941507@Y=50136113@U=80@L=3013681@p=1770403781@","extId":"3013681","name":"HU Neuhofstraße","lon":8.941507,"lat":50.136113,"weight":171,"products":576}},{"StopLocation":{"LocationNotes":{"LocationNote":[{"value":"","key":"WS","type":"A","txtN":""},{"value":"","key":"IS","type":"A","txtN":""},{"value":"","key":"HT","type":"A","txtN":""},{"value":"","key":"HE","type":"A","txtN":""},{"value":"","key":"HB","type":"A","txtN":""},{"value":"de:06435:15647","key":"IF","type":"I","txtN":"de:06435:15647"},{"value":"06435014","key":"GK","type":"I","txtN":"06435014"}]},"altId":["de:06435:15647"],"timezoneOffset":60,"id":"A=1@O=HU-Großauheim Bahnhofstraße@X=8942451@Y=50106170@U=80@L=3015647@p=1770403781@","extId":"3015647","name":"HU-Großauheim Bahnhofstraße","lon":8.942451,"lat":50.10617,"weight":163,"products":68}}]}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        RmvAPI.client = HttpClient(mockEngine)
    }

    @AfterTest
    fun tearDownAfterTest() {
        RmvAPI.client.close()
    }

    @Test
    fun testSearchStopByName() = runBlocking {

        val searchString = "Bauhofstrasse"

        val expected = listOf(
            StopLocationDTO(
                id = "A=1@O=F Zuckschwerdt-/Bauhofstraße@X=8551843@Y=50103500@U=80@L=3001020@p=1770403781@",
                name = "F Zuckschwerdt-/Bauhofstraße",
                lon = 8.551879,
                lat = 50.103482
            ),
            StopLocationDTO(
                id = "A=1@O=MZ Bauhofstraße/Landesmuseum@X=8268826@Y=50004043@U=80@L=3025242@p=1770403781@",
                name = "MZ Bauhofstraße/Landesmuseum",
                lon = 8.268826,
                lat = 50.004043
            ),
            StopLocationDTO(
                id = "A=1@O=HU Neuhofstraße@X=8941507@Y=50136113@U=80@L=3013681@p=1770403781@",
                name = "HU Neuhofstraße",
                lon = 8.941507,
                lat = 50.136113
            ),
            StopLocationDTO(
                id = "A=1@O=HU-Großauheim Bahnhofstraße@X=8942451@Y=50106170@U=80@L=3015647@p=1770403781@",
                name = "HU-Großauheim Bahnhofstraße",
                lon = 8.942451,
                lat = 50.10617
            ),
        )

        val result = RmvAPI.searchStopByName(searchString)

        assertEquals(expected, result)

    }


}