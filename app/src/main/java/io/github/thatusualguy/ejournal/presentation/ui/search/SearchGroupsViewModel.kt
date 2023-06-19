package io.github.thatusualguy.ejournal.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.GroupShort
import io.github.thatusualguy.ejournal.grpc.GroupsGrpcKt
import io.github.thatusualguy.ejournal.presentation.component.ComboOption
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchGroupsState(
    val searchPrompt: String = "",

    val courses: List<ComboOption> = emptyList(),
    val selectedCourses: List<Int> = emptyList(),

    val specialties: List<ComboOption> = emptyList(),
    val selectedSpecialties: List<Int> = emptyList(),

    val groups: List<GroupShort> = emptyList(),
    val userMessage: String? = null
)


class SearchGroupsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SearchGroupsState())
    val state: StateFlow<SearchGroupsState>
        get() = _state

    var allGroups: List<GroupShort> = emptyList()

    init {
        getGroupsFromApi()
        getSpecialtiesFromApi()
        setCourses()
    }

    private fun getGroupsFromApi() {
        viewModelScope.launch {
            val stub = GroupsGrpcKt.GroupsCoroutineStub(ChannelBuilder.channel)
            val request = Empty.newBuilder().build()
            try {
                val result = stub.getAllGroups(request)
                allGroups = result.groupsOrBuilderList.map {
                    GroupShort(
                        id = it.id,
                        admission_year = it.admissionYear,
                        course = it.course,
                        name = it.name,
                        specialty_id = it.specialtyId,
                        specialty_name = it.specialtyName
                    )
                }.sortedBy { it.name }.sortedBy { it.course }

            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
            filterGroups()
        }
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

    fun onSearchPromptChange(newPrompt: String) {
        _state.update { it.copy(searchPrompt = newPrompt) }
        filterGroups()
    }

    fun onSpecialtiesChange(newSelection: List<ComboOption>) {
        _state.update { it.copy(selectedSpecialties = newSelection.map { it.id }) }
        filterGroups()
    }

    fun onCoursesChange(newSelection: List<ComboOption>) {
        _state.update { it.copy(selectedCourses = newSelection.map { it.id }) }
        filterGroups()
    }

    private fun filterGroups() {
        val state = state.value
        val filteredGroups = allGroups.filter { group ->
            (state.selectedCourses.isEmpty() || group.course in state.selectedCourses)
                    && (state.selectedSpecialties.isEmpty() || group.specialty_id in state.selectedSpecialties)
                    && (state.searchPrompt.isBlank() || listOf(
                group.name,
                group.specialty_name,
                group.admission_year.toString()
            ).any {
                it.contains(state.searchPrompt, true)
            })
        }

        _state.update {
            it.copy(groups = filteredGroups)
        }
    }

    fun userMessageShown() {
        _state.update { it.copy(userMessage = null) }
    }
}