package com.example.myapplication.launch_page;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.auth0.android.jwt.JWT;
import com.example.myapplication.R;
import com.example.myapplication.apis.ApiClient;
import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.Retrofitclient;
import com.example.myapplication.apis.response.user_details;
import com.example.myapplication.launch_page.adapter.BookAdapter;
import com.example.myapplication.token.TokenManager;
import com.example.myapplication.viewall.AllBooksActivity;
import com.example.myapplication.viewall.BookRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class launch_page extends Fragment {

    private RecyclerView recyclerViewSemester, recyclerViewCSE, recyclerViewAll;
    private BookAdapter bookAdapterSemester, bookAdapterCSE, bookAdapterAll;
    private List<Book> booksSemester, booksCSE, booksAll;
    LinearLayout ns,crs,clc;
    private String semester;
    private String username;
    private String email="22351@iiitu.ac.in";
    private String field = "Computer Science";  // Change as per user field
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RequestQueue requestQueue;
    private String btechyear = "3";
    private TextView vwAllSem,vwAllField,vwAll,desc1;

    Calendar calendar = Calendar.getInstance();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1; // Month is zero-based, so add 1
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launchhome, container, false);
        extractUserInfoFromToken();
        requestQueue = Volley.newRequestQueue(requireContext());
        getName();
        recyclerViewSemester = view.findViewById(R.id.recyclerViewBooks);
        recyclerViewCSE = view.findViewById(R.id.recyclerViewBooks2);
        recyclerViewAll = view.findViewById(R.id.recyclerViewBooks21);
        vwAll=view.findViewById(R.id.Desc212);
        vwAllField=view.findViewById(R.id.Desc22);
        fetchBooks();
        desc1=view.findViewById(R.id.Desc1);
        if(email.charAt(2)=='1'){
            desc1.setText("CSE TextBooks");
        }
        else if(email.charAt(2)=='3'){
            desc1.setText("IT TextBooks");
        }
        else{
            desc1.setText("ECE TextBooks");
        }
        email=email.trim();
        if(email.endsWith("iiitu.ac.in")){
            if(email.charAt(2)=='1' || email.charAt(2)=='3'){
                field="School Of Computing";
                Log.d("checklgg",email);
            }
            else{
                field="School of Electronics";
            }
        }
        semester=calcSem(email);
        Log.d("checklgg",semester);

        vwAll.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllBooksActivity.class);
            intent.putExtra("showAllBooks", true);
            startActivity(intent);
        });

        vwAllField.setOnClickListener(v->{
            Intent intent=new Intent(getActivity(),AllBooksActivity.class);
            intent.putExtra("showSpecificField",field);
            startActivity(intent);
        });
        ns=view.findViewById(R.id.newss);
        ns.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(),AllBooksActivity.class);
            intent.putExtra("showSpecificField","School of Computing");
            startActivity(intent);
        });

        crs=view.findViewById(R.id.coursess);
//        crs.setOnClickListener(v -> {
//            Intent intent=new
//        });

        clc=view.findViewById(R.id.calci);
        clc.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllBooksActivity.class);
            intent.putExtra("showAllBooks", true);
            startActivity(intent);
        });

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


        return view;
    }

    private String calcSem(String email) {
        LocalDate currentDate = LocalDate.now();  // Get current date
        int m = currentDate.getMonthValue();      // Extract month (1-12)
        int y = currentDate.getYear();
        int c = 2000 + (email.charAt(0) - '0') * 10 + (email.charAt(1) - '0');
        int yr = y - c;
        int s = (m >= 6) ? yr * 2 + 1 : yr * 2;
        return Integer.toString(s);
    }

    private void fetchBooks() {
        String url = "http://10.22.1.63:9899/books/all";
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
                                BookRepository.getInstance().setBooks(booksAll);

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
    private void extractUserInfoFromToken() {
        TokenManager tokenManager = new TokenManager(requireContext());
        String accessToken = tokenManager.getAccessToken(); // Fetch stored token

        if (accessToken != null) {
            try {
                JWT jwt = new JWT(accessToken);
                username = jwt.getClaim("sub").asString();  // Extract "sub" as username
                email=username;
                Log.d("TokenInfo", "Username: " + username);
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();
            } catch (Exception e) {
                Log.e("TokenError", "Error decoding token: ", e);
            }
        } else {
            Log.e("TokenError", "No token found!");
        }
    }
    private void getName(){
        ApiService apiService= ApiClient.getClient().create(ApiService.class);
        Call<user_details> call=apiService.getUserDetails(username.trim());
        Log.d("userName",username);
        call.enqueue(new Callback<user_details>() {
            @Override
            public void onResponse(Call<user_details> call, retrofit2.Response<user_details> response) {
                if(response.isSuccessful()){
                    user_details userDetails=response.body();
                    String firstName=userDetails.getFirstName();
                    String lastName=userDetails.getLastName();
                    SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("firstName", firstName);
                    editor.putString("lastName", lastName);
                    editor.apply();
                    Log.d("UserInfo", "Name stored: " + firstName + " " + lastName);
                }
                else{
                    try {
                        String errorResponse = response.errorBody().string();
                        Log.e("UserInfo", "Failed to fetch user details. Response Code: " + response.code());
                        Log.e("UserInfo", "Response body: " + errorResponse);
                    } catch (Exception e) {
                        Log.e("UserInfo", "Error reading errorBody", e);
                    }

                }
            }

            @Override
            public void onFailure(Call<user_details> call, Throwable throwable) {
                Log.e("UserInfo", "API call failed: " + throwable.getMessage());
            }
        });
    }
}