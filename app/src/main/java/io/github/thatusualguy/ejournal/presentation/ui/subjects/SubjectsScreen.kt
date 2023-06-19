package io.github.thatusualguy.ejournal.presentation.ui.subjects

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer
import io.github.thatusualguy.ejournal.presentation.layout.SubjectEntry
import io.github.thatusualguy.ejournal.presentation.navigation.NavArg
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute

@Composable
fun SubjectsScreen(navController: NavHostController) {
    val subjectsViewModel = viewModel(modelClass = SubjectsViewModel::class.java)
    val state = subjectsViewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val lazyColumnState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            subjectsViewModel.userMessageShown()
        }
    }

    StudentScaffoldDrawer(
        title = "Успеваемость",
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState,
    ) {
        LazyColumn(modifier = Modifier.fillMaxHeight(), state = lazyColumnState) {
            items(state.subjects) {
                SubjectEntry(item = it) {
                    navController.navigate(
                        NavRoute.SubjectMarks.route + "/${NavArg.SubjectId.name}={${NavArg.SubjectId.name}}".replace(
                            oldValue = "{${NavArg.SubjectId.name}}", newValue = it.id.toString()
                        )
                    )
                }
            }
        }
    }
}