package com.example.myapplication.apis;

import com.example.myapplication.BookDetails.dataclass.Review;
import com.example.myapplication.BookDetails.dataclass.ReviewRequest;
import com.example.myapplication.apis.requests.LoginRequest;
import com.example.myapplication.apis.requests.SignupRequest;
import com.example.myapplication.apis.response.SignupResponse;
import com.example.myapplication.launch_page.Bkk;
import com.example.myapplication.launch_page.Book;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Multipart
    @POST("books/upload")
    Call<Void> uploadBook(
            @Part MultipartBody.Part file,
            @Part("title") RequestBody title,
            @Part("author") RequestBody author,
            @Part("field") RequestBody field,
            @Part("isbn") RequestBody isbn,
            @Part("semester") RequestBody semester,
            @Part("smallDescription") RequestBody smallDescription,
            @Part("detailedDescription") RequestBody detailedDescription
    );
}
