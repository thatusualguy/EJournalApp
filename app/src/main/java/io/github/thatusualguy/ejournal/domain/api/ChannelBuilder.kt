package io.github.thatusualguy.ejournal.domain.api

import io.grpc.Channel
import io.grpc.ClientInterceptors
import io.grpc.ManagedChannelBuilder

data class ServerInfo(
    val address: String = "computersultimatemedia.ru", val port: Int = 8888
//    val address: String = "10.0.2.2", val port: Int = 7119
//    val address: String = "10.0.2.2", val port: Int = 7118
)

// singleton class
object ChannelBuilder {

    var serverInfo: ServerInfo = ServerInfo()
        set(value) {
            field = value
            buildChannel()
        }

    var channel: Channel = buildChannel()
        private set
//        get() {
//            return buildChannel()
//        }

    var JwtToken: String? = null
        set(value) {
            field = value
            buildChannel()
        }

    private fun buildChannel(): Channel {
//        if (serverInfo == null)
//            serverInfo = ServerInfo()

        channel = ManagedChannelBuilder.forAddress(serverInfo.address, serverInfo.port)
            .useTransportSecurity()
//            .usePlaintext()
//            .maxRetryAttempts(5)
            .build()

        if (!JwtToken.isNullOrEmpty()) {
            val headerClientInterceptor = AuthClientInterceptor(JwtToken)
            channel =
                ClientInterceptors.intercept(channel, headerClientInterceptor)
        }
        return channel
    }
}