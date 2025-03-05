package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Comment;
import com.example.myapplication.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public commentAdapter(String bookId) {
        comments = new ArrayList<>();
        loadCommentsFromFirestore(bookId);
    }

    // Load comments from Firestore
    private void loadCommentsFromFirestore(String bookId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference commentsRef = db.collection("books").document(bookId).collection("comments");
        commentsRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Comment comment = document.toObject(Comment.class);
                            comments.add(comment);
                        }
                        notifyDataSetChanged();
                    } else {
                        // Handle errors
                    }
                });
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_disp, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserId;
        private TextView textViewCommentText;
        private TextView textViewTimestamp;
        private RatingBar ratingBar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserId = itemView.findViewById(R.id.name);
            textViewCommentText = itemView.findViewById(R.id.comment);
            textViewTimestamp = itemView.findViewById(R.id.time);
            ratingBar=itemView.findViewById(R.id.ratingBar);
        }

        public void bind(Comment comment) {
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(comment.getUserId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("Name");
                            textViewUserId.setText(userName);
                        } else {
                            textViewUserId.setText("User not found");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        textViewUserId.setText("Failed to retrieve user");
                    });
            textViewCommentText.setText(comment.getCommentText());
            ratingBar.setRating(comment.getRating());
            if (comment.getTimestamp() != null) {
                Timestamp timestamp = comment.getTimestamp();
                Date date = timestamp.toDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy , HH:mm", Locale.getDefault());
                String formattedDate = dateFormat.format(date);
                textViewTimestamp.setText(formattedDate);

            } else {
                textViewTimestamp.setText("Timestamp not available");
            }
        }
    }
}
