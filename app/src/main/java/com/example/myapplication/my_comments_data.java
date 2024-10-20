package com.example.myapplication;

import com.google.firebase.Timestamp;
// Required public no-argument constructor
public class my_comments_data {

    private String userId;
    private String commentText;
    private Timestamp timestamp;
    private float rating;
    private String author;
    private String title;
    private String imageurl;


    // Required public no-argument constructor
    public my_comments_data() {
    }

    public my_comments_data(String userId, String commentText, Timestamp timestamp,float rating,String author,String title,String imageurl) {
        this.userId = userId;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.rating=rating;
        this.author=author;
        this.title=title;
        this.imageurl=imageurl;
    }

    // Getter and setter for userId
    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public String getImageurl() {
        return imageurl;
    }

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
