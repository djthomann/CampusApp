package hsrm.mi.campusapp.data.api.canteen

import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.LocalDate
import org.jsoup.Jsoup
import org.jsoup.select.Elements

object CanteenAPI {

    const val BASE_URL: String = "https://www.swffm.de/essen-trinken/speiseplaene"

    val client = HttpClient()

    /* TODO() Make this more beautiful */
    private suspend fun scrapeCanteenData(): List<Menu> {
        val htmlResponse = client.get("$BASE_URL/mensa-point").bodyAsText()

        println("RAW Response: $htmlResponse")

        val document = Jsoup.parse(htmlResponse)
        val menuDivs: Elements = document.getElementsByClass("speiseplan")

        val menus = mutableListOf<Menu>()

        menuDivs.forEach { div ->
            run {
                val dayString = div.select(".panel-heading strong").text()

                val dishRows = div.select(".panel-body table tbody tr")

                val dishes = mutableListOf<Dish>()

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
                            Dish(
                                name = dishName,
                                description = dishFurtherInfo,
                                price = dishPrice,
                                dishAllergens = dishAllergens
                            )
                        )
                    }
                }

                val menu = Menu(dayString, dishes)
                menus.add(menu)
            }
        }

        return menus
    }

    suspend fun getMenusForWeek(day: LocalDate): List<Menu> {
        return scrapeCanteenData()
    }
}