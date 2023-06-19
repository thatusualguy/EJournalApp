package io.github.thatusualguy.ejournal.domain.models

enum class WeekType {
    Bottom,
    Top
}

data class DayTimetableItem(
    val dayOfWeekIndex: Int,
    val dayOfWeekName: String,
    val lessons: List<LessonTimetableItem>,
)

data class LessonTimetableItem(
    val subject_name: String,
    val teacher_name: String,
    val group_name: String,
    val room_num: String,
    val lesson_num: Int
)