package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class addOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_overview);
        Button btn=findViewById(R.id.add_btn);
        EditText comment=findViewById(R.id.comment);
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        TextView book_name=findViewById(R.id.book_name);
        TextView book_author=findViewById(R.id.book_author);



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
                CollectionReference commentsRef = firestore.collection("books").document(documentId).collection("overview");
                OverView comments = new OverView(userId, text,timestamp);
                commentsRef.add(comments)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Comment added successfully
                            }
                        });
                // Inside the source class
                Intent intent = new Intent(addOverview.this, Book_OverView.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);

                // You can add more actions here as needed
            }
        });
    }
}