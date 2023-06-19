package io.github.thatusualguy.ejournal.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.grpc.AccountGrpcKt
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsScreenState(
    val userMessage: String? = null,
    val isChangePasswordShown: Boolean = false
)

class SettingsScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenState())
    val state: StateFlow<SettingsScreenState>
        get() = _state

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

    fun changePasswordDialogState(isChangePasswordShown: Boolean) {
        _state.update { it.copy(isChangePasswordShown = isChangePasswordShown) }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            val stub = AccountGrpcKt.AccountCoroutineStub(ChannelBuilder.channel)
            val request = Ejournal.change_password_request.newBuilder()
                .setOldPassword(oldPassword)
                .setNewPassword(newPassword)
                .build()
            try {
                val result = stub.changePassword(request)
                if (result.hasErrorMessage())
                    _state.update { it.copy(userMessage = result.errorMessage) }
                else
                    _state.update { it.copy(userMessage = "Пароль успешно изменен") }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }
}