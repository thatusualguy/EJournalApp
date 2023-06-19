package io.github.thatusualguy.ejournal.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.Group
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.github.thatusualguy.ejournal.grpc.GroupsGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GroupProfileState(
    val group: Group? = null,
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

class GroupProfileViewModel (groupId: Int) : ViewModel() {
    private val _state = MutableStateFlow(GroupProfileState(isLoading = true))
    val state: StateFlow<GroupProfileState>
        get() = _state

    init {
        setGroup(groupId)
    }

    private fun setGroup(groupId: Int) {
        viewModelScope.launch {
            val stub = GroupsGrpcKt.GroupsCoroutineStub(ChannelBuilder.channel)
            val request = Ejournal.id_request.newBuilder().setId(groupId).build()
            try {
                val groupInfo = stub.getGroup(request)

                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        group = Group(
                            id = groupInfo.id,
                            name = groupInfo.name,
                            specialty_name = groupInfo.specialtyName,
                            specialty_id = groupInfo.specialtyId,
                            course = groupInfo.course,
                            admission_year = groupInfo.admissionYear,
                            teachers = groupInfo.teachersList.map { Pair(it.name, it.id) },
                            students = groupInfo.studentsList.map { Pair(it.name, it.id) },
                            courses = groupInfo.coursesList.map { Pair(it.name, it.id) },
                        )
                    )
                }

            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

    companion object {
        public class Factory(private val groupId: Int) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                GroupProfileViewModel(groupId) as T
        }

    }
}
