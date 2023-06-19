package io.github.thatusualguy.ejournal.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.repo.isValidEmail
import io.github.thatusualguy.ejournal.domain.repo.isValidPassword
import io.github.thatusualguy.ejournal.domain.repo.isValidPhone
import io.github.thatusualguy.ejournal.grpc.AccountGrpcKt
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpScreenState(
    val email: String = "",
    val isValidEmail: Boolean = true,
    val phone: String = "",
    val isValidPhone: Boolean = true,
    val password: String = "",
    val isValidPassword: Boolean = true,
    val passwordRepeat: String = "",
    val isValidPasswordRepeat: Boolean = true,

    val isTryingToSignUp: Boolean = false,
    val userMessage: String? = null,
    val isAccountCreated: Boolean = false
)

class SignUpViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignUpScreenState())
    val state: StateFlow<SignUpScreenState>
        get() = _state

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, isValidEmail = isValidEmail(email)) }
    }

    fun onPhoneChange(phone: String) {
        _state.update { it.copy(phone = phone, isValidPhone = isValidPhone(phone)) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, isValidPassword = isValidPassword(password)) }
    }

    fun onPasswordRepeatChange(passwordRepeat: String) {
        _state.update {
            it.copy(
                passwordRepeat = passwordRepeat,
                isValidPasswordRepeat = it.password == passwordRepeat
            )
        }

    }

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

    fun onAccountCreated() {
        _state.update { it.copy(isAccountCreated = false) }
    }

    fun signUp() {
        if (!listOf(
                state.value.isValidPhone,
                state.value.isValidEmail,
                state.value.isValidPassword,
                state.value.isValidPasswordRepeat
            ).all { it }
        ) {
            _state.update { it.copy(userMessage = "Вначале заполните все поля") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isTryingToSignUp = true) }

            val stub = AccountGrpcKt.AccountCoroutineStub(ChannelBuilder.channel)
            val request = Ejournal.register_request.newBuilder()
                .setEmail(state.value.email)
                .setPassword(state.value.password)
                .setPhone(state.value.phone)
                .build()
            try {
                val result = stub.register(request)
                if (result.hasErrorMessage())
                    _state.update { it.copy(userMessage = result.errorMessage) }
                else {
                    _state.update { it.copy(isAccountCreated = true) }
                }
            }
            catch (e: StatusException){
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE)
                    msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }

            _state.update { it.copy(isTryingToSignUp = false) }
        }
    }
}
