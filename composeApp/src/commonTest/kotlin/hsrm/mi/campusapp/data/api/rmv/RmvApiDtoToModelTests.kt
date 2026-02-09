package hsrm.mi.campusapp.data.api.rmv

import hsrm.mi.campusapp.domain.model.Departure
import hsrm.mi.campusapp.domain.model.Journey
import hsrm.mi.campusapp.domain.model.JourneyStop
import hsrm.mi.campusapp.domain.model.Leg
import hsrm.mi.campusapp.domain.model.Stop
import hsrm.mi.campusapp.domain.model.Trip
import hsrm.mi.campusapp.domain.model.Vehicle
import kotlinx.datetime.LocalTime
import org.maplibre.spatialk.geojson.Position
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RmvApiDtoToModelTests {

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

    @Test
    fun testVehicleDtoToModelKnownType() {

        val expected = Vehicle.BUS

        val dto1 = VehicleDTO(
            catOut = "Bus"
        )

        val dto2 = VehicleDTO(
            catOut = "unknown"
        )

        val actual1 = dto1.toDomain()
        val actual2 = dto2.toDomain()

        assertEquals(expected, actual1)
        assertNotEquals(expected, actual2)

    }

    @Test
    fun testVehicleDtoToModelUnknownType() {

        val expected = Vehicle.UNKNOWN

        val dto = VehicleDTO(
            catOut = "unknown"
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

    @Test
    fun testJourneyDtoToModel() {

        val expected = Journey(
            stops = listOf(
                JourneyStop("Stop 1", "ID 1"),
                JourneyStop("Stop 2", "ID 2")
            )
        )

        val dto = JourneyDTO(
            journeyStops = StopsWrapper(
                stops = listOf(
                    JourneyStopDTO("Stop 1", "ID 1"),
                    JourneyStopDTO("Stop 2", "ID 2")
                )
            )
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

    @Test
    fun testJourneyStopDtoToModel() {

        val expected = JourneyStop(
            name = "Test Journey Stop",
            id = "Test ID"
        )

        val dto = JourneyStopDTO(
            name = "Test Journey Stop",
            id = "Test ID"
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

    @Test
    fun testStopLocationDtoToModel() {

        val expected = Stop(
            id = "Test ID",
            name = "Test Stop",
            campus = null,
            position = Position(1.0, 20.0)
        )

        val dto = StopLocationDTO(
            id = "Test ID",
            name = "Test Stop",
            lon = 1.0,
            lat = 20.0
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

    @Test
    fun testTripDtoToModel() {

        val expected = Trip(
            startTime = LocalTime(9, 0),
            arrivalTime = LocalTime(11, 0),
            legs = listOf(
                Leg(
                    name = "Leg 1",
                    origin = "Origin 1",
                    startTime = LocalTime(9, 0),
                    destination = "Destination 1",
                    endTime = LocalTime(10, 15)
                ),
                Leg(
                    name = "Leg 2",
                    origin = "Origin 2",
                    startTime = LocalTime(10, 15),
                    destination = "Destination 2",
                    endTime = LocalTime(11, 0)
                )
            )
        )

        val dto = TripDTO(
            origin = OriginDTO(
                name = "Trip Origin",
                time = LocalTime(9, 0)
            ),
            destination = DestinationDTO(
                name = "Trip Destination",
                time = LocalTime(11, 0)
            ),
            legList = LegListDTO(
                legs = listOf(
                    LegDTO(
                        name = "Leg 1",
                        origin = OriginDTO("Origin 1", LocalTime(9, 0)),
                        destination = DestinationDTO("Destination 1", LocalTime(10, 15))
                    ),
                    LegDTO(
                        name = "Leg 2",
                        origin = OriginDTO("Origin 2", LocalTime(10, 15)),
                        destination = DestinationDTO("Destination 2", LocalTime(11, 0 ))
                    )
                )
            )
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

    @Test
    fun testLegDtoToModel() {

        val expected = Leg(
            name = "Leg 1",
            origin = "Origin 1",
            startTime = LocalTime(10, 0),
            destination = "Destination 1",
            endTime = LocalTime(11, 15)
        )

        val dto = LegDTO(
            name = "Leg 1",
            origin = OriginDTO(name = "Origin 1", time = LocalTime(10, 0)),
            destination = DestinationDTO(name = "Destination 1", time = LocalTime( 11, 15))
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

}