package com.example.myapplication.BookDetails.dataclass;

import java.util.Date;


public class Review {

    private String id;
    private String userId;
    private String studentName;
    private String reviewHeading;
    private String email;
    private String reviewText;
    private int rating;
    private Date date;

    // Default constructor (required for Firestore)
    public Review() {
    }

    // Parameterized constructor
    public Review(String id,String userId, String studentName,String reviewHeading, String reviewText, int rating, Date date,String email) {
        this.id= id;
        this.userId=userId;
        this.studentName = studentName;
        this.reviewHeading=reviewHeading;
        this.reviewText=reviewText;
        this.rating = rating;
        this.date = date;
        this.email=email;
    }

    // Getters and setters
//    public String getDocId() {
//        return id;
//    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getReviewHeading() {
        return reviewHeading;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getUserId() {
        return userId;
    }


    public void setDocId(String docId) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setReviewHeading(String reviewHeading) {
        this.reviewHeading = reviewHeading;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // toString() for debugging purposes
//    @Override
//    public String toString() {
//        return "Review{" +
//                "docId='" + docId + '\'' +
//                ", studentName='" + studentName + '\'' +
//                ", studentReview='" + studentReview + '\'' +
//                ", rating=" + rating +
//                ", date=" + date +
//                '}';
//    }
}
