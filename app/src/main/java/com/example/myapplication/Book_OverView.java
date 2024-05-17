package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class Book_OverView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private overviewAdapter ca;

    private List<OverView> comments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_over_view);
        String usermail= UserUtil.getUserEmail();
        Button m=findViewById(R.id.button6);
        Intent intent = getIntent();

        String documentId = intent.getStringExtra("documentId");

        m.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Book_OverView.this, addOverview.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });
        if(usermail.charAt(0)!='2'){
            m.setVisibility(View.VISIBLE);
        }
        else{
            m.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        comments = new ArrayList<>();

        ca = new overviewAdapter(documentId);
        recyclerView.setAdapter(ca);

    }
}