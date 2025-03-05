package com.example.myapplication.launch_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.BookDetails.BookDetailsActivity;
import com.example.myapplication.R;
import com.example.myapplication.launch_page.adapter.BookAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class launch_page extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private RecyclerView recyclerViewSemester, recyclerViewCSE, recyclerViewAll;
    private com.example.myapplication.launch_page.adapter.BookAdapter bookAdapterSemester, bookAdapterCSE, bookAdapterAll;
    private List<Book> booksSemester, booksCSE, booksAll;
    private String semester;
    private String field = "Computer Science";  // Change as per user field
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RequestQueue requestQueue;
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
        //get email from user table in mysql and calculate sem and branch
        requestQueue = Volley.newRequestQueue(this);

        recyclerViewSemester = findViewById(R.id.recyclerViewBooks);
        recyclerViewCSE = findViewById(R.id.recyclerViewBooks2);
        recyclerViewAll = findViewById(R.id.recyclerViewBooks21);

        booksSemester = new ArrayList<>();
        booksCSE = new ArrayList<>();
        booksAll = new ArrayList<>();

        bookAdapterSemester = new BookAdapter(booksSemester,this);
        bookAdapterCSE = new BookAdapter(booksCSE,this);
        bookAdapterAll = new BookAdapter(booksAll,this);

        recyclerViewSemester.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCSE.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAll.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewSemester.setAdapter(bookAdapterSemester);
        recyclerViewCSE.setAdapter(bookAdapterCSE);
        recyclerViewAll.setAdapter(bookAdapterAll);

        fetchBooks();
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

    private void fetchBooks() {
        String url = "http://10.22.2.208:9899/books/all";  // Change this to your API URL

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Book book = new Book(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("author"),
                                        jsonObject.optString("imageUrl", ""),
                                        jsonObject.getDouble("averageRating"),
                                        jsonObject.getInt("numberOfReviews"),
                                        jsonObject.getString("isbn"),
                                        jsonObject.getString("semester"),
                                        jsonObject.getString("field")
                                );
                                Log.d("check img",jsonObject.getString("imageUrl"));
                                booksAll.add(book);

                                if (book.getSemester().equals(semester)) {
                                    booksSemester.add(book);
                                }

                                if (book.getField().equals(field)) {
                                    booksCSE.add(book);
                                }
                            }

                            bookAdapterAll.notifyDataSetChanged();
                            bookAdapterSemester.notifyDataSetChanged();
                            bookAdapterCSE.notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e("FetchBooks", "Error parsing JSON", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchBooks", "Volley Error: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }
}
