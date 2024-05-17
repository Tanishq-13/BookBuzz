package com.example.myapplication;

public class User {
    private String Email;
    private String Name;

    public User() {
        // Required empty constructor for Firestore
    }

    public User(String Email, String Name) {
        this.Email= Email;
        this.Name =Name;
    }
    public String getEmail() {
        return Email;
    }


    public void setEmail(String Email) {
        this.Email = Email;
    }
    public void setName(String Name) {
        this.Name =Name;
    }


    public String getName() {
        return Name;
    }

    // Getters and setters
}

