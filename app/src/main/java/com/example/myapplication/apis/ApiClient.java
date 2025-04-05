package com.example.myapplication.apis;

import com.example.myapplication.token.AuthInterceptor;
import com.example.myapplication.token.TokenManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.22.1.63:9898/";

    private static Retrofit retrofit = null;
    public static Retrofit getClient2(TokenManager tokenManager, ApiService apiService) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager, apiService))
                .build();
        return new Retrofit.Builder()
                .baseUrl("http://10.22.1.63:9898/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
