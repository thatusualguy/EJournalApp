package io.github.thatusualguy.ejournal.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.repo.isValidEmail
import io.github.thatusualguy.ejournal.domain.repo.isValidPassword
import io.github.thatusualguy.ejournal.grpc.AccountGrpcKt
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginViewState(
    val email: String = "",
    val isValidEmail: Boolean = true,
    val password: String = "",
    val isValidPassword: Boolean = true,
    val isRememberMe: Boolean = false,
    val isTryingToLogin: Boolean = false,
    val userMessage: String? = null,
    val isUserLoggedIn: Boolean = false,
    val roles: List<String>? = null
)

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState>
        get() = _state

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, isValidEmail = isValidEmail(email)) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, isValidPassword = isValidPassword(password)) }
    }

    fun onRememberMeChange(isRememberMe: Boolean) {
        _state.update { it.copy(isRememberMe = isRememberMe) }
    }

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

    fun onLoggedIn() {
        _state.update { it.copy(isUserLoggedIn = false) }
    }

    fun logIn() {
        viewModelScope.launch {
            _state.update { it.copy(isTryingToLogin = true) };
            try {
                var stub = AccountGrpcKt.AccountCoroutineStub(ChannelBuilder.channel)
                val loginRequest = Ejournal.login_request.newBuilder()
                    .setEmail(state.value.email)
                    .setPassword(state.value.password)
                    .build()

                val loginReply = stub.login(loginRequest)

                if (loginReply.hasErrorMessage()) {
                    _state.update { it.copy(userMessage = loginReply.errorMessage) }
                    return@launch
                } else
                    ChannelBuilder.JwtToken = loginReply.jwt

                stub = AccountGrpcKt.AccountCoroutineStub(ChannelBuilder.channel)
                val emptyRequest = Empty.newBuilder().build()
                val result = stub.getRoles(emptyRequest)

                _state.update {
                    it.copy(
                        isUserLoggedIn = true,
                        roles = result.rolesList
                    )
                }

            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE)
                    msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
            _state.update { it.copy(isTryingToLogin = false) }
        }
    }
}
