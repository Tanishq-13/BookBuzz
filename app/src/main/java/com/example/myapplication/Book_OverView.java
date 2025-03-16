package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.BookDetails.adapter.OverviewAdapter;
import com.example.myapplication.BookDetails.dataclass.Review;
import com.example.myapplication.BookDetails.dataclass.ReviewRequest;
import com.example.myapplication.apis.ApiService;
import com.example.myapplication.apis.Retrofitclient;
import com.example.myapplication.launch_page.Bkk;
import com.example.myapplication.launch_page.Book;
import com.google.firebase.firestore.auth.User;
import com.willy.ratingbar.ScaleRatingBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Book_OverView extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OverviewAdapter adapter;
    private EditText revh,revb;
    private Button prchs;
    private TextView booktitle,bookauth,field,semester,coursert,bookdesc,bookdetdesc;
    private ImageView img;
    private Book book;
    //temp variables for api testing
    private String studentName="Tanishq Gupta",email="22351@iiitu.ac.in";
    private int userId=102;

    private List<Review> reviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        book= (Book) getIntent().getSerializableExtra("book");
        booktitle=findViewById(R.id.courseTitle);
        img=findViewById(R.id.topImageView);
        bookauth=findViewById(R.id.bookauth);
        bookdetdesc=findViewById(R.id.courseDescription1);
        bookdesc=findViewById(R.id.courseDescription);
        field=findViewById(R.id.subjectName);
        prchs=findViewById(R.id.prchs);
        semester=findViewById(R.id.instructorName);
        revh=findViewById(R.id.review_heading);
        revb=findViewById(R.id.review_body);
        coursert=findViewById(R.id.courseRating);
        coursert.setText("⭐ "+Double.toString(book.getAverageRating())+" ("+String.valueOf(book.getNumberOfReviews())+") Ratings");
        bookdesc.setText(book.getSmallDesc());
        Log.d("ijj",book.getSmallDesc());
        bookdetdesc.setText(book.getDetailedDesc());
        booktitle.setText(book.getTitle());
        bookauth.setText(book.getAuthor());
        field.setText(book.getField());
        semester.setText(book.getSemester());
        int id=book.getId();
        Button addr=findViewById(R.id.addrev);
        addr.setOnClickListener(v -> addcomm());
        prchs.setOnClickListener(v -> downloadPdf(book.getPdfUrl()));
        recyclerView=findViewById(R.id.reviewsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchReviews(id);
    }
    private void downloadPdf(String pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No application to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchReviews(int bookId){
        ApiService apiService= Retrofitclient.getInstance().create(ApiService.class);
        Call<Bkk> call=apiService.getBookReviews(bookId);
        call.enqueue(new Callback<Bkk>() {
            @Override
            public void onResponse(Call<Bkk> call, Response<Bkk> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Bkk dt=response.body();
                    List<Review> reviews = dt.getReviews();
//                    ca = new OverviewAdapter(reviews);
                    adapter=new OverviewAdapter(reviews);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Book_OverView.this, "No reviews found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Bkk> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(Book_OverView.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addcomm(){
        ScaleRatingBar will=findViewById(R.id.simpleRatingBar);
        int rat= (int) will.getRating();
        String revbody=revb.getText().toString();
        String revheady=revh.getText().toString();
        if (revbody.isEmpty() || revheady.isEmpty()) {
            Toast.makeText(Book_OverView.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int bookID =book.getId();
        if (bookID == -1) {
            Toast.makeText(this, "Book ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewRequest review = new ReviewRequest(
                bookID,
                userId,
                revheady,
                revbody,
                rat,  // Default rating for now, you can replace with user input
                email,
                studentName,
                getCurrentTimestamp()
        );
        ApiService apiService = Retrofitclient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.postReview(bookID, review);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Book_OverView.this, "Review posted successfully", Toast.LENGTH_SHORT).show();
                    fetchReviews(bookID); // Refresh reviews
                } else {
                    Toast.makeText(Book_OverView.this, "Failed to post review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(Book_OverView.this, "Error posting review", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
}