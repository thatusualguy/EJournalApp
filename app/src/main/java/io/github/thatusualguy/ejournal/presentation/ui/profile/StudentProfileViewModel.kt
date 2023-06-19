package io.github.thatusualguy.ejournal.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.Student
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.github.thatusualguy.ejournal.grpc.StudentsGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudentProfileState(
    val student: Student? = null,
    val isLoading: Boolean = true,
    val userMessage: String? = null
)

class StudentProfileViewModel(studentId: Int?) : ViewModel() {
    private val _state = MutableStateFlow(StudentProfileState(isLoading = true))
    val state: StateFlow<StudentProfileState>
        get() = _state

    init {
        if (studentId == null)
            getMyStudent()
        else
            setStudent(studentId)
    }

    private fun getMyStudent() {
        viewModelScope.launch {
            val stub = StudentsGrpcKt.StudentsCoroutineStub(ChannelBuilder.channel)
            val request = Empty.newBuilder().build()
            val result = stub.getMyStudent(request)
            setStudent(result.id)
        }
    }

    private fun setStudent(studentId: Int) {
        viewModelScope.launch {
            val stub = StudentsGrpcKt.StudentsCoroutineStub(ChannelBuilder.channel)
            val request = Ejournal.id_request.newBuilder().setId(studentId).build()
            try {
                val studentInfo = stub.getStudent(request)

                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        student = Student(
                            id = studentInfo.id,
                            firstName = studentInfo.studentName.firstName,
                            middleName = studentInfo.studentName.middleName,
                            lastName = studentInfo.studentName.lastName,
                            course = studentInfo.course,
                            specialty_id = studentInfo.specialtyId,
                            group = studentInfo.group,
                            budget = studentInfo.budget,
                            group_id = studentInfo.groupId,
                            specialty = studentInfo.specialty,
                            teachers = studentInfo.teachersList.map { Pair(it.name, it.id) },
                            courses = studentInfo.coursesList.map { Pair(it.name, it.id) },
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
        public class Factory(private val studentId: Int?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                StudentProfileViewModel(studentId) as T
        }

    }
}
