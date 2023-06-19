package io.github.thatusualguy.ejournal.domain.api;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

import io.grpc.Metadata;

public class GrpcHeader {
    private GrpcHeader() {
    }

    private static final String AUTHORIZATION_KEY = "Authorization";

    public static final Metadata.Key<String> AUTHORIZATION =
            Metadata.Key.of(AUTHORIZATION_KEY, ASCII_STRING_MARSHALLER);
}
