package com.example.myapplication.BookDetails.dataclass;


public class ReviewRequest {
    private int bookID;
    private int userId;
    private String reviewHeading;
    private String reviewText;
    private int rating;
    private String email;
    private String studentName;
    private String date;

    public ReviewRequest(int bookID, int userId, String reviewHeading, String reviewText, int rating, String email, String studentName, String date) {
        this.bookID = bookID;
        this.userId = userId;
        this.reviewHeading = reviewHeading;
        this.reviewText = reviewText;
        this.rating = rating;
        this.email = email;
        this.studentName = studentName;
        this.date = date;
    }

    public int getBookID() { return bookID; }
    public int getUserId() { return userId; }
    public String getReviewHeading() { return reviewHeading; }
    public String getReviewText() { return reviewText; }
    public int getRating() { return rating; }
    public String getEmail() { return email; }
    public String getStudentName() { return studentName; }
    public String getDate() { return date; }
}
