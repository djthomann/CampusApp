package hsrm.mi.campusapp

import hsrm.mi.campusapp.data.persistence.AppDatabase
import hsrm.mi.campusapp.domain.service.BuildingService
import hsrm.mi.campusapp.domain.service.CampusService
import hsrm.mi.campusapp.domain.service.CanteenService
import hsrm.mi.campusapp.domain.service.CourseService
import hsrm.mi.campusapp.domain.service.ExamService
import hsrm.mi.campusapp.domain.service.IBuildingService
import hsrm.mi.campusapp.domain.service.ICampusService
import hsrm.mi.campusapp.domain.service.ICanteenService
import hsrm.mi.campusapp.domain.service.ICourseService
import hsrm.mi.campusapp.domain.service.IExamService
import hsrm.mi.campusapp.domain.service.IMenuService
import hsrm.mi.campusapp.domain.service.IStopService
import hsrm.mi.campusapp.domain.service.MenuService
import hsrm.mi.campusapp.domain.service.StopService
import hsrm.mi.campusapp.presentation.state.AppState
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val networkModule = module {
    single { HttpClient() }
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
}

val appStateModule = module {
    single { AppSettings() }
    single { AppState(get(), get(), get(), get(), get()) }
}

val serviceModule = module {
    single {get<AppDatabase>().getBuildingDao() } // BuildingDao
    single<IBuildingService> { BuildingService(get()) } // BuildingService

    single { get<AppDatabase>().getCampusDao() } // CampusDao
    single<ICampusService> { CampusService(dao = get()) } // CampusService

    single { get<AppDatabase>().getCanteenDao() } // CanteenDao
    single<ICanteenService> { CanteenService(dao = get()) } // CanteenService

    single { get<AppDatabase>().getCourseDao() } // CourseDao
    single<ICourseService> { CourseService(dao = get()) } // CourseService

    single { get<AppDatabase>().getExamDao() } // ExamDao
    single<IExamService> { ExamService(dao = get()) } // ExamService

    single { get<AppDatabase>().getMenuDao() } // MenuDao
    single { get<AppDatabase>().getDishDao() } // DishDao
    single { get<AppDatabase>().getSideDishDao() } // SideDishDao
    single<IMenuService> { MenuService(get(), get(), get()) } // MenuService

    single { get<AppDatabase>().getStopDao() } // StopDao
    single<IStopService> { StopService(dao = get()) } // StopService
}

val appModules = listOf(
    serviceModule,
    appStateModule,
    networkModule
)