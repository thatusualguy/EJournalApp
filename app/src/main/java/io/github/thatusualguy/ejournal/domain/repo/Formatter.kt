package io.github.thatusualguy.ejournal.domain.repo

fun formatName(name: String): String {
    if (name.length > 1) return name
    if (name.length == 1) return "$name."
    return "-"
}