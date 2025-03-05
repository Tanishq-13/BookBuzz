package com.example.myapplication;

public class Book {
    private String title;
    private String author;
    private String imageurl;
    private String semester;

    public Book() {
        // Required empty constructor for Firestore
    }

    public Book(String title, String author, String imageurl,String semester) {
        this.title = title;
        this.author = author;
        this.imageurl = imageurl;
        this.semester=semester;
    }
    public String getTitle() {
        return title;
    }
    public String getSemester() {
        return semester;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setSemester(String semester) {
        this.semester = semester;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageURL() {
        return imageurl;
    }

    public void setImageURL(String imageURL) {
        this.imageurl = imageURL;
    }

    // Getters and setters
}

