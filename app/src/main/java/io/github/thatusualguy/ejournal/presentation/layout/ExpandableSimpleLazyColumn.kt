package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import io.github.thatusualguy.ejournal.presentation.component.ExpandableCard
import io.github.thatusualguy.ejournal.presentation.component.SimpleItemCard
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun ExpandableSimpleLazyColumn(
    label: String,
    values: List<Pair<String, Int>>,
    onClick: ((Pair<String, Int>) -> Unit)? = null
) {
    ExpandableCard(header = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = label)
            Text(
                text = values.size.toString(),
                modifier = Modifier.alpha(ContentAlpha.medium)
            )
        }
    },
        content = {
            LazyColumn(content = {
                items(values) {
                    if (onClick == null)
                        SimpleItemCard(
                            label = it.first
                        )
                    else
                        SimpleItemCard(
                            label = it.first,
                            onClick = {
                                onClick.invoke(it)
                            }
                        )
                }
            })
        }
    )
}

@Composable
@Preview
fun ExpandableSimpleLazyColumnPreview() {
    val values = listOf(
        Pair("name meme", 1),
        Pair("name meme", 1),
        Pair("name meme", 1),
        Pair("name meme", 1),
        Pair("name meme", 1),
        Pair("name meme", 1),
        Pair("name meme", 1),
    )

    EJournalTheme {
        ExpandableSimpleLazyColumn("memes", values) { }
    }
}