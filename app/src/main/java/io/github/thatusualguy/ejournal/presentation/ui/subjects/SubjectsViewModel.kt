package io.github.thatusualguy.ejournal.presentation.ui.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Empty
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.grpc.StatisticsGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SubjectItem(
    val name: String, val id: Int, val average: Double
)

data class SubjectsViewState(
    val subjects: List<SubjectItem> = emptyList(), val userMessage: String? = null
)

class SubjectsViewModel : ViewModel() {
    private val _state = MutableStateFlow(SubjectsViewState())
    val state: StateFlow<SubjectsViewState>
        get() = _state

    init {
        getSubjects()
    }

    private fun getSubjects() {
        viewModelScope.launch {
            val stub = StatisticsGrpcKt.StatisticsCoroutineStub(ChannelBuilder.channel)
            try {
                val result = stub.getGpaBySubjects(Empty.getDefaultInstance())

                _state.update { it ->
                    it.copy(subjects = result.subjectsOrBuilderList.map {
                        SubjectItem(
                            it.subject,
                            it.subjectId,
                            it.average.toDouble(),
                        )
                    })
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
}
