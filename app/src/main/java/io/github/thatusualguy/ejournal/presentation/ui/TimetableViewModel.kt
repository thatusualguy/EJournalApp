package io.github.thatusualguy.ejournal.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.DayTimetableItem
import io.github.thatusualguy.ejournal.domain.models.LessonTimetableItem
import io.github.thatusualguy.ejournal.domain.models.WeekType
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.github.thatusualguy.ejournal.grpc.TimetableGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimetableViewState(
    val selected_week: WeekType = WeekType.Top,
    val timetable: List<DayTimetableItem> = emptyList(),
    val userMessage: String? = null,
    val header: String = ""
)

class TimetableViewModel : ViewModel() {
    private val _state = MutableStateFlow(TimetableViewState())
    val state: StateFlow<TimetableViewState>
        get() = _state

    private val timetables: MutableMap<WeekType, List<DayTimetableItem>> = mutableMapOf()

    init {
        getTimetableForMe()
        getCurrentWeek()
    }

    fun changeTimetable(week: WeekType) {
        val timetable = timetables.getOrDefault(week, emptyList())
        _state.update {
            it.copy(
                timetable = timetable,
                selected_week = week
            )
        }
    }

    private fun getTimetableForMe() {
        viewModelScope.launch {
            val stub = TimetableGrpcKt.TimetableCoroutineStub(ChannelBuilder.channel)
            try {
                val timetable = stub.getMyStudentTimetable(Empty.getDefaultInstance())
                setTimetable(timetable = timetable)

            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    private fun toDaysList(api_week: Ejournal.week_timetableOrBuilder?): List<DayTimetableItem> {
        val days: MutableList<DayTimetableItem> = mutableListOf()

        for (i in 1..6) {
            val apiDay = api_week?.daysList?.find { it.day == i }
            var lessons: List<LessonTimetableItem> = emptyList()
            if (apiDay != null && apiDay.lessonsOrBuilderList.isNotEmpty())
                lessons = apiDay.lessonsOrBuilderList.map {
                    LessonTimetableItem(
                        subject_name = it.subjectName,
                        group_name = it.groupName,
                        teacher_name = it.teacherName,
                        lesson_num = it.period,
                        room_num = it.room
                    )
                }

            val dayOfWeekName = when (i) {
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

            days.add(DayTimetableItem(i, dayOfWeekName, lessons))
        }
        return days.sortedBy { it.dayOfWeekIndex }
    }


    private fun getCurrentWeek() {
        viewModelScope.launch {
            val stub = TimetableGrpcKt.TimetableCoroutineStub(ChannelBuilder.channel)
            try {
                val currentWeek = stub.getCurrentWeek(Empty.getDefaultInstance())
                changeTimetable(WeekType.values()[currentWeek.typeValue])
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

    fun getTimetableForGroup(groupId: Int) {
        viewModelScope.launch {
            val stub = TimetableGrpcKt.TimetableCoroutineStub(ChannelBuilder.channel)
            val request = Ejournal.id_request.newBuilder().setId(groupId).build()
            try {
                val timetable = stub.getGroupTimetable(request = request)
                setTimetable(timetable)
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    private fun setTimetable(timetable: Ejournal.timetable_reply) {
        _state.update { it.copy(header = timetable.timetableName) }

        timetables.clear()
        timetables[WeekType.Top] =
            toDaysList(timetable.weeksOrBuilderList.find { it.type.number == WeekType.Top.ordinal })
        timetables[WeekType.Bottom] =
            toDaysList(timetable.weeksOrBuilderList.find { it.type.number == WeekType.Bottom.ordinal })
        changeTimetable(_state.value.selected_week)
    }

    fun getTimetableForTeacher(teacherId: Int) {
        viewModelScope.launch {
            val stub = TimetableGrpcKt.TimetableCoroutineStub(ChannelBuilder.channel)
            val request = Ejournal.id_request.newBuilder().setId(teacherId).build()
            try {
                val timetable = stub.getTeacherTimetable(request = request)
                setTimetable(timetable)
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }
}
