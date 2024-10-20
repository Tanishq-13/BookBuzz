package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private TextView toolbarTitle;
    private TextView authorview;

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
        setContentView(R.layout.activity_book_details);
        Intent intent = getIntent();
        Book book = intent.getParcelableExtra("book");
        String documentId = intent.getStringExtra("documentId");

        imageView = findViewById(R.id.book_image);
        title = findViewById(R.id.book_title);
        authorview=findViewById(R.id.author);
        btn=findViewById(R.id.button2);
        Button overview=findViewById(R.id.button5);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        // Call the method to get the currently logged-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        commentsRef = db.collection("comments");
        db.collection("books").document(documentId).collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Double> ratings = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Double rating = document.getDouble("rating");
                        if (rating != null) {
                            ratings.add(rating);
                        }
                    }
                    // Calculate the average rating and review count
                    int reviewCount = ratings.size();
                    double sum = 0;
                    for (double rating : ratings) {
                        sum += rating;
                    }
                    double averageRating = reviewCount > 0 ? sum / reviewCount : 0;

                    // Update the UI with the average rating and review count
                    TextView ratingTextView = findViewById(R.id.average_rating_text);
                    TextView reviewCountTextView = findViewById(R.id.review_count_text);

                    ratingTextView.setText(String.format("%.1f ★", averageRating));
                    reviewCountTextView.setText(reviewCount + " Reviews");


                } else {
                    // Handle the error
                    Log.d("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });
            // Get the user ID of the currently logged-in user
        String userId = currentUser.getUid();
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Remove default title to use custom TextView
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        fetchBookDetails(documentId);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.comments_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        comments = new ArrayList<>();

        ca = new commentAdapter(documentId);
        recyclerView.setAdapter(ca);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("books").document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title1 = documentSnapshot.getString("title");
                        String imageUrl = documentSnapshot.getString("imageurl");
                        title.setText(title1);
                        Picasso.get().load(imageUrl).into(imageView);


                        // Fetch comments data from Firestore
                        // Now you have the author information
                    } else {
                        // Document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsActivity.this, addComment.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });
        overview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailsActivity.this, Book_OverView.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });
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
    private void fetchBookDetails(String documentId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference bookRef = db.collection("books").document(documentId);
        bookRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String title = documentSnapshot.getString("title");
                String author = documentSnapshot.getString("author");
                String imageUrl = documentSnapshot.getString("imageURL");

                if (title != null) {
                    toolbarTitle.setText(title);
                }
                authorview.setText("By "+author);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}