package com.example.asian.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final String mAccessToken;

    public AuthInterceptor(String accessToken) {
        this.mAccessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + mAccessToken)
                .build();

        return chain.proceed(newRequest);
    }
}