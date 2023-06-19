package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.domain.models.Teacher
import io.github.thatusualguy.ejournal.presentation.component.ExpandableCard
import io.github.thatusualguy.ejournal.domain.repo.formatName
import io.github.thatusualguy.ejournal.presentation.navigation.NavArg
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute


@Composable
fun TeacherEntry(item: Teacher, navController: NavHostController?) {
    ExpandableCard(
        header = {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = item.lastName + " " + formatName(item.firstName) + " " + formatName(
                            item.middleName
                        ), modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Medium,
                        fontSize = (MaterialTheme.typography.body1.fontSize.value + 4).sp
                    )
                }
                Row {
                    Text(
                        text = " ${item.courses.size} предметов",
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
                            "Предметов",
                            "Групп",
                            "Связь",
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
                            item.courses.size,
                            item.groups.size,
                            item.contactInfo.let {
                                var s = ""
                                if (it.any {  it.contains("tel:")})
                                    s += "Телефон "
                                if (it.any {  it.contains("email:")})
                                    s += "Email "
                                if (it.any {  it.contains("vk:")})
                                    s += "ВКонтакте"
                                if(s.isBlank())
                                    s=" - "
                                s
                            },
                        )
                        labels.forEach {
                            Text(
                                text = "$it",
                                modifier = Modifier.alpha(ContentAlpha.high),
                            )
                        }

                        TextButton(onClick = {
                            navController?.navigate(
                                NavRoute.Timetable.route + "?${NavArg.GroupId.name}=-1?${NavArg.TeacherId.name}={${NavArg.TeacherId.name}}"
                                    .replace(
                                        oldValue = "{${NavArg.TeacherId.name}}",
                                        newValue = item.id.toString()
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
                        route = NavRoute.Teacher.route + "/${NavArg.TeacherId.name}=val1".replace(
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