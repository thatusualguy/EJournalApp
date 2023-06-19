package io.github.thatusualguy.ejournal.domain.models

data class Teacher(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val contactInfo: List<String>,

    val groups: List<Pair<String, Int>>,
    val courses: List<Pair<String, Int>>,
)

