package io.github.thatusualguy.ejournal.presentation.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
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
fun SignUpScreen(navController: NavHostController?) {
    val signUpViewModel = viewModel(modelClass = SignUpViewModel::class.java)
    val state = signUpViewModel.state.collectAsState().value
    val scaffoldState = rememberScaffoldState()


    val isCreateButtonEnabled = !state.isTryingToSignUp

    state.userMessage?.let { userMessage ->
        LaunchedEffect(userMessage) {
            scaffoldState.snackbarHostState.showSnackbar(userMessage)
            signUpViewModel.userMessageShown()
        }
    }

    LaunchedEffect(state.isAccountCreated) {
        if (state.isAccountCreated) {
            navController?.navigate(NavRoute.NoRole.route) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
            signUpViewModel.onAccountCreated()
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


                AppLogo(isLoading = state.isTryingToSignUp)

                Column() {

                    Text(
                        text = "Создание аккаунта",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.h5
                    )

                    OutlinedTextField(
                        value = state.email,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        onValueChange = { signUpViewModel.onEmailChange(it) },
                        label = { Text(text = "Эл. почта") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(painterResource(id = R.drawable.email), null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = !state.isValidEmail,
                        shape = MaterialTheme.shapes.medium
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedTextField(
                        value = state.phone,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        onValueChange = { signUpViewModel.onPhoneChange(it) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        label = { Text(text = "Телефон") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.phone), null
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = !state.isValidPhone,
                        shape = MaterialTheme.shapes.medium
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedTextField(
                        value = state.password,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        onValueChange = { signUpViewModel.onPasswordChange(it) },
                        label = { Text(text = "Пароль") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.lock), null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = !state.isValidPassword,
                        shape = MaterialTheme.shapes.medium
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    OutlinedTextField(
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        ),
                        value = state.passwordRepeat,
                        onValueChange = { signUpViewModel.onPasswordRepeatChange(it) },
                        label = { Text(text = "Повторите пароль") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.lock), null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = !state.isValidPasswordRepeat,
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    Button(
                        onClick = { signUpViewModel.signUp() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = MaterialTheme.shapes.medium,
                        enabled = isCreateButtonEnabled
                    ) {
                        Text(text = "Создать аккаунт")
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Уже есть аккаунт?")
                    TextButton(onClick = { navController?.popBackStack() }) {
                        Text(text = "Войти")
                    }
                }
            }
        }
    }
}


    @Preview(showBackground = true)
    @Composable
    fun SignUpPreview() {
        EJournalTheme {
            SignUpScreen(null)
        }
    }