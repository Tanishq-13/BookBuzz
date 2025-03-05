package com.example.myapplication;

import android.os.UserManager;

public class userTeacher {
    private static userTeacher instance;

    private userTeacher() {
        // Private constructor to prevent instantiation
    }

    public static synchronized userTeacher getInstance() {
        if (instance == null) {
            instance = new userTeacher();
        }
        return instance;
    }

    public boolean isTeacherUser(String userEmail) {
        return userEmail != null && userEmail.endsWith("@example.com");
    }
}
