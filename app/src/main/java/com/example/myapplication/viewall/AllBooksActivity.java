package com.example.myapplication.viewall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.launch_page.Book;
import com.example.myapplication.launch_page.adapter.BookAdapter2;

import java.util.List;
import java.util.stream.Collectors;

public class AllBooksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter2 bookAdapter;
    private List<Book> allBooks;
    private List<Book> filteredBooks;
    String fieldFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        boolean showAllBooks = intent.getBooleanExtra("showAllBooks", false);
        fieldFilter = intent.getStringExtra("showSpecificField");
//        fieldFilter=fieldFilter.toLowerCase();
        if (showAllBooks) {
            allBooks = BookRepository.getInstance().getBooks();
            Log.d("AllBooksActivity", "Books fetched: " + allBooks.size());
            bookAdapter = new BookAdapter2(allBooks,this);
            recyclerView.setAdapter(bookAdapter);
        }
        else if (fieldFilter != null) {
            for (Book book : BookRepository.getInstance().getBooks()) {
                Log.d("AllBooks", "Title: " + book.getTitle() + " | Field: " + book.getField());
            }

            filteredBooks = BookRepository.getInstance().getBooks()
                    .stream()
                    .filter(book -> fieldFilter.equals(book.getField()))
                    .collect(Collectors.toList());
            bookAdapter = new BookAdapter2(filteredBooks,this);
            recyclerView.setAdapter(bookAdapter);
        }
//        Log.d("AllBooksActivity", "Books fetched: " + filteredBooks.size());
        ;
    }
}
