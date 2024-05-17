package com.example.myapplication;

import com.google.firebase.Timestamp;
// Required public no-argument constructor
public class Comment {

    private String userId;
    private String commentText;
    private Timestamp timestamp;
    private float rating;

    // Required public no-argument constructor
    public Comment() {
    }

    public Comment(String userId, String commentText, Timestamp timestamp,float rating) {
        this.userId = userId;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.rating=rating;
    }

    // Getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }


    // Getter and setter for commentText
    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    // Getter and setter for timestamp
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public float getRating() {
        return rating;
    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
