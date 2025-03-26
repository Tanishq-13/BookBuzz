package com.example.myapplication.token;

import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.requests.RefreshTokenRequest;
import com.example.myapplication.apis.response.JwtResponseDto;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

public class AuthInterceptor implements Interceptor {
    private TokenManager tokenManager;
    private ApiService apiService;

    public AuthInterceptor(TokenManager tokenManager, ApiService apiService) {
        this.tokenManager = tokenManager;
        this.apiService = apiService;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = tokenManager.getAccessToken();
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        Response response = chain.proceed(request);

        if (response.code() == 401) { // Unauthorized, token expired
            refreshAccessToken();

            // Retry the request with new token
            accessToken = tokenManager.getAccessToken();
            request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            return chain.proceed(request);
        }

        return response;
    }

    private void refreshAccessToken() {
        String refreshToken = tokenManager.getRefreshToken();
        if (refreshToken == null) return;

        Call<JwtResponseDto> call = apiService.refreshAccessToken(new RefreshTokenRequest(refreshToken));
        try {
            retrofit2.Response<JwtResponseDto> response = call.execute();
            if (response.isSuccessful()) {
                JwtResponseDto jwtResponseDto = response.body();
                tokenManager.saveTokens(jwtResponseDto.getAccessToken(), jwtResponseDto.getToken());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
