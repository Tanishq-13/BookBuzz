package com.example.myapplication;

import com.google.firebase.Timestamp;
// Required public no-argument constructor
public class OverView {

    private String userId;
    private String overviewText;
    private Timestamp timestamp;
    // Required public no-argument constructor
    public OverView() {
    }

    public OverView(String userId, String commentText, Timestamp timestamp) {
        this.userId = userId;
        this.overviewText = commentText;
        this.timestamp = timestamp;
    }

    // Getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    // Getter and setter for commentText
    public String getCommentText() {
        return overviewText;
    }

    public void setCommentText(String commentText) {
        this.overviewText = commentText;
    }

    // Getter and setter for timestamp
    public Timestamp getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
