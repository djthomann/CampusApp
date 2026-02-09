package hsrm.mi.campusapp.data.api.canteen

import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class CanteenApiDtoToModelTests {

    @Test
    fun testMenuDtoToModel() {

        val expected = Menu(
            canteen = "Test Canteen",
            date = LocalDate(2026, 1, 1),
            dateString = "Montag, 01. Januar",
            dishes = listOf(
                Dish(
                    name = "Dish 1",
                    description = "Description 1",
                    price = "10.00 €",
                    dishAllergens = "No Allergens"
                ),
                Dish(
                    name = "Dish 2",
                    description = "Description 2",
                    price = "5.00 €",
                    dishAllergens = "Allergens 2"
                )
            ),
            sideDishes = mapOf(
                SideDishType.SALAD to listOf("Salad 1", "Salad 2", "Salad 3"),
                SideDishType.GARNISH to listOf("Garnish 1", "Garnish 2", "Garnish 3"),
                SideDishType.DESSERT to listOf("DESSERT 1", "DESSERT 2", "DESSERT 3")
            )
        )
        
        val dto = MenuDTO(
            canteen = "Test Canteen",
            date = "Montag, 01. Januar",
            year = 2026,
            dishes = listOf(
                DishDTO(
                    name = "Dish 1",
                    description = "Description 1",
                    price = "10.00 €",
                    dishAllergens = "No Allergens"
                ),
                DishDTO(
                    name = "Dish 2",
                    description = "Description 2",
                    price = "5.00 €",
                    dishAllergens = "Allergens 2"
                )
            ),
            sideDishes = mapOf(
                SideDishType.SALAD to listOf("Salad 1", "Salad 2", "Salad 3"),
                SideDishType.GARNISH to listOf("Garnish 1", "Garnish 2", "Garnish 3"),
                SideDishType.DESSERT to listOf("DESSERT 1", "DESSERT 2", "DESSERT 3")
            )
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

    @Test
    fun testDishDtoToModel() {

        val expected = Dish(
            name = "Test Dish",
            description = "Test Description",
            price = "1.00 €",
            dishAllergens = "Test Allergens"
        )

        val dto = DishDTO(
            name = "Test Dish",
            description = "Test Description",
            price = "1.00 €",
            dishAllergens = "Test Allergens"
        )

        val actual = dto.toDomain()

        assertEquals(expected, actual)

    }

    @Test
    fun testDateStringToLocalDate() {

        // These dates should probably be picked differently
        val dates = mapOf(
            "Freitag, 01. Januar" to LocalDate(2016, 1, 1),
            "Dienstag, 02. Februar" to LocalDate(2016, 2, 2),
            "Donnerstag, 03. März" to LocalDate(2016, 3, 3),
            "Montag, 04. April" to LocalDate(2016, 4, 4),
            "Donnerstag, 05. Mai" to LocalDate(2016, 5, 5),
            "Montag, 06. Juni" to LocalDate(2016, 6, 6),
            "Donnerstag, 07. Juli" to LocalDate(2016, 7, 7),
            "Montag, 08. August" to LocalDate(2016, 8, 8),
            "Freitag, 09. September" to LocalDate(2016, 9, 9),
            "Montag, 10. Oktober" to LocalDate(2016, 10, 10),
            "Freitag, 11. November" to LocalDate(2016, 11, 11),
            "Montag, 12. Dezember" to LocalDate(2016, 12, 12),
        )

       dates.forEach { (dateString, date) ->

           val expected = date

           val actual = dateStringToLocalDate(dateString, date.year)

           assertEquals(expected, actual)

       }


    }

}