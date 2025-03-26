package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.util.Log;

import com.example.myapplication.apis.ApiClient;
import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.requests.RefreshTokenRequest;
import com.example.myapplication.apis.response.JwtResponseDto;
import com.example.myapplication.launch_page.HomeActivity;
import com.example.myapplication.token.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Page extends AppCompatActivity {
    private static final long SPLASH_DELAY = 5000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageView gifImageView = findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.iiituanim);

        TokenManager tokenManager = new TokenManager(this);
        String refreshToken = tokenManager.getRefreshToken();

        if (refreshToken != null) {
            // Refresh token exists, try to refresh access token
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            refreshAccessToken(tokenManager, apiService);
        } else {
            Log.d("cant","cant find");
            // No refresh token, navigate to login
            navigateToLogin();
        }
    }

    private void refreshAccessToken(TokenManager tokenManager, ApiService apiService) {
        RefreshTokenRequest request = new RefreshTokenRequest(tokenManager.getRefreshToken());
        Call<JwtResponseDto> call = apiService.refreshAccessToken(request);
        Log.d("refree",request.getToken());

        call.enqueue(new Callback<JwtResponseDto>() {
            @Override
            public void onResponse(Call<JwtResponseDto> call, Response<JwtResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JwtResponseDto jwtResponseDto = response.body();
                    tokenManager.saveTokens(jwtResponseDto.getAccessToken(), jwtResponseDto.getToken());

                    // Successfully refreshed, navigate to HomeActivity
                    navigateToHome();
                } else {
                    Log.e("Token Refresh", "Failed to refresh, redirecting to login");
                    navigateToLogin();
                }
            }

            @Override
            public void onFailure(Call<JwtResponseDto> call, Throwable throwable) {
                Log.e("Token Refresh", "Network error while refreshing token");
                navigateToLogin();
            }
        });
    }

    private void navigateToHome() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Home_Page.this, HomeActivity.class));
            finish();
        }, SPLASH_DELAY);
    }

    private void navigateToLogin() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Home_Page.this, authentication.class));
            finish();
        }, SPLASH_DELAY);
    }
}
