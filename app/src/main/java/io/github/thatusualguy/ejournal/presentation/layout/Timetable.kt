package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.thatusualguy.ejournal.domain.models.DayTimetableItem
import io.github.thatusualguy.ejournal.domain.models.LessonTimetableItem
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme
import kotlin.math.max

@Composable
private fun LessonTimetableEntry(
    item: LessonTimetableItem, onClick: (() -> Unit)? = null
) {
    val lineHeight = 18.times(4f/3f).times(5)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) {
                lineHeight.toDp()
            })
            .clickable { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Text(
            text = item.lesson_num.toString(),
            modifier = Modifier
                .padding(end = 4.dp)
                .alpha(ContentAlpha.medium)
        )

        Text(
            modifier = Modifier.weight(1f),
            fontSize = (MaterialTheme.typography.body1.fontSize.value - 4).sp,
            text = item.subject_name, overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )

        Text(
            text = item.room_num
                .replace("ауд.", "", true),
//                .take(5),
            modifier = Modifier.alpha(ContentAlpha.medium),
            fontSize = 18.sp
        )
    }
}

@Composable
fun DayTimetableEntry(item: DayTimetableItem?, day: Int? = null) {
    var dayOfWeekName = item?.dayOfWeekName.orEmpty()
    if (day != null) dayOfWeekName =
        when (day) {
            1 -> "Понедельник"
            2 -> "Вторник"
            3 -> "Среда"
            4 -> "Четверг"
            5 -> "Пятница"
            6 -> "Суббота"
            else -> {
                "ошибка"
            }
        }

    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = dayOfWeekName,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
            Divider(thickness = 2.dp)
            Column(Modifier.padding(start = 6.dp, end = 6.dp, bottom = 6.dp)) {
                if (item?.lessons?.isNotEmpty() == true) {
                    val max_lesson_num = max(3, item.lessons.maxOf { it.lesson_num })

                    for (i in 1..max_lesson_num) {
                        LessonTimetableEntry(item = item.lessons.firstOrNull { it.lesson_num == i }
                            ?: LessonTimetableItem("", "", "", "", i))

                        if (i != max_lesson_num) Divider()
                    }
                } else {
                    Text(text = "Нет занятий")
                }
            }
        }
    }
}

@Composable
@Preview
fun DayTimetableEntryPreview() {
    val lessons = listOf(
        LessonTimetableItem(
            "Иностранный язык 2 подгруппа",
            "teacher AA",
            "C921",
            "204\n205",
            1
        ),
        LessonTimetableItem(
            "Введение в специальность: Часть 1. Основы исследовательской и проектной деятельности",
            "teacher AA",
            "C921",
            "204",
            2
        ),
        LessonTimetableItem(
            "Экономика",
            "teacher AA",
            "C921",
            "ауд.204",
            3
        ),
        LessonTimetableItem(
            "Экономика1",
            "teacher AA",
            "C921",
            "ауд.204",
            4
        )
    )
    val day1 = DayTimetableItem(1, "Понедельник", lessons)
    val day2 = DayTimetableItem(1, "Вторник", lessons)

    EJournalTheme {
        Column {
            repeat(3) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                        DayTimetableEntry(item = day1)
                    }
                    DayTimetableEntry(item = day2)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
@Preview(device = "spec:width=400px,height=2340px,dpi=440")
fun LessonTimetableEntryPreview() {
    val lesson = LessonTimetableItem(
        "Subject name",
        "teacher AA",
        "C921",
        "204",
        1
    )
    EJournalTheme {
        LessonTimetableEntry(lesson) {}
    }
}

@Composable
@Preview(device = "spec:width=400px,height=2340px,dpi=440")
fun DayTimetableEntryPreviewNull() {

    EJournalTheme {
        DayTimetableEntry(null, 2)
    }
}