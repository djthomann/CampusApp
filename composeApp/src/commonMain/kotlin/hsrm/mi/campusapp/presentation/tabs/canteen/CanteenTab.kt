package hsrm.mi.campusapp.presentation.tabs.canteen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import campusapp.composeapp.generated.resources.Res
import campusapp.composeapp.generated.resources.food_tab_title
import campusapp.composeapp.generated.resources.no_menu_available
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.service.ICanteenService
import hsrm.mi.campusapp.domain.service.IMenuService
import hsrm.mi.campusapp.presentation.components.CampusButton
import hsrm.mi.campusapp.presentation.tabs.CampusTab
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime


object CanteenTab: CampusTab {

    private fun readResolve(): Any = CanteenTab

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

        val canteenService = koinInject<ICanteenService>()
        val menuService = koinInject<IMenuService>()
        val screenModel = rememberScreenModel { CanteenScreenModel(canteenService, menuService) }

        val menus by screenModel.menus.collectAsStateWithLifecycle()
        val expandedMenu = remember { mutableStateOf<Menu?>(null) }

        val selectedCanteen = screenModel.selectedCanteen
        val canteens by screenModel.canteens.collectAsStateWithLifecycle()

        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                canteens.forEach {
                    CampusButton(
                        text = it.name,
                        onClick = {
                            screenModel.selectedCanteen = it
                        },
                        isActive = it == selectedCanteen
                    )
                }
            }
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
                    items(menus) {
                        MenuEntry(it, expanded = expandedMenu.value == it,
                            onClick = {
                                if (expandedMenu.value != it) {
                                    expandedMenu.value = it
                                } else {
                                    expandedMenu.value = null
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}