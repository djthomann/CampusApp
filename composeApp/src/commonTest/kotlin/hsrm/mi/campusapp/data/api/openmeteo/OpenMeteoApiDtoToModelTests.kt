package hsrm.mi.campusapp.data.api.openmeteo

import hsrm.mi.campusapp.domain.model.Campus
import hsrm.mi.campusapp.domain.model.Weather
import org.maplibre.spatialk.geojson.Position
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenMeteoApiDtoToModelTests {

    @Test
    fun testWeatherDtoToModel() {

        val campus = Campus(
            name = "Test Campus",
            center = Position(1.0, 2.0),
            tilt = 10.0,
            jsonPath = "Test Path"
        )

        val expected = Weather(
            campus = campus,
            temperature = 10.0f,
            windSpeed = 5.0f,
            rain = 1.0f,
            cloudCover = 50,
            isDay = true
        )

        val dto = WeatherDTO(
            temperature = 10.0f,
            windSpeed = 5.0f,
            rain = 1.0f,
            cloudCover = 50,
            isDayNum = 0
        )

        val actual = dto.toDomain(campus)

        assertEquals(expected, actual)

    }

}