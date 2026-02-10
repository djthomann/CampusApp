package hsrm.mi.campusapp.domain.service

import hsrm.mi.campusapp.domain.model.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek

interface ICourseService {

    fun getAllCourses(): Flow<List<Course>>
    fun getCoursesForDayOfWeek(dayOfWeek: DayOfWeek): Flow<List<Course>>

}