package io.github.thatusualguy.ejournal.presentation.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.github.thatusualguy.ejournal.R
import io.github.thatusualguy.ejournal.presentation.layout.AppLogo
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme

@Composable
fun LoginScreen(
    navController: NavHostController?
) {
    val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
    val state = loginViewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            loginViewModel.userMessageShown()
        }
    }

    LaunchedEffect(state.isUserLoggedIn) {
        if(!state.isUserLoggedIn)
            return@LaunchedEffect

        if (state.roles?.contains("Student") == true) {
            navController?.navigate(NavRoute.Home.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            loginViewModel.onLoggedIn()
        }
        else{
            navController?.navigate(NavRoute.NoRole.route)
        }
    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                AppLogo(isLoading = state.isTryingToLogin)

                Column {
                    Text(
                        text = "Вход в аккаунт",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.h5
                    )
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { loginViewModel.onEmailChange(it) },
                        label = { Text(text = "Эл. почта") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.email),
                                null,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                        ),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        isError = !state.isValidEmail
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { loginViewModel.onPasswordChange(it) },
                        label = { Text(text = "Пароль") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.lock), null
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        shape = MaterialTheme.shapes.medium,
                        isError = !state.isValidPassword,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = state.isRememberMe,
                                onCheckedChange = { loginViewModel.onRememberMeChange(it) },
                            )
                            Text(
                                text = "Запомнить меня",
                                modifier = Modifier.clickable {
                                    loginViewModel.onRememberMeChange(!state.isRememberMe)
                                }
                            )
                        }
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Восстановить пароль")
                        }
                    }
                    Button(
                        onClick = { loginViewModel.logIn() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.medium,
                        enabled = !state.isTryingToLogin && state.isValidEmail && state.isValidPassword
                    ) {
                        Text(text = "Войти")
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Нет аккаунта?")
                    TextButton(onClick = { navController?.navigate(NavRoute.Signup.route) }) {
                        Text(text = "Создать")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    EJournalTheme {
        LoginScreen(null)

    }
}
