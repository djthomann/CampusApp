package hsrm.mi.campusapp.data.api.canteen

import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlin.time.ExperimentalTime

data class MenuDTO (
    val canteen: String,
    val date: String,
    val year: Int,
    val dishes: List<DishDTO>,
    val sideDishes: Map<SideDishType, List<String>>
)

fun MenuDTO.toDomain(): Menu {
    return Menu(
        canteen = canteen,
        date = dateStringToLocalDate(date, year),
        dateString = date,
        dishes = dishes.map { it.toDomain() },
        sideDishes = sideDishes
    )
}

@OptIn(ExperimentalTime::class)
/*
    Converts String of Format: Day, dd.Month
 */
fun dateStringToLocalDate(input: String, year: Int): LocalDate {
    return try {

        val parts = input.split(" ")

        val day = parts[1].replace(".", "").toInt()

        val monthName = parts[2].lowercase()

        val month = when {
            monthName.startsWith("jan") -> Month.JANUARY
            monthName.startsWith("feb") -> Month.FEBRUARY
            monthName.startsWith("mär") -> Month.MARCH
            monthName.startsWith("apr") -> Month.APRIL
            monthName.startsWith("mai") -> Month.MAY
            monthName.startsWith("jun") -> Month.JUNE
            monthName.startsWith("jul") -> Month.JULY
            monthName.startsWith("aug") -> Month.AUGUST
            monthName.startsWith("sep") -> Month.SEPTEMBER
            monthName.startsWith("okt") -> Month.OCTOBER
            monthName.startsWith("nov") -> Month.NOVEMBER
            monthName.startsWith("dez") -> Month.DECEMBER
            else -> Month.JANUARY // Fallback
        }

        LocalDate(year, month, day)
    } catch (e: Exception) {
        val s = e.toString()
        LocalDate(2000, Month.JANUARY, 1) // Fallback
    }
}