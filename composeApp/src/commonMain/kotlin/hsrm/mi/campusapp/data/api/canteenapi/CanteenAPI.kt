package hsrm.mi.campusapp.data.api.canteenapi

import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.SideDishType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import org.jsoup.select.Elements
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object CanteenAPI {

    const val BASE_URL: String = "https://www.swffm.de/essen-trinken/speiseplaene"

    val client = HttpClient() {
        install(UserAgent) {
            agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        }
    }

    /* TODO() Make this more beautiful */
    @OptIn(ExperimentalTime::class)
    private suspend fun scrapeCanteenData(canteen: Canteen): List<MenuDTO> {
        val html = client.get("$BASE_URL/${canteen.url}") {
            header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
            header("Accept-Language", "de,en-US;q=0.7,en;q=0.3")
        }

        val htmlResponse = html.bodyAsText()

        // Detect blocked IP
        // println("STATUS: ${html.status}RAW Response: $htmlResponse ")

        val document = Jsoup.parse(htmlResponse)
        val menuDivs: Elements = document.getElementsByClass("speiseplan")

        val menus = mutableListOf<MenuDTO>()

        menuDivs.forEach { div ->
            run {
                val menu = parseMenu(canteen, div)
                menus.add(menu)
            }
        }

        println(menus)

        return menus
    }

    suspend fun getMenusForWeek(canteen: Canteen, day: LocalDate): List<MenuDTO> {
        return scrapeCanteenData(canteen)
    }
}

@OptIn(ExperimentalTime::class)
fun parseMenu(canteen: Canteen, div: Element): MenuDTO {
    val dayString = div.select(".panel-heading strong").text()

    val dishRows = div.select(".panel-body table tbody tr")
    val sideDishButtons = div.select(".panel-body > div a[data-bs-title]")

    val dishes = mutableListOf<DishDTO>()
    val sideDishes = mutableMapOf<SideDishType, List<String>>()


    dishRows.forEach { dish ->

        run {
            dishes.add(parseDish(dish))
        }
    }

    sideDishButtons.forEach { button ->
        val sideDishDTO = parseSideDish(button)
        sideDishDTO?.let {
            sideDishes[sideDishDTO.type] = sideDishDTO.sideDishes
        }
    }

    return MenuDTO(
        canteen = canteen.name,
        date = dayString,
        year = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
        dishes = dishes,
        sideDishes = sideDishes
    )
}

fun parseDish(dish: Element): DishDTO {

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

    return DishDTO(
        name = dishName,
        description = dishFurtherInfo,
        price = dishPrice,
        dishAllergens = dishAllergens
    )

}

fun parseSideDish(button: Element): SideDishDTO?  {

    val sideDishes = mutableListOf<String>()

    val sideDishTypeString: String = button.ownText()

    val escapedHtml = button.attr("data-bs-title")
    val innerHtml = Parser.unescapeEntities(escapedHtml, true)

    val doc = Jsoup.parse(innerHtml)

    val dishes = doc.select("div.sidedish li span").map { it.text().trim() }

    val sideDishType = SideDishMapper.sideDishTypeFromHTML(sideDishTypeString)

    if(sideDishType != null) {
        sideDishes.addAll(dishes)
    }

    sideDishType?.let {
        return SideDishDTO(sideDishType, sideDishes)
    }

    return null

}