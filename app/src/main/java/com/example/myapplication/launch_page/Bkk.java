package com.example.myapplication.launch_page;

import com.example.myapplication.BookDetails.dataclass.Review;

import java.io.Serializable;
import java.util.List;

public class Bkk implements Serializable {
    private int id;
    private String title;
    private String author;
    private String imageUrl;
    private double averageRating;
    private int numberOfReviews;
    private String isbn;
    private String semester;
    private String field;
    private List<Review> reviews; // Add this
    public Bkk(int id, String title, String author, String imageUrl, double averageRating, int numberOfReviews, String isbn, String semester, String field,List<Review> reviews) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.averageRating = averageRating;
        this.numberOfReviews = numberOfReviews;
        this.isbn = isbn;
        this.semester = semester;
        this.field = field;
        this.reviews=reviews;
    }
    public List<Review> getReviews() {
        return reviews;
    }

}

