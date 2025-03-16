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

import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.Retrofitclient;
import com.example.myapplication.apis.requests.SignupRequest;
import com.example.myapplication.apis.response.SignupResponse;
import com.example.myapplication.launch_page.HomeActivity;

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
            if (userEmail.isEmpty() || userPassword.isEmpty() || userName.isEmpty()) {
                Toast.makeText(authentication.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a SignupRequest object for the API call
            SignupRequest signupRequest = new SignupRequest(userName, userPassword, userEmail, userPhone, userFirstName, userLastName);

            // Retrofit API call
            ApiService apiService = Retrofitclient.getInstance().create(ApiService.class);
            Call<SignupResponse> call = apiService.signup(signupRequest);

            // Make the API call
            call.enqueue(new Callback<SignupResponse>() {
                @Override
                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Handle successful API response
                        Toast.makeText(authentication.this, "User signed up via API", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));  // Or another activity
                    } else {
                        // Handle API error response
                        Toast.makeText(authentication.this, "API Error: " + response.message(), Toast.LENGTH_SHORT).show();
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
