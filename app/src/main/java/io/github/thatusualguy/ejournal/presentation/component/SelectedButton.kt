package io.github.thatusualguy.ejournal.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun SelectedButton(text: String, selected: Boolean = false, onClick: () -> Unit = {}) {
    val backgroundColor =
        if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    val textColor =
        if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(text = text)
    }
}


@Preview
@Composable
fun SelectedButtonPreview() {
    EJournalTheme {
        Column {
            SelectedButton("text",selected = false) {}
            SelectedButton("text",selected = true) {}

        }
    }
}