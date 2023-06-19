package io.github.thatusualguy.ejournal.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.BuildConfig
import io.github.thatusualguy.ejournal.R
import io.github.thatusualguy.ejournal.domain.repo.AppTheme
import io.github.thatusualguy.ejournal.domain.repo.Settings
import io.github.thatusualguy.ejournal.presentation.component.email
import io.github.thatusualguy.ejournal.presentation.component.phone
import io.github.thatusualguy.ejournal.presentation.component.website
import io.github.thatusualguy.ejournal.presentation.layout.ChangePasswordDialog
import io.github.thatusualguy.ejournal.presentation.layout.StudentScaffoldDrawer
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun SettingsScreen(navController: NavHostController?) {
    val settingViewModel = viewModel(modelClass = SettingsScreenViewModel::class.java)
    val state = settingViewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            settingViewModel.userMessageShown()
        }
    }

    StudentScaffoldDrawer(
        title = "Настройки",
        scope = scope,
        navController = navController,
        scaffoldState = scaffoldState
    ) {
        if (state.isChangePasswordShown)
            ChangePasswordDialog(
                onClose = {
                    settingViewModel.changePasswordDialogState(false)
                },
                onSave = { oldPassword, newPassword ->
                    settingViewModel.changePasswordDialogState(false)
                    settingViewModel.changePassword(
                        oldPassword,
                        newPassword
                    )
                })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(text = "Аккаунт", style = MaterialTheme.typography.h5)
            val accountActions:List<Pair<() -> Unit?, @Composable () -> Unit>> = listOf(
                {
                    settingViewModel.changePasswordDialogState(true)
                } to @Composable {
                    Text(text = "Сменить пароль")
                    Icon(Icons.Rounded.Edit, contentDescription = null)
                },
                {
                    navController?.navigate(route = NavRoute.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                } to @Composable {
                    Text(text = "Выйти из аккаунта")
                    Icon(Icons.Rounded.ExitToApp, contentDescription = null)
                },
            )
            accountActions.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            it.first.invoke()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    it.second()
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Внешний вид", style = MaterialTheme.typography.h5)

            var themeSelectorExpanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        themeSelectorExpanded = !themeSelectorExpanded
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text("Тёмная тема")

                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                )
                {
                    Row {
                        Text(
                            text = when (Settings.getAppTheme(context)
                                .collectAsState().value.themeMode) {
                                AppTheme.Dark -> "Включена"
                                AppTheme.Light -> "Выключена"
                                AppTheme.System -> "Системная"
                            }
                        )
                        Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = themeSelectorExpanded,
                        onDismissRequest = { themeSelectorExpanded = false },
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                Settings.setAppTheme(context, AppTheme.Dark)
                                themeSelectorExpanded = false
                            }
                        ) {
                            Text("Включена")
                        }
                        DropdownMenuItem(
                            onClick = {
                                Settings.setAppTheme(context, AppTheme.Light)
                                themeSelectorExpanded = false
                            }
                        ) {
                            Text("Выключена")
                        }
                        DropdownMenuItem(
                            onClick = {
                                Settings.setAppTheme(context, AppTheme.System)
                                themeSelectorExpanded = false
                            }
                        ) {
                            Text("Системная")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Разработчик", style = MaterialTheme.typography.h5)

            val developerActions = listOf<Pair<() -> Unit?, @Composable () -> Unit>>(
                {
                    website("https://github.com/thatusualguy", context)
                } to @Composable {
                    Text(text = "Github: @thatusualguy")
                    Icon(painterResource(R.drawable.link), contentDescription = null)
                },
                {
                    website("https://t.me/thatusualguy", context)
                } to @Composable {
                    Text(text = "TG: @thatusualguy")
                    Icon(painterResource(R.drawable.link), contentDescription = null)
                },
                {
                    website("https://vk.com/public220441510", context)
                } to @Composable {
                    Text(text = "Сообщество ВКонтакте")
                    Icon(painterResource(R.drawable.link), contentDescription = null)
                },
            )
            developerActions.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            it.first.invoke()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    it.second()
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Администрация колледжа", style = MaterialTheme.typography.h5)


            val collegeActions = listOf<Pair<() -> Unit?, @Composable () -> Unit>>(
                {
                    email("aviacollege@guap.ru", context)
                } to @Composable {
                    Text(text = "Email: aviacollege@guap.ru")
                    Icon(painterResource(R.drawable.link), contentDescription = null)
                },
                {
                    phone("+7(812)388-86-93", context)
                } to @Composable {
                    Text(text = "Телефон: +7 (812) 388-86-93")
                    Icon(painterResource(R.drawable.link), contentDescription = null)
                },
                {
                    website("https://new.guap.ru/fspo/", context)
                } to @Composable {
                    Text(text = "Сайт факультета: new.guap.ru/fspo/")
                    Icon(painterResource(R.drawable.link), contentDescription = null)
                },
                {
                    website("https://vk.com/12fak", context)
                } to @Composable {
                    Text(text = "Группа факультета: ФСПО ГУАП")
                    Icon(painterResource(R.drawable.link), contentDescription = null)
                },
            )
            collegeActions.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            it.first.invoke()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    it.second()
                }
            }

            val version = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(version, Modifier.alpha(ContentAlpha.medium))
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    EJournalTheme() {
        SettingsScreen(navController = null)
    }
}