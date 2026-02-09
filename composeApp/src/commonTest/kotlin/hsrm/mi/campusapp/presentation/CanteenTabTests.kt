package hsrm.mi.campusapp.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import hsrm.mi.campusapp.presentation.tabs.canteen.CanteenTab
import kotlin.test.Test

class CanteenTabTests {

    @Test
    @OptIn(ExperimentalTestApi::class)
    fun testSelectCanteen() = runComposeUiTest {

        setContent {
            CanteenTab.Content()
        }

    }

}