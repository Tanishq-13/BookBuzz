package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.List;

public class addComment extends AppCompatActivity {
    private List<Comment> comments;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        EditText comment=findViewById(R.id.comment);
        EditText revT=findViewById(R.id.reviewTitle);
        btn=findViewById(R.id.add_btn);
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        TextView book_name=findViewById(R.id.book_name);
        TextView book_author=findViewById(R.id.book_author);

        RatingBar ratingBar = findViewById(R.id.ratingBar);


        DocumentReference bookDocRef = firestore.collection("books").document(documentId);
        bookDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve the book name from the document snapshot
                String bookName = documentSnapshot.getString("title");
                String bookAuthor = documentSnapshot.getString("author");

                book_name.setText(bookName);
                book_author.setText(bookAuthor);
            } else {
            }
        }).addOnFailureListener(e -> {
        });

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = currentUser.getUid();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp timestamp = Timestamp.now();
                // Define the behavior when the Button is clicked
                String text = comment.getText().toString();
                String rev=revT.getText().toString();
                float userRating = ratingBar.getRating();
                CollectionReference commentsRef = firestore.collection("books").document(documentId).collection("comments");
                Comment comments = new Comment(userId, text,timestamp,userRating,rev);
                commentsRef.add(comments)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Comment added successfully
                            }
                        });
                final String[] author = {""};
                Toast.makeText(addComment.this, author[0],Toast.LENGTH_LONG).show();
                String title="";
                String imageurl="";
                DocumentReference docRef = firestore.collection("books").document(documentId);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Object fieldValue = document.get("author");

                                String jkauthor=fieldValue.toString();
                                my_comments_data comments2 = new my_comments_data(userId, text,timestamp,userRating, jkauthor,document.get("title").toString(),document.get("imageurl").toString());
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                String userId = currentUser.getUid();
                                CollectionReference commentsRef2 = firestore.collection("users").document(userId).collection("added_comments");
                                commentsRef2.add(comments2)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // Comment added successfully
                                            }
                                        });
                            } else {

                            }
                        } else {
                        }
                    }
                });

                // Inside the source class
                Intent intent = new Intent(addComment.this, BookDetailsActivity.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);

                // You can add more actions here as needed
            }
        });
    }
    public String getSpecificField(String collectionName, String documentId, String fieldName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String[] h = {""};
        db.collection(collectionName).document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Access the specific field
                                Object fieldValue = document.get(fieldName);

                                // Perform operations with the field value
                                if (fieldValue != null) {
                                    h[0] = fieldValue.toString();
                                } else {
                                    Log.d("Firestore", fieldName + " does not exist in the document");
                                }
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        } else {
                            Log.d("Firestore", "get failed with ", task.getException());
                        }
                    }
                });
        return h[0];
    }
}