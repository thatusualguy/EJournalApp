package io.github.thatusualguy.ejournal.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.DayTimetableItem
import io.github.thatusualguy.ejournal.domain.models.LessonTimetableItem
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.github.thatusualguy.ejournal.grpc.StatisticsGrpcKt
import io.github.thatusualguy.ejournal.grpc.TimetableGrpcKt
import io.github.thatusualguy.ejournal.presentation.ui.subjects.SubjectItem
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class HomeViewState(
    val timetableToday: DayTimetableItem? = null,
    val timetableTomorrow: DayTimetableItem? = null,
    val average: Float = 5f,
    val chartData: List<Float> = emptyList(),
    val subjects: List<SubjectItem> = emptyList(),
    val userMessage: String? = null,
    val groupAverage: Float = 5f
)

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state

    init {
        getBetsNWorstSubjects()
        getTimetable()
        getGpa()
        getGroupGpa()
    }

    private fun getGroupGpa() {
        viewModelScope.launch {
            val stub = StatisticsGrpcKt.StatisticsCoroutineStub(ChannelBuilder.channel)
            try {
                val result = stub.getMyGroupGpaBySubjects(Empty.getDefaultInstance())
                _state.update {
                    it.copy(
                        groupAverage = result.subjectsList.map { it.average }.average().toFloat()
                    )
                }

            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }

        }
    }

    private fun getBetsNWorstSubjects() {
        viewModelScope.launch {
            val stub = StatisticsGrpcKt.StatisticsCoroutineStub(ChannelBuilder.channel)
            try {
                val result = stub.getGpaBySubjects(Empty.getDefaultInstance())

                val subjects = result.subjectsOrBuilderList.map {
                    SubjectItem(
                        it.subject,
                        it.subjectId,
                        it.average.toDouble(),
                    )
                }.sortedByDescending { it.average }
                val bestSubjects = subjects.take(2)
                val worstSubjects = subjects.takeLast(2)
                val resultSubjects =
                    (bestSubjects + worstSubjects).sortedByDescending { it.average }


                _state.update {
                    it.copy(subjects = resultSubjects)
                }

            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }

        }
    }

    private fun getGpa() {
        viewModelScope.launch {
            val stub = StatisticsGrpcKt.StatisticsCoroutineStub(ChannelBuilder.channel)
            try {
                val result = stub.getGpa(Empty.getDefaultInstance())
                _state.update {
                    it.copy(average = result.average.toFloat())
                }

            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }

        }
    }

    private fun getDay(
        timetable: Ejournal.timetable_reply, day: Int, week: Ejournal.week_type
    ): DayTimetableItem {

        val lessons =
            timetable.weeksOrBuilderList.find { w -> w.type == week }?.daysList?.find { d -> d.day == day }?.lessonsOrBuilderList?.map {
                LessonTimetableItem(
                    subject_name = it.subjectName,
                    group_name = it.groupName,
                    teacher_name = it.teacherName,
                    lesson_num = it.period,
                    room_num = it.room
                )
            }.orEmpty()

        val dayName = when (day) {
            1 -> "Понедельник"
            2 -> "Вторник"
            3 -> "Среда"
            4 -> "Четверг"
            5 -> "Пятница"
            6 -> "Суббота"
            else -> {
                ""
            }
        }
        return DayTimetableItem(day, dayName, lessons)
    }

    private fun getTimetable() {
        viewModelScope.launch {
            val stub = TimetableGrpcKt.TimetableCoroutineStub(ChannelBuilder.channel)
            try {
                val currentWeek = stub.getCurrentWeek(Empty.getDefaultInstance())
                val timetable = stub.getMyStudentTimetable(Empty.getDefaultInstance())


                var todayDay = LocalDateTime.now().dayOfWeek.value
                var todayWeek = currentWeek.type
                if (todayDay > 6) {
                    todayDay = 1
                    todayWeek = Ejournal.week_type.forNumber((todayWeek.number + 1) % 2)
                }
                var tomorrowDay = todayDay + 1
                var tomorrowWeek = todayWeek
                if (tomorrowDay > 6) {
                    tomorrowDay = 1
                    tomorrowWeek = Ejournal.week_type.forNumber((todayWeek.number + 1) % 2)
                }

                val timetableToday = getDay(timetable, todayDay, todayWeek)
                val timetableTomorrow = getDay(timetable, tomorrowDay, tomorrowWeek)

                _state.update {
                    it.copy(
                        timetableToday = timetableToday,
                        timetableTomorrow = timetableTomorrow
                    )
                }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    fun userMessageShown() {
        _state.update {
            it.copy(userMessage = null)
        }
    }
}
