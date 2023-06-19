package io.github.thatusualguy.ejournal.domain.api;

import io.grpc.*;


public class AuthClientInterceptor implements ClientInterceptor {

    private final String Jwt;

    public AuthClientInterceptor(String Jwt) {
        this.Jwt = Jwt;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, final Metadata metadata) {
                final Listener<RespT> tracingResponseListener = responseListener(responseListener);

                super.start(tracingResponseListener, injectInternalToken(metadata));
            }
        };
    }

    private <RespT> ForwardingClientCallListener<RespT> responseListener(ClientCall.Listener<RespT> responseListener) {
        return new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
            @Override
            public void onClose(Status status, Metadata metadata) {
//                handleAuthStatusCodes(status);

                super.onClose(status, metadata);
            }
        };
    }

    private Metadata injectInternalToken(Metadata metadata) {
        final String authHeader = metadata.get(GrpcHeader.AUTHORIZATION);

        if (authHeader == null || authHeader.isEmpty()) {
            metadata.put(GrpcHeader.AUTHORIZATION, "Bearer " + Jwt);
        }

        return metadata;
    }

//    private void handleAuthStatusCodes(Status status) {
//    }
}