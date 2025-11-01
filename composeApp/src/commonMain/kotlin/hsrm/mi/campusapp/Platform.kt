package hsrm.mi.campusapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform