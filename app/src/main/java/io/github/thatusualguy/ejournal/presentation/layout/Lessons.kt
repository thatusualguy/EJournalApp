package io.github.thatusualguy.ejournal.presentation.layout

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import io.github.thatusualguy.ejournal.domain.models.LessonItem
import io.github.thatusualguy.ejournal.presentation.component.ExpandableCard
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme
import java.text.SimpleDateFormat
import java.util.Date




@SuppressLint("SimpleDateFormat")
@Composable
fun LessonEntry(item: LessonItem, onClick: () -> Unit) {
    val formatter = SimpleDateFormat("dd.MM")

    ExpandableCard(
        header = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatter.format(item.date), modifier = Modifier
                )
                Text(
                    text = if (item.mark != null && item.mark != "0") item.mark else "",
                    modifier = Modifier.fillMaxWidth(0.2f),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.theme,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        content = {
            Column {
                Text(text = item.theme, modifier = Modifier.fillMaxWidth())
                Divider(
                    Modifier
                        .alpha(ContentAlpha.high)
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally)
                )
                Text(text = item.note, modifier = Modifier.fillMaxWidth())
            }
        },
        onHold = {}
    )
}

@Preview
@Composable
fun LessonsEntryPreview() {
    val lesson =
        LessonItem(
            date = Date(),
            mark = "5",
            skipped = false,
            theme = "Изучение жизни кроликов на природе. Крутые!!",
            note = "стр. 14"
        )
    EJournalTheme {
        Column {
            repeat(5) {
                LessonEntry(
                    lesson
                ) { }
            }
        }
    }
}
