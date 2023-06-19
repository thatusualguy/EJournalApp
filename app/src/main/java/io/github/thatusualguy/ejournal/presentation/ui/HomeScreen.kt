package io.github.thatusualguy.ejournal.presentation.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.presentation.BackPress
import io.github.thatusualguy.ejournal.presentation.component.CircularMarkIndicator
import io.github.thatusualguy.ejournal.presentation.layout.DayTimetableEntry
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer
import io.github.thatusualguy.ejournal.presentation.layout.SubjectEntry
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute
import kotlinx.coroutines.delay
import kotlin.math.round


@Composable
fun HomeScreen(navController: NavHostController?) {
    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
    val state = homeViewModel.state.collectAsState().value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()


    var showToast by remember { mutableStateOf(false) }
    var backPressState by remember { mutableStateOf<BackPress>(BackPress.Idle) }
    val context = LocalContext.current

    if (showToast) {
        Toast.makeText(context, "Нажмите повторно для выхода", Toast.LENGTH_SHORT).show()
        showToast = false
    }

    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPress.InitialTouch) {
            delay(2000)
            backPressState = BackPress.Idle
        }
    }

    BackHandler(backPressState == BackPress.Idle) {
        backPressState = BackPress.InitialTouch
        showToast = true
    }

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            homeViewModel.userMessageShown()
        }
    }

    StudentScaffoldDrawer(
        title = "Дашборд",
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    state.timetableToday?.let {
                        DayTimetableEntry(
                            item = it
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    state.timetableTomorrow?.let {
                        DayTimetableEntry(
                            item = it
                        )
                    }
                }
            }
            CustomDivider()
            Text(
                text = "Успеваемость",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Card(shape = MaterialTheme.shapes.medium) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CircularMarkIndicator(mark = state.average, Modifier.height(65.dp))
                    Text(
                        text =
                        "Средний балл группы: \n" +
                                "Ваш средний балл:    ",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text =
                        "${round(state.groupAverage*100)/100}\n${round(state.average*100)/100}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            CustomDivider()
            Text(
                text = "Предметы",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            val navigateToSubjects = { navController?.navigate(NavRoute.Subjects.route) }
            LazyColumn(
                Modifier
                    .clickable { navigateToSubjects() }
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {


                items(state.subjects.take(2)) {
                    SubjectEntry(item = it, onClick = { navigateToSubjects() })
                }

                item {
                    Text(
                        text = "···",
                        modifier = Modifier
                            .height(30.dp)
                            .offset(y = -8.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        letterSpacing = 6.sp
                    )
                }

                items(state.subjects.takeLast(2)) {
                    SubjectEntry(item = it, onClick = { navigateToSubjects() })
                }
            }
        }
    }
}

@Composable
private fun CustomDivider() {
    Divider(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
}

@Composable
@Preview(showBackground = true)
fun StudentHomeScreenPreview() {
    HomeScreen(null)
}