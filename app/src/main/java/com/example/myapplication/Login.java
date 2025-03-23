package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.Retrofitclient;
import com.example.myapplication.apis.requests.LoginRequest;
import com.example.myapplication.apis.response.JwtResponseDto;
import com.example.myapplication.launch_page.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout myButton = findViewById(R.id.donthaveaccount);
        TextView memail=findViewById(R.id.textView2);
        TextView pwd=findViewById(R.id.editTextTextEmailAddress);
        Button login=findViewById(R.id.login);
        FirebaseAuth fauth=FirebaseAuth.getInstance();
//        Button login_teacher=findViewById(R.id.button4);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the actions to be performed when the button is clicked
                // For example, show a toast message
                Intent intent = new Intent(Login.this, authentication.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=memail.getText().toString().trim();
                String password=pwd.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginRequest lrq=new LoginRequest(username,password);
                ApiService apiService= Retrofitclient.getInstance().create(ApiService.class);
                Call<JwtResponseDto> call=apiService.login(lrq);
                call.enqueue(new Callback<JwtResponseDto>() {
                    @Override
                    public void onResponse(Call<JwtResponseDto> call, Response<JwtResponseDto> response) {
                        if(response.isSuccessful()){
                            JwtResponseDto jwtResponseDto=response.body();
                            String accessToken=jwtResponseDto.getAccessToken();
                            String refreshToken=jwtResponseDto.getToken();
                            Intent intent=new Intent(Login.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Login.this, "Login failed! Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JwtResponseDto> call, Throwable throwable) {
                        Toast.makeText(Login.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}