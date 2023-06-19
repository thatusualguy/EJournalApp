package io.github.thatusualguy.ejournal.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.domain.models.DayTimetableItem
import io.github.thatusualguy.ejournal.domain.models.LessonTimetableItem
import io.github.thatusualguy.ejournal.domain.models.WeekType
import io.github.thatusualguy.ejournal.presentation.component.SelectedButton
import io.github.thatusualguy.ejournal.presentation.layout.DayTimetableEntry
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer

@Composable
fun TimetableScreen(
    navController: NavHostController?,
    groupId: Int? = null,
    teacherId: Int? = null
) {
    val timetableViewModel = viewModel(modelClass = TimetableViewModel::class.java)
    val state = timetableViewModel.state.collectAsState().value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            timetableViewModel.userMessageShown()
        }
    }

    LaunchedEffect(Unit) {
        if (groupId != null && groupId != -1)
            timetableViewModel.getTimetableForGroup(groupId)
        if (teacherId != null && teacherId != -1)
            timetableViewModel.getTimetableForTeacher(teacherId)
    }

    StudentScaffoldDrawer(
        title = state.header,
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                SelectedButton(
                    text = "Числитель",
                    selected = state.selected_week == WeekType.Top,
                    onClick = {
                        timetableViewModel.changeTimetable(WeekType.Top)
                    })

                SelectedButton(
                    text = "Знаменатель",
                    selected = state.selected_week == WeekType.Bottom,
                    onClick = {
                        timetableViewModel.changeTimetable(WeekType.Bottom)
                    })
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
            ) {
                repeat(3) {
                    val day = (it * 2) + 1
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Box(modifier = Modifier.weight(1f)) {
                            DayTimetableEntry(
                                item = state.timetable.find { item -> item.dayOfWeekIndex == day },
                                day = day
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            DayTimetableEntry(
                                item = state.timetable.find { item -> item.dayOfWeekIndex == (day + 1) },
                                day = day + 1
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}