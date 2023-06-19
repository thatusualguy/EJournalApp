package io.github.thatusualguy.ejournal.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.Teacher
import io.github.thatusualguy.ejournal.grpc.GroupsGrpcKt
import io.github.thatusualguy.ejournal.grpc.TeachersGrpcKt
import io.github.thatusualguy.ejournal.presentation.component.ComboOption
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchTeacherState(
    val searchPrompt: String = "",

    val groups: List<ComboOption> = emptyList(), val selectedGroups: List<Int> = emptyList(),

    val courses: List<ComboOption> = emptyList(), val selectedCourses: List<Int> = emptyList(),

    val teachers: List<Teacher> = emptyList(), val userMessage: String? = null
)


class SearchTeacherViewModel : ViewModel() {
    private val _state = MutableStateFlow(SearchTeacherState())
    val state: StateFlow<SearchTeacherState>
        get() = _state

    var allTeachers: List<Teacher> = emptyList()

    init {
        getTeachersFromApi()
        getGroupsFromApi()
        getCoursesFromApi()
    }

    private fun getCoursesFromApi() {
        viewModelScope.launch {
            val stub = TeachersGrpcKt.TeachersCoroutineStub(ChannelBuilder.channel)
            val request = Empty.newBuilder().build()
            try {
                val result = stub.getCoursesList(request)

                _state.update { it ->
                    it.copy(courses = result.valuesOrBuilderList.map { s ->
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

    private fun getTeachersFromApi() {
        viewModelScope.launch {
            val stub = TeachersGrpcKt.TeachersCoroutineStub(ChannelBuilder.channel)
            val request = Empty.newBuilder().build()
            try {
                val result = stub.getAllTeachers(request)

                allTeachers = result.teachersOrBuilderList.map { s ->
                    Teacher(
                        id = s.id,
                        firstName = s.name.firstName,
                        lastName = s.name.lastName,
                        middleName = s.name.middleName,
                        groups = s.groupsList.map { Pair(it.name, it.id) },
                        courses = s.coursesList.map { Pair(it.name, it.id) },
                        contactInfo = s.contactInfo.split(";")
                    )
                }.sortedBy { it.lastName }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
            filterTeachers()
        }
    }

    private fun filterTeachers() {
        val state = state.value
        _state.update {
            it.copy(teachers = allTeachers.filter { teacher ->
                (state.selectedGroups.isEmpty() || teacher.groups.any { it.second in state.selectedGroups })
                        && (state.selectedCourses.isEmpty() || teacher.courses.any { it.second in state.selectedCourses })
                        && (state.searchPrompt.isBlank()
                        || "${teacher.lastName} ${teacher.firstName} ${teacher.middleName}".contains(
                    state.searchPrompt, true
                ) || teacher.groups.any {
                    it.first.contains(
                        state.searchPrompt,
                        true
                    )
                } || teacher.courses.any { it.first.contains(state.searchPrompt, true) })
            })
        }
    }

    fun onSearchPromptChange(newPrompt: String) {
        _state.update { it.copy(searchPrompt = newPrompt) }
        filterTeachers()
    }

    fun onGroupsChange(newSelection: List<ComboOption>) {
        _state.update { it.copy(selectedGroups = newSelection.map { it.id }) }
        filterTeachers()
    }

    fun onCoursesChange(newSelection: List<ComboOption>) {
        _state.update { it.copy(selectedCourses = newSelection.map { it.id }) }
        filterTeachers()
    }

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }

}

