package io.github.thatusualguy.ejournal.domain.models

data class StudentItem(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val middle_name: String,
    val group_id: Int,
    val group_name: String,
    val course: Int,
    val budget: Boolean,
    val specialty_id: Int,
    val specialty_name: String,
)

data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val group_id: Int,
    val group: String,
    val course: Int,
    val budget: Boolean,
    val specialty_id: Int,
    val specialty: String,

    val teachers: List<Pair<String, Int>>,
    val courses: List<Pair<String, Int>>,
)
