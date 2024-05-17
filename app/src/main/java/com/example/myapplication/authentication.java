package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class authentication extends AppCompatActivity {

    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Button myButton = findViewById(R.id.button);

        TextView mname=findViewById(R.id.textView2);
        TextView memail=findViewById(R.id.editTextTextEmailAddress);
        TextView pwd=findViewById(R.id.editTextTextPassword);
        TextView cnfpwd=findViewById(R.id.editTextText);

        FirebaseAuth fauth;
        FirebaseFirestore fstore=FirebaseFirestore.getInstance();
        Button signin=findViewById(R.id.button3);
        String userEmail = UserUtil.getUserEmail();
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the actions to be performed when the button is clicked
                // For example, show a toast message
                Intent intent = new Intent(authentication.this, Login.class);
                startActivity(intent);
            }
        });
        fauth=FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memail.getText().toString().trim();
                String password = pwd.getText().toString().trim();
                String name = mname.getText().toString().trim();
                if (!cnfpwd.equals(pwd)) {
                    Toast.makeText(authentication.this, "Password doesnt match in both fields", Toast.LENGTH_SHORT).show();
                } else {
                    fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserId = fauth.getCurrentUser().getUid();
                                DocumentReference dr = fstore.collection("users").document(UserId);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Name", name);
                                user.put("Email", email);
                                dr.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(authentication.this, "User data Created", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                Toast.makeText(authentication.this, "User Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                            }
                        }
                    });
                }
            }
        });
        

    }
}