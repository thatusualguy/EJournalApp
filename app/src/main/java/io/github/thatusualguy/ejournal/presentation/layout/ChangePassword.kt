package io.github.thatusualguy.ejournal.presentation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.thatusualguy.ejournal.domain.repo.isValidPassword
import io.github.thatusualguy.ejournal.presentation.component.PasswordOutlinedTextField

@Preview
@Composable
fun ChangePasswordDialog(
    onClose: () -> Unit = {},
    onSave: (oldPassword: String, newPassword: String) -> Unit = { _, _ -> }
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    val oldPasswordIsValid = isValidPassword(oldPassword)
    val newPasswordIsValid = isValidPassword(newPassword)
    val confirmPasswordIsValid = newPassword == confirmNewPassword

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Изменение пароля",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                PasswordOutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = "Старый пароль",
                    isError = !oldPasswordIsValid,
                    imeAction = ImeAction.Next
                )
                PasswordOutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "Новый пароль",
                    isError = !newPasswordIsValid,
                    imeAction = ImeAction.Next
                )

                PasswordOutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    label = "Повторите пароль",
                    isError = !confirmPasswordIsValid,
                    imeAction = ImeAction.Send
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onClose) {
                        Text(text = "Отмена")
                    }
                    Button(
                        onClick = {
                            onSave(oldPassword, newPassword)
                            onClose.invoke()
                        },
                        modifier = Modifier.padding(start = 8.dp),
                        enabled = oldPasswordIsValid.and(newPasswordIsValid)
                            .and(confirmPasswordIsValid)
                    ) {
                        Text(text = "Изменить")
                    }
                }
            }
        }
    }
}



