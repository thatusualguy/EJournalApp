package io.github.thatusualguy.ejournal.presentation.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.presentation.component.MultiComboBox
import io.github.thatusualguy.ejournal.presentation.component.SlideUpPanelLayout
import io.github.thatusualguy.ejournal.presentation.component.simpleVerticalScrollbar
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer
import io.github.thatusualguy.ejournal.presentation.layout.TeacherEntry

@Composable
fun SearchTeacherScreen(
    navController: NavHostController?,
) {
    val viewModel = viewModel(modelClass = SearchTeacherViewModel::class.java)
    val state = viewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            viewModel.userMessageShown()
        }
    }

    StudentScaffoldDrawer(
        title = "Поиск",
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState,
    ) {
        Surface(
            color = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
        ) {
            SlideUpPanelLayout(
                panelHeader = "Найдено ${state.teachers.size} преподавателей",
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 0.dp, horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val modifier = Modifier
//                            .fillMaxWidth()
                            .padding(top = 16.dp)

                        MultiComboBox(
                            labelText = "Предмет",
                            options = state.courses,
                            onOptionsChosen = { viewModel.onCoursesChange(it) },
                            selectedIds = state.selectedCourses,
                            modifier = modifier
                        )

                        MultiComboBox(
                            labelText = "Группа",
                            options = state.groups,
                            onOptionsChosen = { viewModel.onGroupsChange(it) },
                            selectedIds = state.selectedGroups,
                            modifier = modifier
                        )

                        OutlinedTextField(
                            value = state.searchPrompt,
                            onValueChange = { viewModel.onSearchPromptChange(it) },
                            label = {
                                Text(text = "Поиск")
                            },
                            trailingIcon = {
                                Icon(Icons.Rounded.Search, null)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                            modifier = modifier.padding(top = 8.dp)
                        )
                    }
                },
                panelContent = {
                    val lazyListState = rememberLazyListState()
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.simpleVerticalScrollbar(lazyListState),
                        content = {
                            items(state.teachers) {
                                TeacherEntry(item = it, navController = navController)
                            }
                        })
                }
            )
        }
    }
}

