package hsrm.mi.campusapp.data.api.canteen

import hsrm.mi.campusapp.data.api.canteenapi.DishDTO
import hsrm.mi.campusapp.data.api.canteenapi.MenuDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object CanteenAPI {

    const val BASE_URL: String = "https://www.swffm.de/essen-trinken/speiseplaene"

    val client = HttpClient()

    /* TODO() Make this more beautiful */
    @OptIn(ExperimentalTime::class)
    private suspend fun scrapeCanteenData(): List<MenuDTO> {
        val htmlResponse = client.get("$BASE_URL/mensa-point").bodyAsText()

        println("RAW Response: $htmlResponse")

        val document = Jsoup.parse(htmlResponse)
        val menuDivs: Elements = document.getElementsByClass("speiseplan")

        val menus = mutableListOf<MenuDTO>()

        menuDivs.forEach { div ->
            run {
                val dayString = div.select(".panel-heading strong").text()

                val dishRows = div.select(".panel-body table tbody tr")

                val dishes = mutableListOf<DishDTO>()

                dishRows.forEach { dish ->
                    run {
                        val cells = dish.select("td")
                        val textCell = cells[0]

                        val divs = textCell.select("div")
                        val textDiv = divs[0]

                        val dishName = textDiv.select("strong").text()
                        val dishAllergens = textDiv.select("> span").text()

                        val pFurtherInfo = textDiv.select("p")
                        val dishFurtherInfo = pFurtherInfo.text()

                        val priceCell = cells[1]
                        val dishPrice = priceCell.select("p strong ").text()

                        dishes.add(
                            DishDTO(
                                name = dishName,
                                description = dishFurtherInfo,
                                price = dishPrice,
                                dishAllergens = dishAllergens
                            )
                        )
                    }
                }

                val menu = MenuDTO(dayString, Clock.System
                    .todayIn(TimeZone.currentSystemDefault())
                    .year, dishes)
                menus.add(menu)
            }
        }

        return menus
    }

    suspend fun getMenusForWeek(day: LocalDate): List<MenuDTO> {
        return scrapeCanteenData()
    }
}