package com.example.myapplication.BookDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.BookDetails.adapter.BookAdapter;
import com.example.myapplication.BookDetails.dataclass.Book;
import com.example.myapplication.Book_OverView;
import com.example.myapplication.Comment;
import com.example.myapplication.MainActivity2;
import com.example.myapplication.R;
import com.example.myapplication.addComment;
import com.example.myapplication.commentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView title;
    private Button btn;

    private RecyclerView recyclerView;
    private commentAdapter ca;
    private FirebaseFirestore db;
    private CollectionReference commentsRef;
    private List<Comment> comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchpage);
        Intent intent = getIntent();
        Book book = intent.getParcelableExtra("book");
        String documentId = intent.getStringExtra("documentId");

        List<com.example.myapplication.BookDetails.dataclass.Book> books = Arrays.asList(
                new Book("Introduction to Algorithms", R.drawable.person_svgrepo_com, 4.5f),
                new Book("Data Structures", R.drawable.person_svgrepo_com, 4.2f),
                new Book("Operating Systems", R.drawable.person_svgrepo_com, 3.9f),
                new Book("Database Management", R.drawable.person_svgrepo_com, 4.1f),
                new Book("Machine Learning", R.drawable.person_svgrepo_com, 4.8f)
        );

        RecyclerView recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new BookAdapter(books));
    }

    @Override
    public void onBackPressed() {
        // Define your custom behavior here
        // For example, navigate to another activity when the back button is pressed
        super.onBackPressed();
        Intent intent = new Intent(BookDetailsActivity.this, MainActivity2.class);

        startActivity(intent);
        // Finish the current activity to prevent going back to it when pressing back again
        finish();
    }


}