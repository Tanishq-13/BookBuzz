package com.example.myapplication.launch_page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.launch_page.adapter.BookAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class launch_page extends Fragment {

    private RecyclerView recyclerViewSemester, recyclerViewCSE, recyclerViewAll;
    private BookAdapter bookAdapterSemester, bookAdapterCSE, bookAdapterAll;
    private List<Book> booksSemester, booksCSE, booksAll;
    private String semester;
    private String field = "Computer Science";  // Change as per user field
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RequestQueue requestQueue;
    private String sem = null;
    Calendar calendar = Calendar.getInstance();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launchhome, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());

        recyclerViewSemester = view.findViewById(R.id.recyclerViewBooks);
        recyclerViewCSE = view.findViewById(R.id.recyclerViewBooks2);
        recyclerViewAll = view.findViewById(R.id.recyclerViewBooks21);

        booksSemester = new ArrayList<>();
        booksCSE = new ArrayList<>();
        booksAll = new ArrayList<>();

        bookAdapterSemester = new BookAdapter(booksSemester, getContext());
        bookAdapterCSE = new BookAdapter(booksCSE, getContext());
        bookAdapterAll = new BookAdapter(booksAll, getContext());

        recyclerViewSemester.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCSE.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAll.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerViewSemester.setAdapter(bookAdapterSemester);
        recyclerViewCSE.setAdapter(bookAdapterCSE);
        recyclerViewAll.setAdapter(bookAdapterAll);

        fetchBooks();

        return view;
    }

    private String calcSem(String email, int m, int y) {
        int c = 2000 + (email.charAt(0) - '0') * 10 + (email.charAt(1) - '0');
        int yr = y - c;
        int s = (m >= 6) ? yr * 2 + 1 : yr * 2;
        return Integer.toString(s);
    }

    private void fetchBooks() {
        String url = "http://192.168.29.159:9899/books/all";
        // Change this to your API URL

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Book book = new Book(
                                        jsonObject.optInt("id", -1),  // Assuming ID should not be null, using -1 as a fallback
                                        jsonObject.optString("title", null),
                                        jsonObject.optString("author", null),
                                        jsonObject.optString("imageUrl", null),
                                        jsonObject.has("averageRating") ? jsonObject.optDouble("averageRating") : null,
                                        jsonObject.has("numberOfReviews") ? jsonObject.optInt("numberOfReviews") : null,
                                        jsonObject.optString("isbn", null),
                                        jsonObject.optString("semester", null),
                                        jsonObject.optString("field", null),
                                        jsonObject.optString("detailedDescription", null),
                                        jsonObject.optString("smallDescription", null),
                                        jsonObject.optString("pdfUrl", null)
                                );
                                Log.d("JSON_RESPONSE", jsonObject.toString());

                                Log.d("check img", jsonObject.getString("imageUrl"));
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
                        // Log network response details if available
                        if (error.networkResponse != null) {
                            String responseBody = new String(error.networkResponse.data);
                            Log.e("FetchBooks", "Response Code: " + error.networkResponse.statusCode);
                            Log.e("FetchBooks", "Response Data: " + responseBody);
                        } else {
                            // Log a more general error message if no response is available
                            Log.e("FetchBooks", "Error: " + error.toString());
                        }
                    }
                }
        );


                requestQueue.add(jsonArrayRequest);
    }
}