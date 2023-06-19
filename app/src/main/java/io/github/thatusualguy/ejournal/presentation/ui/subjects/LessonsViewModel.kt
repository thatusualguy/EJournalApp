package io.github.thatusualguy.ejournal.presentation.ui.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.thatusualguy.ejournal.domain.api.ChannelBuilder
import io.github.thatusualguy.ejournal.domain.models.LessonItem
import io.github.thatusualguy.ejournal.grpc.Ejournal
import io.github.thatusualguy.ejournal.grpc.JournalGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

data class LessonsState(
    val subjectName: String? = null,
    val subjectId: Int? = null,
    val lessons: List<LessonItem> = emptyList(),
    val userMessage: String? = null
)

class LessonsViewModel : ViewModel() {
    private val _state = MutableStateFlow(LessonsState())
    val state: StateFlow<LessonsState>
        get() = _state

    fun changeSubjectId(subjectId: Int) {
        _state.update { it.copy(subjectId = subjectId) }

        getLessons()
        getSubjectName()
    }

    private fun getLessons() {
        viewModelScope.launch {
            val stub = JournalGrpcKt.JournalCoroutineStub(ChannelBuilder.channel)
            val request =
                Ejournal.id_request.newBuilder().setId(state.value.subjectId!!).build()

            try {
                val result = stub.getMyMarksBySubject(request)

                _state.update {
                    it.copy(lessons = result.lessonsOrBuilderList.map { lesson ->
                        LessonItem(
                            Date(lesson.date.seconds * 1000),
                            lesson.mark.toString(),
                            false,
                            lesson.theme,
                            lesson.note
                        )
                    }.sortedByDescending { it.date })
                }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }

    private fun getSubjectName() {
        viewModelScope.launch {
            val stub = JournalGrpcKt.JournalCoroutineStub(ChannelBuilder.channel)
            val request =
                Ejournal.id_request.newBuilder().setId(state.value.subjectId!!).build()

            try {
                val result = stub.getSubjectById(request)
                _state.update {
                    it.copy(subjectName = result.subjectName)
                }
            } catch (e: StatusException) {
                var msg = e.localizedMessage
                if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
                _state.update { it.copy(userMessage = msg) }
            }
        }
    }
}
