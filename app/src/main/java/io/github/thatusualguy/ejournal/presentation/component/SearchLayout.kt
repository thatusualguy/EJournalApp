package io.github.thatusualguy.ejournal.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme


@Composable
fun SlideUpPanelLayout(
    content: @Composable () -> Unit,
    panelHeader: String,
    panelContent: @Composable () -> Unit
) {
    var isPanelOpen by remember { mutableStateOf(false) }
    var boxHeightPx by remember {
        mutableStateOf(0f)
    }
    val maxHeightPx = boxHeightPx
    val minHeight = 60.dp
    val minHeightPx = with(LocalDensity.current) { minHeight.toPx() }
    var height by remember {
        mutableStateOf(if (isPanelOpen) maxHeightPx else minHeightPx)
    }

    Surface(color = MaterialTheme.colors.background) {
        BoxWithConstraints(modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                boxHeightPx =
                    coordinates.size.height
                        .toFloat()
                        .coerceAtLeast(minimumValue = minHeightPx)
            }
        ) {
            Column {
                content.invoke()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(
                            0,
                            (maxHeightPx - height).toInt()
                        )
                    }
                    .draggable(
                        state = rememberDraggableState { deltaY ->
                            height = (height - deltaY * 2).coerceIn(minHeightPx..maxHeightPx)
                        },
                        onDragStopped = {
                            if (height >= maxHeightPx * 0.5) {
                                isPanelOpen = true
                                height = maxHeightPx
                            } else {
                                isPanelOpen = false
                                height = minHeightPx
                            }
                        },
                        orientation = Orientation.Vertical
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = panelHeader,
                        fontSize = (MaterialTheme.typography.body1.fontSize.value + 4).sp,
                        modifier = Modifier
                            .alpha(ContentAlpha.medium)
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(3.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    panelContent.invoke()
                }
            }
        }
    }
}

@Composable
@Preview
fun SlideUpPanelLayout() {
    EJournalTheme {
        SlideUpPanelLayout(
            content =
            {
                Text(text = "meme")
            },
            panelHeader = "Header",
            panelContent = {
                LazyColumn {
                    items(30) {
                        Text(text = it.toString())
                    }
                }
            })
    }
}