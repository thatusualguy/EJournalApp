package io.github.thatusualguy.ejournal.presentation.component

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.thatusualguy.ejournal.domain.models.LessonItem
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableCard(
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onHold: () -> Unit = {},
    expanded: Boolean = false
) {
    var expandedState by remember { mutableStateOf(expanded) }
    val transition = updateTransition(
        targetState = expandedState, label = "ExpandRotate"
    )

    val rotation by transition.animateFloat(
        transitionSpec = {
            tween(1000)
        }, label = "Rotate"
    ) { state ->
        if (state) 180f else 0f
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)

        .combinedClickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { expandedState = !expandedState },
            onLongClick = { onHold.invoke() }
        )
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300, easing = LinearOutSlowInEasing
            )
        ),
//        backgroundColor = MaterialTheme.colors.background,
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f))
                { header.invoke() }
                IconButton(
                    onClick = { expandedState = !expandedState },
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .rotate(rotation)
                ) {
                    Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
                }
            }
            if (expandedState) {
                content.invoke()
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
@Preview
fun ExpandableCardPreview() {
    val formatter = SimpleDateFormat("dd.MM")
    val lessons = listOf(
        LessonItem(Date(), "5", false, "Изучение жизни кроликов на природе. Крутые!!", "дз 12412"),
        LessonItem(Date(), "5", false, "Изучение жизни кроликов на природе. Крутые!!", "дз 12412"),
        LessonItem(Date(), "5", false, "Изучение жизни кроликов на природе. Крутые!!", "дз 12412"),
        LessonItem(Date(), "5", false, "Изучение жизни кроликов на природе. Крутые!!", "дз 12412"),
        LessonItem(Date(), "5", false, "Изучение жизни кроликов на природе. Крутые!!", "дз 12412"),
    )

    EJournalTheme {
        Column {
            lessons.forEach { item ->
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
                        Text(text = item.theme, modifier = Modifier.fillMaxWidth())
                        Divider(
                            Modifier
                                .alpha(ContentAlpha.high)
                                .fillMaxWidth(0.8f)
                                .align(Alignment.CenterHorizontally)
                        )
                    },
                    onHold = {}
                )
            }
        }
    }
}