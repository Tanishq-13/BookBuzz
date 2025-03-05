package com.example.myapplication.BookDetails.dataclass;


public class Book {
    private String title;
    private int imageResource;
    private float rating;

    public Book(String title, int imageResource, float rating) {
        this.title = title;
        this.imageResource = imageResource;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public float getRating() {
        return rating;
    }
}
