package hsrm.mi.campusapp.data.api.canteen

import hsrm.mi.campusapp.data.api.canteenapi.DishDTO
import hsrm.mi.campusapp.data.api.canteenapi.MenuDTO
import hsrm.mi.campusapp.data.api.canteenapi.SideDishMapper
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.SideDishType
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object CanteenAPI {

    const val BASE_URL: String = "https://www.swffm.de/essen-trinken/speiseplaene"

    val client = HttpClient()

    /* TODO() Make this more beautiful */
    @OptIn(ExperimentalTime::class)
    private suspend fun scrapeCanteenData(canteen: Canteen): List<MenuDTO> {
        val htmlResponse = client.get("$BASE_URL/${canteen.url}").bodyAsText()

        // println("RAW Response: $htmlResponse")

        val document = Jsoup.parse(htmlResponse)
        val menuDivs: Elements = document.getElementsByClass("speiseplan")

        val menus = mutableListOf<MenuDTO>()

        menuDivs.forEach { div ->
            run {
                val dayString = div.select(".panel-heading strong").text()

                val dishRows = div.select(".panel-body table tbody tr")

                val sideDishDivs = div.select(".panel-body > div")

                val dishes = mutableListOf<DishDTO>()
                val sideDishes = mutableMapOf<SideDishType, List<String>>()

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

                sideDishDivs.forEach { div ->

                    val sideDishButtons = div.select("a[data-bs-title]")

                    sideDishButtons.forEach { button ->

                        val sideDishTypeString: String = button.ownText()

                        val escapedHtml = button.attr("data-bs-title")
                        val innerHtml = Parser.unescapeEntities(escapedHtml, true)

                        val doc = Jsoup.parse(innerHtml)

                        val dishes = doc.select("div.sidedish li span").map { it.text().trim() }

                        val sideDishType = SideDishMapper.sideDishTypeFromHTML(sideDishTypeString)

                        sideDishType?.let {
                            sideDishes[sideDishType] = dishes
                        }

                    }

                }

                val menu = MenuDTO(
                    canteen = canteen.name,
                    date = dayString,
                    year = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
                    dishes = dishes,
                    sideDishes = sideDishes)
                menus.add(menu)
                println("MENU" + menu)
            }
        }

        return menus
    }

    suspend fun getMenusForWeek(canteen: Canteen, day: LocalDate): List<MenuDTO> {
        return scrapeCanteenData(canteen)
    }
}