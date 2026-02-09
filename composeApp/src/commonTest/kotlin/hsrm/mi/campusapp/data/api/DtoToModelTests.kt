package hsrm.mi.campusapp.data.api

import hsrm.mi.campusapp.data.api.rmv.DepartureDTO
import hsrm.mi.campusapp.data.api.rmv.JourneyDetailRef
import hsrm.mi.campusapp.data.api.rmv.VehicleDTO
import hsrm.mi.campusapp.data.api.rmv.toDomain
import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Vehicle
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals

class DtoToModelTests {

    @Test
    fun testDepartureDtoToModel() {

        val expected = Departure(
            journeyDetailRef = "Test Ref",
            name = "Departure 1",
            time = LocalTime(11, 0),
            direction = "Bus 1",
            vehicle = Vehicle.BUS,
            journey = null
        )

        val dto = DepartureDTO(
            journeyDetailRef = JourneyDetailRef(
                ref = "Test Ref"
            ),
            prodcutAtStop = VehicleDTO(
                catOut = "Bus"
            ),
            name = "Departure 1",
            time = LocalTime(11, 0),
            direction = "Bus 1",
            journey = null
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

}