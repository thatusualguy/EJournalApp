package io.github.thatusualguy.ejournal.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TableRow(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit,
    weightLeft: Float = 0.5f
) {
    Row {
        Box(modifier = Modifier.weight(weight = weightLeft))
        {
            left.invoke()
        }
        right.invoke()

    }
}

@Composable
fun AlmostTable(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit
) {
    Row {
        Column(modifier = Modifier, horizontalAlignment = Alignment.Start) {
            left.invoke()
        }
        Column(modifier = Modifier.weight(1f)) {
            right.invoke()
        }
    }
}