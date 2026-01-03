package hsrm.mi.campusapp.data.api.rmv

actual object ApiKeys {
    actual val RMV: String = System.getenv("RMV_API_KEY")
}