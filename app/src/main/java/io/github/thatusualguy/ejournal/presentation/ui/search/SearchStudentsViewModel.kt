package io.github.thatusualguy.ejournal.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.StudentItem
import io.github.thatusualguy.ejournal.grpc.GroupsGrpcKt
import io.github.thatusualguy.ejournal.grpc.StudentsGrpcKt
import io.github.thatusualguy.ejournal.presentation.component.ComboOption
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchStudentState(
    val searchPrompt: String = "",

    val groups: List<ComboOption> = emptyList(),
    val selectedGroups: List<Int> = emptyList(),

    val courses: List<ComboOption> = emptyList(),
    val selectedCourses: List<Int> = emptyList(),

    val specialties: List<ComboOption> = emptyList(),
    val selectedSpecialties: List<Int> = emptyList(),

    val students: List<StudentItem> = emptyList(),
    val userMessage: String? = null
)


class SearchStudentsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SearchStudentState())
    val state: StateFlow<SearchStudentState>
        get() = _state

    var allStudents: List<StudentItem> = emptyList()

    init {
        getStudentsFromApi()
        getGroupsFromApi()
        getSpecialtiesFromApi()
        setCourses()
    }

    private fun setCourses() {
        _state.update { it.copy(courses = listOf(1, 2, 3, 4).map { ComboOption("$it курс", it) }) }
    }

    private fun getSpecialtiesFromApi() {
        viewModelScope.launch {
            val stub = GroupsGrpcKt.GroupsCoroutineStub(ChannelBuilder.channel)
            val request = Empty.newBuilder().build()
            try {
                val result = stub.getSpecialtiesList(request)

                _state.update { it ->
                    it.copy(specialties = result.valuesOrBuilderList.map { s ->
                        ComboOption(s.name, s.id)
                    }.sortedBy { it.text })
                }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    private fun getGroupsFromApi() {
        viewModelScope.launch {
            val stub = GroupsGrpcKt.GroupsCoroutineStub(ChannelBuilder.channel)
            val request = Empty.newBuilder().build()
            try {
                val result = stub.getGroupsList(request)

                _state.update { it ->
                    it.copy(groups = result.valuesOrBuilderList.map { s ->
                        ComboOption(s.name, s.id)
                    }.sortedBy { it.text })
                }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    private fun getStudentsFromApi() {
        viewModelScope.launch {
            val stub = StudentsGrpcKt.StudentsCoroutineStub(ChannelBuilder.channel)
            val request = Empty.newBuilder().build()
            try {
                val result = stub.getAllStudents(request)

                allStudents = result.studentsOrBuilderList.map { s ->
                    StudentItem(
                        id = s.id,
                        first_name = s.studentName.firstName,
                        last_name = s.studentName.lastName,
                        middle_name = s.studentName.middleName,
                        group_id = s.groupId,
                        group_name = s.group,
                        course = s.course,
                        budget = s.budget,
                        specialty_id = s.specialtyId,
                        specialty_name = s.specialty
                    )
                }.sortedBy { it.id }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
            filterStudents()
        }
    }

    fun onSearchPromptChange(newPrompt: String) {
        _state.update { it.copy(searchPrompt = newPrompt) }
        filterStudents()
    }

    fun onGroupsChange(newSelection: List<ComboOption>) {
        _state.update { it.copy(selectedGroups = newSelection.map { it.id }) }
        filterStudents()
    }

    fun onSpecialtiesChange(newSelection: List<ComboOption>) {
        _state.update { it.copy(selectedSpecialties = newSelection.map { it.id }) }
        filterStudents()
    }

    fun onCoursesChange(newSelection: List<ComboOption>) {
        _state.update { it.copy(selectedCourses = newSelection.map { it.id }) }
        filterStudents()
    }

    private fun filterStudents() {
        val state = state.value
        val filteredStudents = allStudents.filter { student ->
            (state.selectedGroups.isEmpty() || student.group_id in state.selectedGroups) && (state.selectedCourses.isEmpty() || student.course in state.selectedCourses) && (state.selectedSpecialties.isEmpty() || student.specialty_id in state.selectedSpecialties) && (state.searchPrompt.isBlank() || listOf(
                "${student.last_name} ${student.first_name} ${student.middle_name}",
                student.specialty_name,
                student.group_name
            ).any {
                it.contains(state.searchPrompt, true)
            })
        }

        _state.update {
            it.copy(students = filteredStudents)
        }
    }

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }
}
