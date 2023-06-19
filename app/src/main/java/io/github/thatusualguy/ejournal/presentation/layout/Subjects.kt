package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.thatusualguy.ejournal.presentation.component.CircularMarkIndicator
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme
import io.github.thatusualguy.ejournal.presentation.ui.subjects.SubjectItem

@Composable
fun SubjectEntry(item: SubjectItem, onClick: (() -> Unit)) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        elevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = item.name,
                modifier = Modifier.fillMaxWidth(0.7f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            CircularMarkIndicator(mark = item.average.toFloat())
        }
    }
}

@Preview
@Composable
fun SubjectEntryPreview() {
    val subjects = listOf(
        SubjectItem("Русскиadsjbajbdjwbawbjbjajwdbjй", 1, 4.54543),
        SubjectItem("Математика", 2, 3.5),
        SubjectItem("Геометрия", 2, 3.0),
        SubjectItem("Алгебра", 1, 5.0),
        SubjectItem("Алгебра", 1, 2.1),
    )
    EJournalTheme {
        Column {
            subjects.forEach { item ->
                SubjectEntry(
                    item
                ) { }
            }
        }
    }
}


