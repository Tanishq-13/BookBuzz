package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewUserName;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private List<String> documentIds;
    private String semm = null;
    Calendar calendar = Calendar.getInstance();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firestore = FirebaseFirestore.getInstance();


        // Initialize FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();
        getUserName(userId);

        DocumentReference docRef = firestore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            String email = documentSnapshot.getString("Email");
            semm = calcSem(email, month, year);
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
        textViewUserName = view.findViewById(R.id.welcome_msg);
        recyclerView = view.findViewById(R.id.recycle);
        bookList = new ArrayList<>();
        documentIds = new ArrayList<>();
        bookAdapter = new BookAdapter(getContext(), bookList, documentIds);

        // Set layout manager and adapter for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bookAdapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();

        // Set item click listener
        bookAdapter.setOnItemClickListener(documentId -> {
            // Handle item click here
            Intent intent = new Intent(getContext(), BookDetailsActivity.class);
            intent.putExtra("documentId", documentId);
            startActivity(intent);
        });

        return view;
    }

        private void getUserName(String uid) {
            firestore.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userName = document.getString("Name");

                            textViewUserName.setText("Welcome back "+"\n"+userName+"\nLets discover some new genre!");
                        } else {
                            textViewUserName.setText("User not found");
                        }
                    } else {
                    }
                }
            });
        }


    private String calcSem(String email, int m, int y) {
        String t = "";
        int c = 2000 + (email.charAt(0) - '0') * 10 + (email.charAt(1) - '0');
        int yr = y - c;
        int s;
        if (m >= 6) {
            s = yr * 2 + 1;
        } else {
            s = yr * 2;
        }
        t = Integer.toString(s);
        return t;
    }

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
            semm=calcSem(email,month,year);
            firestore.collection("books")
                    // Filter books by semester
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Access each document here

                            String documentId = documentSnapshot.getId();
                            Book book = documentSnapshot.toObject(Book.class);
                            String semester = book.getSemester();

                            if (book.getSemester() != null && semester.equals(semm)) {
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
                    });
        }
    }
}
