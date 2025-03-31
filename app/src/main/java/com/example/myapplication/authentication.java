package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.apis.ApiClient;
import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.Retrofitclient;
import com.example.myapplication.apis.requests.SignupRequest;
import com.example.myapplication.apis.response.SignupResponse;
import com.example.myapplication.launch_page.HomeActivity;
import com.example.myapplication.token.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class authentication extends AppCompatActivity {

    private EditText username, email, password, phoneNumber, firstName, lastName;
    private LinearLayout loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Initialize views
        username = findViewById(R.id.username);
        email = findViewById(R.id.editTextTextEmailAddress);  // Ensure correct field mapping
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.editTextTextPhone);  // Corrected field ID
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);

        signUpButton = findViewById(R.id.button3);
        loginButton = findViewById(R.id.haveaccount);

        // Redirect to Login Activity
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(authentication.this, Login.class);
            startActivity(intent);
        });

        // Sign Up Logic - Making API call
        signUpButton.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String userName = username.getText().toString().trim();
            String userPhone = phoneNumber.getText().toString().trim();
            String userFirstName = firstName.getText().toString().trim();
            String userLastName = lastName.getText().toString().trim();

            // Validate input fields (email and password)
            if ( userPassword.isEmpty() || userName.isEmpty()) {
                Toast.makeText(authentication.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a SignupRequest object for the API call
            SignupRequest signupRequest = new SignupRequest(userName, userPassword, userEmail, userPhone, userFirstName, userLastName);

            // Retrofit API call
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<SignupResponse> call = apiService.signup(signupRequest);

            // Make the API call
            call.enqueue(new Callback<SignupResponse>() {
                @Override
                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Get the access and refresh tokens from response
                        String accessToken = response.body().getAccessToken();  // Ensure API returns this
                        String refreshToken = response.body().getToken(); // Ensure API returns this
                        Log.d("acess", String.valueOf(response));
                        if (accessToken != null && refreshToken != null) {
                            TokenManager tokenManager = new TokenManager(authentication.this);
                            tokenManager.saveTokens(accessToken, refreshToken);
                        } else {
                            Log.e("Signup", "Tokens missing in response");
                        }

                        Toast.makeText(authentication.this, "User signed up successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("Signup", "Error Code: " + response.code() + ", Body: " + errorBody);
                            Toast.makeText(authentication.this, "Signup Failed: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignupResponse> call, Throwable t) {
                    // Handle network failure or error
                    Log.d("poiu",String.valueOf(t.getMessage()));
                    Toast.makeText(authentication.this, "API Call Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
