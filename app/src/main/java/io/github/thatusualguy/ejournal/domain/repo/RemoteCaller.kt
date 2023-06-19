package io.github.thatusualguy.ejournal.domain.repo

//import io.grpc.Status
//import io.grpc.StatusException
//import kotlinx.coroutines.flow.update
//
//class RemoteCaller {
//    fun callApi(call: () -> Unit, onError: (message: String) -> Unit) {
//        try {
//            call.invoke()
//        } catch (e: StatusException) {
//            var msg =
//                when (e.status) {
//                    Status.ABORTED, Status.CANCELLED -> "Запрос отменен"
//                    Status.INTERNAL -> "Ошибка сервера"
//                    Status.NOT_FOUND -> "Объект не найден"
//                    Status.DEADLINE_EXCEEDED -> "Сервер слишком долго не отвечает"
//                    Status.UNIMPLEMENTED -> "Ошибка сервера"
//                    Status.FAILED_PRECONDITION ->
//                    Status.UNAVAILABLE -> "Сервер недоступен. Проверьте ваше подключение к интернету"
//                    else -> e.localizedMessage
//                }
//
//            if (e.status == Status.UNAVAILABLE) msg = "Сервер недоступен"
//            _state.update { it.copy(userMessage = msg) }
//        } catch () {
//
//        }
//    }
//
//}