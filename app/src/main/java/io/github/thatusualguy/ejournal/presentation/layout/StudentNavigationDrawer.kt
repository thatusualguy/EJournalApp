package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.R
import io.github.thatusualguy.ejournal.domain.models.NavigationItem
import io.github.thatusualguy.ejournal.presentation.component.ScaffoldDrawer
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute
import kotlinx.coroutines.CoroutineScope

@Composable
fun StudentScaffoldDrawer(
    title: String,
    scope: CoroutineScope,
    navController: NavHostController?,
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit
) {

    val navigationItems = listOf(
        NavigationItem(
            name = "Дашборд",
            route = NavRoute.Home.route,
            icon = Icons.Rounded.Home,
        ),
        NavigationItem(
            name = "Оценки",
            route = NavRoute.Subjects.route,
            icon = Icons.Rounded.Star,
        ),
        NavigationItem(
            name = "Расписание",
            route = NavRoute.Timetable.route,
            icon = Icons.Rounded.DateRange,
        ),
        NavigationItem(
            name = "Поиск",
            route = null,
            icon = Icons.Rounded.Search
        ),
        NavigationItem(
            name = "Поиск студента",
            route = NavRoute.SearchStudents.route,
            icon = ImageVector.vectorResource(R.drawable.person_search),
            level = 2
        ),
        NavigationItem(
            name = "Поиск группы",
            route = NavRoute.SearchGroups.route,
            icon = ImageVector.vectorResource(R.drawable.groups),
            level = 2
        ),
        NavigationItem(
            name = "Поиск преподавателя",
            route = NavRoute.SearchTeachers.route,
            icon = ImageVector.vectorResource(R.drawable.school),
            level = 2
        ),
        NavigationItem(
            name = "Профиль",
            route = NavRoute.Profile.route,
            icon = ImageVector.vectorResource(R.drawable.account_circle),
        ),
        NavigationItem(
            name = "Настройки",
            route = NavRoute.Settings.route,
            icon = Icons.Rounded.Settings,
        ),
    )

    ScaffoldDrawer(
        title = title,
        navigationItems = navigationItems,
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState
    ) {
        content()
    }
}