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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.domain.models.Teacher
import io.github.thatusualguy.ejournal.presentation.component.phone
import io.github.thatusualguy.ejournal.presentation.component.email
import io.github.thatusualguy.ejournal.domain.repo.formatName
import io.github.thatusualguy.ejournal.presentation.component.website
import io.github.thatusualguy.ejournal.presentation.layout.ExpandableSimpleLazyColumn
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer
import io.github.thatusualguy.ejournal.presentation.navigation.NavArg
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute


@Composable
fun TeacherProfileScreen(navController: NavHostController, teacherId: Int?) {
    val viewModel: TeacherProfileViewModel =
        viewModel(factory = TeacherProfileViewModel.Companion.Factory(teacherId!!))
    val state = viewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            viewModel.userMessageShown()
        }
    }

    StudentScaffoldDrawer(
//        title = if (state.teacher != null) state.teacher.lastName + " " + formatName(state.teacher.firstName) + " " + formatName(
//            state.teacher.middleName
//        ) else "Загрузка профиля",
        title = "Преподаватель",
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState,
    ) {

        if (state.isLoading || state.teacher == null) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.6f))
            return@StudentScaffoldDrawer
        }

        val teacher: Teacher = state.teacher
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
                    text = teacher.lastName + " " + formatName(teacher.firstName) + " " + formatName(
                        teacher.middleName
                    ),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Способы связи",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (teacher.contactInfo.isEmpty()) Text(
                    text = "не указано",
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    style = MaterialTheme.typography.body2,
                )
                else teacher.contactInfo.forEach {
                    val contactValue = it.substringAfter(":").trim()
                    val contactType = it.let {
                        var s = ""
                        if (it.contains("tel:")) s += "Телефон"
                        if (it.contains("email:")) s += "Email"
                        if (it.contains("vk:")) s += "ВКонтакте"
                        s
                    }


                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = contactType,
                            modifier = Modifier
                                .alpha(ContentAlpha.medium)
                                .weight(0.3f),
                        )

                        TextButton(onClick = {
                            when (contactType) {
                                "Телефон" -> phone(contactValue, ctx)
                                "Email" -> email(contactValue, ctx)
                                "ВКонтакте" -> website(contactValue, ctx)
                            }
                        }) {
                            Text(
                                text = contactValue, maxLines = 1, overflow = TextOverflow.Ellipsis
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
                        NavRoute.Timetable.route + "?${NavArg.GroupId.name}=-1?${NavArg.TeacherId.name}={${NavArg.TeacherId.name}}".replace(
                            oldValue = "{${NavArg.TeacherId.name}}",
                            newValue = teacher.id.toString()
                        )
                    )
                }) {
                    Text(
                        text = "перейти к расписанию",
                        modifier = Modifier.alpha(ContentAlpha.high),
                    )
                }
                Divider(modifier = Modifier.fillMaxWidth())
                Box(
                    modifier = Modifier
                        .heightIn(max = 60000.dp)
                ) {
                    ExpandableSimpleLazyColumn(label = "Группы", values = teacher.groups) {
                        navController.navigate(
                            route = NavRoute.Group.route + "/${NavArg.GroupId.name}=val1".replace(
                                oldValue = "val1", newValue = it.second.toString()
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .heightIn(max = 60000.dp)
                ) {
                    ExpandableSimpleLazyColumn(label = "Предметы", values = teacher.courses)
                }
            }
        }
    }
}
