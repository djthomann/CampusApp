package hsrm.mi.campusapp.data.api

import hsrm.mi.campusapp.data.api.canteen.CanteenAPI
import hsrm.mi.campusapp.data.api.openmeteo.OpenMeteoAPI
import hsrm.mi.campusapp.data.api.rmv.RmvAPI
import io.ktor.client.HttpClient

object ApiModule {

    fun init(client: HttpClient) {
        OpenMeteoAPI.init(client)
        RmvAPI.init(client)
        CanteenAPI.init(client)
    }

}