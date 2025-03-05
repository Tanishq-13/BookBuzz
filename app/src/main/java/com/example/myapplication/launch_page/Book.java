package com.example.myapplication.launch_page;

import com.example.myapplication.BookDetails.dataclass.Review;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    private int id;
    private String title, author, imageUrl, isbn, semester, field;
    private double averageRating;
    private int numberOfReviews;
//    private List<Review> reviews;

    public Book(int id, String title, String author, String imageUrl, double averageRating, int numberOfReviews, String isbn, String semester, String field) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.averageRating = averageRating;
        this.numberOfReviews = numberOfReviews;
        this.isbn = isbn;
        this.semester = semester;
        this.field = field;
//        this.reviews=reviews;
    }

//    public List<Review> getReviews() {
//        return reviews;
//    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getImageUrl() { return imageUrl; }
    public double getAverageRating() { return averageRating; }
    public int getNumberOfReviews() { return numberOfReviews; }
    public String getIsbn() { return isbn; }
    public String getSemester() { return semester; }
    public String getField() { return field; }
}
