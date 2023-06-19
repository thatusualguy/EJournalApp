package io.github.thatusualguy.ejournal.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.Teacher
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.github.thatusualguy.ejournal.grpc.TeachersGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeacherProfileState(
    val teacher: Teacher? = null,
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

class TeacherProfileViewModel(teacherId: Int) : ViewModel() {
    private val _state = MutableStateFlow(TeacherProfileState())
    val state: StateFlow<TeacherProfileState>
        get() = _state

    init {
        setTeacher(teacherId)
    }

    private fun setTeacher(teacherId: Int) {
        viewModelScope.launch {
            val stub = TeachersGrpcKt.TeachersCoroutineStub(ChannelBuilder.channel)
            val request = Ejournal.id_request.newBuilder().setId(teacherId).build()
            try {
                val teacherInfo = stub.getTeacher(request)

                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        teacher = Teacher(
                            id = teacherInfo.id,
                            firstName = teacherInfo.name.firstName,
                            middleName = teacherInfo.name.middleName,
                            lastName = teacherInfo.name.lastName,
                            contactInfo = teacherInfo.contactInfo.split(";"),
                            groups = teacherInfo.groupsList.map { Pair(it.name, it.id) },
                            courses = teacherInfo.coursesList.map { Pair(it.name, it.id) },
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
        public class Factory(private val teacherId: Int) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                TeacherProfileViewModel(teacherId) as T
        }

    }
}
