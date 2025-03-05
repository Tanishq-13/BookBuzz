package com.example.myapplication.apis;

import com.example.myapplication.BookDetails.dataclass.Review;
import com.example.myapplication.BookDetails.dataclass.ReviewRequest;
import com.example.myapplication.apis.requests.LoginRequest;
import com.example.myapplication.apis.requests.SignupRequest;
import com.example.myapplication.apis.response.SignupResponse;
import com.example.myapplication.launch_page.Bkk;
import com.example.myapplication.launch_page.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/v1/signup")
    Call<SignupResponse> signup(@Body SignupRequest signupRequest);

    @POST("auth/v1/login")
    Call<Response> login(@Body LoginRequest loginRequest);
    @GET("/books/{id}")
    Call<Bkk> getBookReviews(@Path("id") int bookId);

    @POST("/book/{book_id}/reviews")
    Call<Void> postReview(@Path("book_id") int bookId, @Body ReviewRequest reviewRequest);
}
