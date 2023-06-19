package io.github.thatusualguy.ejournal.presentation.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun SimpleItemCard(label: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    var _modifier = modifier
    if (onClick != null)
        _modifier = _modifier.clickable {
            onClick.invoke()
        }

    Card(
        modifier = _modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = label,
            )
        }
    }
}

@Preview
@Composable
fun SimpleItemCardPreview() {
    EJournalTheme() {
        Column() {
            repeat(5) {
                SimpleItemCard(label = "Beautiful item")
            }
        }
    }
}