package io.github.thatusualguy.ejournal.presentation.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.domain.models.Student
import io.github.thatusualguy.ejournal.domain.repo.formatName
import io.github.thatusualguy.ejournal.presentation.layout.ExpandableSimpleLazyColumn
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer
import io.github.thatusualguy.ejournal.presentation.navigation.NavArg
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute

@Composable
fun StudentProfileScreen(navController: NavHostController, studentId: Int?) {
    val viewModel: StudentProfileViewModel =
        viewModel(factory = StudentProfileViewModel.Companion.Factory(studentId))
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
//        title = if (state.student != null) state.student.lastName + " " + formatName(state.student.firstName) + " " + formatName(
//            state.student.middleName
//        ) else "Загрузка профиля",
        title = "Студент",
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState,
    ) {
        if (state.isLoading || state.student == null) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.6f))
            return@StudentScaffoldDrawer
        }

        val student: Student = state.student
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .heightIn(max = 60000.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(max = 60000.dp)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = student.lastName + " " + formatName(student.firstName) + " " + formatName(
                        student.middleName
                    ),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 8.dp)
                )


                Row {
                    Column(
                        modifier = Modifier.padding(end = 4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val labels = listOf(
                            "Курс",
                            "Группа",
                            "Спец.",
                            "Бюджет",
                            "",
                        )
                        labels.forEach {
                            Text(
                                text = it,
                                modifier = Modifier.alpha(ContentAlpha.medium),
                            )
                        }

                    }

                    Column(modifier = Modifier.weight(1f)) {
                        val labels = listOf(
                            student.course,
                            student.group,
                            if (student.budget) "да" else "нет",
                            student.specialty,
                        )
                        labels.forEach {
                            Text(
                                text = "$it",
                                modifier = Modifier.alpha(ContentAlpha.high),
                            )
                        }
                    }
                }


                Text(
                    text = "Расписание",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextButton(onClick = {
                    navController.navigate(
                        NavRoute.Timetable.route + "?${NavArg.GroupId.name}={${NavArg.GroupId.name}}?${NavArg.TeacherId.name}=-1}"
                            .replace(
                                oldValue = "{${NavArg.GroupId.name}}",
                                newValue = student.group_id.toString()
                            )
                    )
                }) {
                    Text(
                        text = "перейти к расписанию",
                        modifier = Modifier.alpha(ContentAlpha.high),
                    )
                }

                Text(
                    text = "Группа",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextButton(onClick = {
                    navController.navigate(
                        NavRoute.Group.route + "/${NavArg.GroupId.name}={${NavArg.GroupId.name}}"
                            .replace(
                                oldValue = "{${NavArg.GroupId.name}}",
                                newValue = student.group_id.toString()
                            )
                    )
                }) {
                    Text(
                        text = "перейти к группе",
                        modifier = Modifier.alpha(ContentAlpha.high),
                    )
                }

                Divider(modifier = Modifier.fillMaxWidth())
                Box(
                    modifier = Modifier
                        .heightIn(max = 60000.dp)
                ) {
                    ExpandableSimpleLazyColumn(
                        label = "Преподаватели",
                        values = student.teachers,
                        onClick = {
                            navController.navigate(
                                route = NavRoute.Teacher.route + "/${NavArg.TeacherId.name}=val1"
                                    .replace(
                                        oldValue = "val1",
                                        newValue = it.second.toString()
                                    )
                            )
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .heightIn(max = 60000.dp)
                ) {
                ExpandableSimpleLazyColumn(label = "Предметы", values = student.courses)
            }}
        }
    }
}




@Preview
@Composable
fun ItemList(count: Int = 100) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        repeat(count) { counter ->
            Text("Item $counter", Modifier.padding(10.dp))
        }
    }
}