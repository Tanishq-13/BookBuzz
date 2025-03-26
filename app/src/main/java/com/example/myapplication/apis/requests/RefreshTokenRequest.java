package com.example.myapplication.apis.requests;

public class RefreshTokenRequest {
    public String token;
    public RefreshTokenRequest(String token){
        this.token=token;
    }

    public String getToken() {
        return token;
    }
}
