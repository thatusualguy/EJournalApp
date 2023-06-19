package io.github.thatusualguy.ejournal.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme
import kotlin.math.roundToInt

@Composable
fun CircularMarkIndicator(mark: Float, modifier: Modifier = Modifier, fontSize: TextUnit = MaterialTheme.typography.body1.fontSize) {
    val aspectModifier = if (modifier != Modifier) modifier.aspectRatio(1f) else Modifier

    val markPercent = mark / 5

    val colorStops = arrayOf(
        0.0f to Color.Red,
        2.5f / 5 to Color(0xFFFFA500),
        3.5f / 5 to Color.Yellow,
        4.5f / 5 to Color.Green
    )

    var color: Color = colorStops[0].second
    colorStops.forEach {
        if (it.first < markPercent) color = it.second
    }
    Box(
        modifier = aspectModifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = aspectModifier,
            color = color.copy(alpha = 0.8f),
            progress = markPercent,
        )

        Text(
            text = ((mark * 10).roundToInt() / 10f).toString(),
            textAlign = TextAlign.Center,
            fontSize = fontSize
        )
    }
}

@Preview
@Composable
fun CircularMarkIndicatorPreview() {
    EJournalTheme {
        Column {

            CircularMarkIndicator(mark = 4f)
            CircularMarkIndicator(mark = 4f, Modifier.width(80.dp))
            CircularMarkIndicator(mark = 4f,
                Modifier
                    .height(80.dp)
                    .aspectRatio(1f))
        }
    }
}
