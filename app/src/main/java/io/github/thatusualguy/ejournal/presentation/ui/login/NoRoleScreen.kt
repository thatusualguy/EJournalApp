package io.github.thatusualguy.ejournal.presentation.ui.login

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.presentation.component.website
import io.github.thatusualguy.ejournal.presentation.layout.AppLogo
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun NoRoleScreen(navController: NavHostController?) {
    val context = LocalContext.current

    val vkName = "Computers Ultimate Media"
    val vkLink = "https://vk.com/computersultimatemedia"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppLogo()
        Column {
            Text(
                text = "Необходимо подтверждение",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "Приложение EJournal разработано для использования строго " +
                        "студентами ФСПО ГУАП, поэтому каждому зарегистрированному " +
                        "пользователю требуется пройти процедуру подтверждения."+
                        "\nЧтобы получить подтверждение, обратитесь в сообщения " +
                        "группы $vkName в ВКонтакте.",
                style = MaterialTheme.typography.body1,
                lineHeight = MaterialTheme.typography.body1.fontSize.times(4f / 3f).times(1f)
            )
            Text(text = "$vkName:\n${vkLink.substringAfter("://")}",
                modifier = Modifier.padding(top = 16.dp))
        }


        Column() {
            Button(
                onClick = { website(vkLink, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(text = "Перейти к сообществу")
            }
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText(vkName, vkLink)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(
                        context,
                        "Ссылка скопирована в буфер обмена",
                        Toast.LENGTH_LONG
                    ).show()
                }) {
                Text(text = "Скопировать ссылку")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NoRoleScreenPreview() {
    EJournalTheme {
        NoRoleScreen(null)
    }
}