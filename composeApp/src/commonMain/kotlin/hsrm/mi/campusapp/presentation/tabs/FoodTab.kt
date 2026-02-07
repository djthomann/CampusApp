package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.food_tab_title
import campusapp.composeapp.generated.resources.no_menu_available
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.canteen.CanteenAPI
import hsrm.mi.campusapp.data.api.canteenapi.toDomain
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Dish
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.model.SideDishType
import hsrm.mi.campusapp.domain.persistence.DatabaseHolder
import hsrm.mi.campusapp.domain.persistence.DishEntity
import hsrm.mi.campusapp.domain.persistence.MenuEntity
import hsrm.mi.campusapp.domain.persistence.SideDishEntity
import hsrm.mi.campusapp.domain.service.CanteenService
import hsrm.mi.campusapp.presentation.components.CampusButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

class FoodScreenModel: ScreenModel {

    private val sideDishDao = DatabaseHolder.db.getSideDishDao()
    private val dishDao = DatabaseHolder.db.getDishDao()
    private val menuDao = DatabaseHolder.db.getMenuDao()

    var menus by mutableStateOf<List<Menu>>(emptyList())
        private set

    var selectedCanteen by mutableStateOf<Canteen?>(null)
    val canteens: StateFlow<List<Canteen>> = CanteenService.getAllCanteens().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalTime::class)
    suspend fun loadMenuFromAPI(canteen: Canteen): List<Menu> {
        val result = CanteenAPI.getMenusForWeek(canteen, LocalDate.now())
        return result.map { it.toDomain() }
    }

    fun saveMenus(menus: List<Menu>) {

        println("SAVING MENUS")

        screenModelScope.launch(Dispatchers.IO) {
            menus.forEach { menu ->
                val newMenu = MenuEntity(
                    canteen = menu.canteen,
                    date = menu.date.toString(),
                    dateString = menu.dateString
                )
                println("INSERTING MENU $newMenu")
                val menuId = menuDao.insertMenu(newMenu)
                menu.dishes.forEach { dish ->
                    val newDish = DishEntity(
                        name = dish.name,
                        menuId = menuId,
                        description = dish.description,
                        price = dish.price,
                        dishAllergens = dish.dishAllergens
                    )
                    dishDao.insert(newDish)
                }
                menu.sideDishes.forEach { entry ->
                    entry.value.forEach { sideDish ->
                        val newSideDish = SideDishEntity(
                            menuId = menuId,
                            type = entry.key,
                            name = sideDish
                        )
                        sideDishDao.insert(newSideDish)
                    }

                }
            }
        }
    }

    fun loadMenusWithDishes(canteen: Canteen) {
        screenModelScope.launch {
            menuDao.getMenusWithDishesAsFlow().collect { loadedMenus ->
                println("LOADED FROM DB:$loadedMenus")
                menus = loadedMenus.map { menuWithDishesEntity -> Menu(
                    canteen = menuWithDishesEntity.menu.canteen,
                    date = LocalDate.parse(menuWithDishesEntity.menu.date),
                    dateString = menuWithDishesEntity.menu.dateString,
                    dishes = menuWithDishesEntity.dishes.map { dishEntity ->
                        Dish(
                            name = dishEntity.name,
                            description = dishEntity.description,
                            price = dishEntity.price,
                            dishAllergens = dishEntity.dishAllergens
                        )
                    },
                    sideDishes = menuWithDishesEntity.sideDishes.groupBy { it.type }.mapValues { (_, entities) -> entities.map { it.name } }
                )
                }.filter { menu -> menu.canteen == canteen.name }

            }
        }
    }

    // TODO() Faulty logic probably
    fun loadMenu(canteen: Canteen) {

        selectedCanteen = canteen
        // loadMenuFromAPI(canteen)

        println("LOADING MENUS FOR: ${canteen.name}")

        screenModelScope.launch {
            // Lade aus DB
            loadMenusWithDishes(canteen)

            // Wenn nichts im DB, lade von API
            if (menus.isEmpty()) {
                println("Try API for loading menus")
                val loadedMenus = loadMenuFromAPI(canteen)  // suspend, wartet jetzt
                saveMenus(loadedMenus)                       // wartet ebenfalls
                menus = loadedMenus
            }
        }
    }

    fun clearMenus() {
        screenModelScope.launch {
            menuDao.deleteAllMenus()
        }
    }

    fun clearDishes() {
        screenModelScope.launch {
            dishDao.deleteAllDishes()
        }
    }

}     // TODO() CLEAN THIS UP!!!!

object FoodTab: CampusTab {

    private fun readResolve(): Any = FoodTab

    override val topAppBarTitle = runBlocking { getString(Res.string.food_tab_title) }
    override val activeIcon: ImageVector =  Icons.Filled.Dining
    override val inactiveIcon: ImageVector =  Icons.Outlined.Dining

    override val options: TabOptions
        @Composable
        get() {
            val title = topAppBarTitle
            val icon = rememberVectorPainter(activeIcon)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { FoodScreenModel() }

        val menus = screenModel.menus
        val expandedMenu = remember { mutableStateOf<Menu?>(null) }

        val selectedCanteen = screenModel.selectedCanteen
        val canteens by screenModel.canteens.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(canteens) { canteen ->

                    CampusButton(
                        text = canteen.name,
                        onClick = {
                            screenModel.loadMenu(canteen)
                        },
                        isActive = canteen == selectedCanteen
                    )
                }
            }
            /*Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CampusButton(
                    text = "Clear All",
                    onClick = {
                        screenModel.clearMenus()
                        screenModel.clearDishes()
                    }
                )
            }*/
            Spacer(
                modifier = Modifier.padding(5.dp)
            )
            if(menus.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(
                            Res.string.no_menu_available
                        )
                    )
                }
            } else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(menus) { MenuEntry(it, expanded = expandedMenu.value == it, onClick = {
                        if (expandedMenu.value != it) {
                            expandedMenu.value = it
                        } else {
                            expandedMenu.value = null
                        }

                    }) }
                }
            }
        }
    }
}

@Composable
fun MenuEntry(menu: Menu, expanded: Boolean, onClick: () -> Unit) {

    val backgroundColor by animateColorAsState(
        targetValue = if (expanded)
            MaterialTheme.colorScheme.secondary
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "backgroundColor"
    )

    val textColor = if (expanded)
        MaterialTheme.colorScheme.onSecondary
    else
        MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = if (expanded) 6.dp else 0.dp),
            ) {
                Text(menu.dateString, color = textColor)
            }
            AnimatedVisibility(
                visible = expanded
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Gerichte", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                    menu.dishes.sortedBy { dish -> dish.price }.forEachIndexed { index, dish ->
                        DishEntry(dish, index % 2 == 0)
                        HorizontalDivider(thickness = 1.dp)
                    }
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val isWide = maxWidth > 600.dp

                        if (isWide) {
                            SideDishesRow(menu.sideDishes)
                        } else {
                            SideDishesColumn(menu.sideDishes)
                        }
                    }

                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(if(!expanded) textColor else Color.Transparent).height(4.dp)
        )
    }
}

@Composable
fun SideDishesColumn(sideDishes: Map<SideDishType, List<String>>) {

    val textColor = MaterialTheme.colorScheme.onSecondary

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Column {
            Text("Beilagen", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
            sideDishes[SideDishType.GARNISH]?.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
            }
        }
        Column {
            Text("Salate", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
            sideDishes[SideDishType.SALAD]?.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
            }
        }
        Column {
            Text("Dessert", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
            sideDishes[SideDishType.DESSERT]?.forEach {
                Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
            }
        }
    }

}

@Composable
fun SideDishesRow(sideDishes: Map<SideDishType, List<String>>) {

    val textColor = MaterialTheme.colorScheme.onSecondary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column {
                Text("Beilagen", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                sideDishes[SideDishType.GARNISH]?.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column {
                Text("Salate", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                sideDishes[SideDishType.SALAD]?.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column {
                Text("Dessert", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
                sideDishes[SideDishType.DESSERT]?.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyMedium, color = textColor)
                }
            }
        }
    }
}

@Composable
fun DishEntry(dish: Dish, isEven: Boolean) {

    val textColor = MaterialTheme.colorScheme.onSecondary

    val backgroundColor = if(isEven) Color.Transparent else Color.DarkGray // TODO() Get rid of this

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.weight(1f), text = dish.name, style = MaterialTheme.typography.bodyMedium, color = textColor)
        Text(modifier = Modifier.wrapContentWidth(), text = dish.price, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = textColor)
    }
}