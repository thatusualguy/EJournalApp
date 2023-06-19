package io.github.thatusualguy.ejournal.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val name: String, val route: String?, val icon: ImageVector, val level:Int = 1
)