package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.domain.models.StudentItem
import io.github.thatusualguy.ejournal.presentation.component.ExpandableCard
import io.github.thatusualguy.ejournal.domain.repo.formatName
import io.github.thatusualguy.ejournal.presentation.navigation.NavArg
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun StudentEntry(
    item: StudentItem,
    navController: NavHostController? = null
) {
    ExpandableCard(
        header = {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = item.last_name + " " + formatName(item.first_name) + " " + formatName(
                            item.middle_name
                        ), modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Medium,
                        fontSize = (MaterialTheme.typography.body1.fontSize.value + 4).sp
                    )
                    Text(
                        text = item.group_name,
                        fontSize = (MaterialTheme.typography.body1.fontSize.value + 4).sp
                    )
                }
                Row {
                    Text(
                        text = item.specialty_name,
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                Row {
                    Column(modifier = Modifier, horizontalAlignment = Alignment.Start) {
                        val labels = listOf(
                            "Курс",
                            "Группа",
                            "Спец.",
                            "Бюджет",
//                        "Расписание",
                        )
                        labels.forEach {
                            Text(
                                text = it,
                                modifier = Modifier.alpha(ContentAlpha.medium),
                            )
                        }
                        Text(
                            text = "Расписание",
                            modifier = Modifier
                                .alpha(ContentAlpha.medium)
                                .padding(top = 14.dp, end = 8.dp),
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        val labels = listOf(
                            item.course,
                            item.group_name,
                            item.specialty_name.substringBefore(" "),
                            if (item.budget) "да" else "нет",

                            )
                        labels.forEach {
                            Text(
                                text = "$it",
                                modifier = Modifier.alpha(ContentAlpha.high),
                            )
                        }

                        TextButton(onClick = {
                            navController?.navigate(
                                NavRoute.Timetable.route + "?${NavArg.GroupId.name}={${NavArg.GroupId.name}}?${NavArg.TeacherId.name}=-1"
                                    .replace(
                                        oldValue = "{${NavArg.GroupId.name}}",
                                        newValue = item.group_id.toString()
                                    )
                            )
                        }) {
                            Text(
                                text = "перейти к расписанию",
                                modifier = Modifier.alpha(ContentAlpha.high),
                            )
                        }
                    }
                }
                TextButton(onClick = {
                    navController?.navigate(
                        route = NavRoute.Student.route + "/${NavArg.StudentId.name}=val1".replace(
                            oldValue = "val1",
                            newValue = item.id.toString()
                        )
                    )
                }) {
                    Text(text = "перейти к профилю")
                }
            }
        },
    )
}

@Composable
@Preview
fun StudentEntryPreview() {
    val student = StudentItem(
        id = 1000,
        first_name = "И",
        last_name = "Фамилия",
        middle_name = "О",
        group_id = 10,
        group_name = "C921",
        course = 4,
        budget = false,
        specialty_id = 10,
        specialty_name = "09.02.07 Информационные системы и программирование"
    )

    EJournalTheme {
        LazyColumn {
            items(5) {
                StudentEntry(student)
            }
        }
    }
}