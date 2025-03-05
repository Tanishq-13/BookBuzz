package com.example.myapplication.apis.requests;

public class SignupRequest {
    private String username;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String password;
    private String email;

    // Constructor
    public SignupRequest(String username, String password, String email,String phone_number,String first_name,String last_name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.first_name=first_name;
        this.last_name=last_name;
        this.phone_number=phone_number;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
