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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.thatusualguy.ejournal.domain.models.GroupShort
import io.github.thatusualguy.ejournal.presentation.component.AlmostTable
import io.github.thatusualguy.ejournal.presentation.component.ExpandableCard
import io.github.thatusualguy.ejournal.presentation.navigation.NavArg
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun GroupEntry(item: GroupShort, expanded: Boolean = false, navController: NavController?) {
    ExpandableCard(
        expanded = expanded,
        header = {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = item.name, modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Medium,
                        fontSize = (MaterialTheme.typography.body1.fontSize.value + 4).sp
                    )
                    Text(
                        text = "${item.course} курс",// item.group_name,
                        fontSize = (MaterialTheme.typography.body1.fontSize.value + 2).sp
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
            AlmostTable(
                left = {
                    val labels = listOf(
                        "Год поступл.",
                        "Код спец.",
                        "Спец.\n",
                    )
                    labels.forEach {
                        Text(
                            text = it,
                            modifier = Modifier
                                .alpha(ContentAlpha.medium)
                                .padding(end = 8.dp),
                        )
                    }

                    Text(
                        text = "Расписание",
                        modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .padding(top = 14.dp, end = 8.dp),
                    )
                },
                right = {
                    val labels = listOf(
                        item.admission_year,
                        item.specialty_name.substringBefore(" "),
                        item.specialty_name.substringAfter(" "),
                    )
                    labels.forEach {
                        Text(
                            text = "$it",
                            modifier = Modifier.alpha(ContentAlpha.high),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    TextButton(onClick = {
                        navController?.navigate(
                            NavRoute.Timetable.route + "?${NavArg.GroupId.name}={${NavArg.GroupId.name}}?${NavArg.TeacherId.name}=-1"
                                .replace(
                                    oldValue = "{${NavArg.GroupId.name}}",
                                    newValue = item.id.toString()
                                )
                        )
                    }) {
                        Text(
                            text = "перейти к расписанию",
                            modifier = Modifier.alpha(ContentAlpha.high),
                        )
                    }

                    TextButton(onClick = {
                        navController?.navigate(
                            NavRoute.Group.route + "/${NavArg.GroupId.name}={${NavArg.GroupId.name}}"
                                .replace(
                                    oldValue = "{${NavArg.GroupId.name}}",
                                    newValue = item.id.toString()
                                )
                        )
                    }) {
                        Text(
                            text = "перейти к группе",
                            modifier = Modifier.alpha(ContentAlpha.high),
                        )
                    }
                }
            )
        }
    )
}


@Composable
@Preview
fun GroupEntryPreview() {
    val group = GroupShort(
        id = 1,
        admission_year = 2019,
        course = 4,
        name = "C921",
        specialty_id = 1,
        specialty_name = "15.02.10 Мехатроника и мобильная робототехника"
    )

    EJournalTheme {
        Column {
            repeat(2) {
                GroupEntry(item = group, expanded = true, null)
            }
        }
    }
}
