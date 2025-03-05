package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.BookDetails.BookDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private List<String> documentIds;
    private String sem = null;
    Calendar calendar = Calendar.getInstance();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchpage);


        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        DocumentReference docRef = firestore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
                String email = documentSnapshot.getString("Email");
                sem=(calcSem(email,month,year));
                if (email != null) {
                    // Do something with the email
                    Log.d("UserEmail", "Email: " + email);
                } else {
                    Log.d("UserEmail", "Email not found");
                }
        }).addOnFailureListener(e -> {
            Log.e("UserEmail", "Error getting document: " + e.getMessage());
        });

        // Initialize RecyclerView, Adapter, and data lists
//        recyclerView = findViewById(R.id.recycle);
        bookList = new ArrayList<>();
        documentIds = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList, documentIds);

        // Set layout manager and adapter for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookAdapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();
        // Set item click listener
        bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String documentId) {
                // Handle item click here
                Intent intent = new Intent(MainActivity2.this, BookDetailsActivity.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });
    }

    private String calcSem(String email,int m,int y) {
        String t="";
        int c=2000+(email.charAt(0)-'0')*10+(email.charAt(1)-'0');
        int yr=y-c;
        int s;
        if(m>=6){
            s=yr*2+1;
        }
        else{
            s=yr*2;
        }
        t=Integer.toString(s);
        return t;
    }

    /*private void fetchDataFromFirestore() {
        // Assuming you have a "books" collection in Firestore
        firestore.collection("books")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Access each document here
                        String documentId = documentSnapshot.getId();
                        Book book = documentSnapshot.toObject(Book.class);
                        bookList.add(book);
                        documentIds.add(documentId);
                        // Process the document or add it to your list
                    }
                    bookAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(MainActivity2.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
    */
    private void fetchDataFromFirestore() {
        String email = UserUtil.getUserEmail();
        char firstChar = 0;
        if (email != null && email.length() > 0) {
            // Get the first character of the email
            firstChar = email.charAt(0);

            // Convert char to String
            String firstCharString = String.valueOf(firstChar);

            // Display the first character in a Toast
        }

    // Assuming you have a "books" collection in Firestore
        if(firstChar=='2') {
            sem=calcSem(email,month,year);
            firestore.collection("books")
                    // Filter books by semester
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Access each document here

                            String documentId = documentSnapshot.getId();
                            Book book = documentSnapshot.toObject(Book.class);
                            String semester = book.getSemester();   

                            if (book.getSemester() != null && semester.equals(sem)) {
                                // If the semester matches, add the book to the list
                                bookList.add(book);
                                documentIds.add(documentId);
                            }
                            // Process the document or add it to your list

                            // Process the document or add it to your list
                        }
                        bookAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(MainActivity2.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
        else{
            firestore.collection("books")
                    // Filter books by semester
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Access each document here

                            String documentId = documentSnapshot.getId();
                            Book book = documentSnapshot.toObject(Book.class);
                            String semester = book.getSemester();

                            if (book.getSemester() != null) {
                                // If the semester matches, add the book to the list
                                bookList.add(book);
                                documentIds.add(documentId);
                            }
                            // Process the document or add it to your list

                            // Process the document or add it to your list
                        }
                        bookAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(MainActivity2.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}