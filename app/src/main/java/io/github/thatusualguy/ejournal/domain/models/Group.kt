package io.github.thatusualguy.ejournal.domain.models

data class GroupShort(
    val id: Int,
    val admission_year: Int,
    val course: Int,
    val name: String,
    val specialty_id: Int,
    val specialty_name: String
)

data class Group(
    val id: Int,
    val admission_year: Int,
    val course: Int,
    val name: String,
    val specialty_id: Int,
    val specialty_name: String,

    val students: List<Pair<String, Int>>,
    val teachers: List<Pair<String, Int>>,
    val courses: List<Pair<String, Int>>
)