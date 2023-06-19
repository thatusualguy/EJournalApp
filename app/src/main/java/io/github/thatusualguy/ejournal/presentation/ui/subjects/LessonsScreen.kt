package io.github.thatusualguy.ejournal.presentation.ui.subjects

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.presentation.layout.LessonEntry
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer


@Composable
fun LessonsScreen(
    navController: NavHostController?,
    subjectId: Int
) {
    val lessonsViewModel = viewModel(modelClass = LessonsViewModel::class.java)
    val state = lessonsViewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        lessonsViewModel.changeSubjectId(subjectId)
    }


    StudentScaffoldDrawer(
        title = state.subjectName ?: "",
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState,
    ) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(state.lessons) {
                LessonEntry(item = it) { }
            }
        }
    }
}
