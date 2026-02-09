package hsrm.mi.campusapp.presentation.tabs.canteen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kizitonwose.calendar.core.now
import hsrm.mi.campusapp.data.api.canteen.CanteenAPI
import hsrm.mi.campusapp.data.api.canteen.toDomain
import hsrm.mi.campusapp.domain.model.Canteen
import hsrm.mi.campusapp.domain.model.Menu
import hsrm.mi.campusapp.domain.service.CanteenService
import hsrm.mi.campusapp.domain.service.MenuService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

class CanteenScreenModel: ScreenModel {

    var selectedCanteen by mutableStateOf<Canteen?>(null)
    val canteens: StateFlow<List<Canteen>> = CanteenService.getAllCanteens().stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
    val menus: StateFlow<List<Menu>> = snapshotFlow { selectedCanteen }
        .flatMapLatest { canteen ->
            if (canteen == null) return@flatMapLatest flowOf(emptyList())
            MenuService.getMenusForCanteenAsFlow(canteen).map { list ->


                if (list.isEmpty()) {
                    screenModelScope.launch {
                        try {
                            val apiData = CanteenAPI.getMenusForWeek(canteen, LocalDate.now())
                            MenuService.saveMenus(apiData.map { it.toDomain() })
                        } catch (e: Exception) { /* Log error */ }
                    }
                }
                list
            }
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

}