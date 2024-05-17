package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserUtil {

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Method to get the currently signed-in user's email
    public static String getUserEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getEmail();
        } else {
            return null; // User is not signed in or email is not available
        }
    }

    // Add other authentication-related methods as needed (e.g., sign out)
}
