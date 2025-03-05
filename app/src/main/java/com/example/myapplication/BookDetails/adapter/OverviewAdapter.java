package com.example.myapplication.BookDetails.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.BookDetails.dataclass.Review;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {
    private List<Review> reviews;

    public OverviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.name.setText(review.getStudentName());
        holder.email.setText(review.getEmail());
        holder.reviewHeading.setText(review.getReviewHeading());
        holder.reviewdesc.setText(review.getReviewText());
        holder.ratingBar.setRating(review.getRating());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        holder.date.setText(sdf.format(review.getDate()));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, reviewHeading, reviewdesc, date;
        com.willy.ratingbar.ScaleRatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.namee);
            reviewHeading = itemView.findViewById(R.id.reviewHeading);
            reviewdesc = itemView.findViewById(R.id.reviewdesc);
            ratingBar = itemView.findViewById(R.id.simpleRatingBar);
            date = itemView.findViewById(R.id.date);
        }
    }
}
