package hsrm.mi.campusapp.presentation.tabs

import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.navigator.tab.Tab

interface CampusTab: Tab {

    val topAppBarTitle: String?
    val activeIcon: ImageVector
    val inactiveIcon: ImageVector
}