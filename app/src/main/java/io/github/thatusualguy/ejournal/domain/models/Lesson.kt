package io.github.thatusualguy.ejournal.domain.models

import java.util.Date

data class LessonItem(
    val date: Date, val mark: String?, val skipped: Boolean, val theme: String, val note: String
)