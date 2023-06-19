package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.thatusualguy.ejournal.R
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun AppLogo(isLoading: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .widthIn(max = 250.dp)
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.fspo_sign
                ), contentDescription = null, modifier = Modifier.fillMaxSize()
            )
            if (isLoading)
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 8.dp
                )
        }
        Text(
            text = "EJournal",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun AppLogoPreview() {
    EJournalTheme() {
        Column(modifier = Modifier.fillMaxSize()) {
            AppLogo()
            AppLogo(true)
        }
    }
}