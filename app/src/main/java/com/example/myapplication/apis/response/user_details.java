package com.example.myapplication.apis.response;

import com.google.gson.annotations.SerializedName;

public class user_details {
    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    public user_details(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
