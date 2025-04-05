package com.example.myapplication.viewall;

import com.example.myapplication.launch_page.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private List<Book> allBooks = new ArrayList<>();

    private BookRepository() {}

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public void setBooks(List<Book> books) {
        this.allBooks = books;
    }

    public List<Book> getBooks() {
        return allBooks;
    }
}

