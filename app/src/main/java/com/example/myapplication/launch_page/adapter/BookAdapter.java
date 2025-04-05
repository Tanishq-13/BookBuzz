package com.example.myapplication.launch_page.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Book_OverView;
import com.example.myapplication.R;
import com.example.myapplication.launch_page.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    Context context;

    public BookAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context=context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_launchpage, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        holder.bookRating.setText(book.getAuthor());
//        Picasso.get()
//                .load(book.getImageUrl())
//                .into(holder.image);

        String rating = String.format("⭐ %.1f", book.getAverageRating());
        if(book.getAverageRating()!=0){
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText(rating+" ("+book.getNumberOfReviews()+")");
        }
        else{

        }
        Log.d("check img and rev",book.getImageUrl()+" "+book.getAverageRating());
        Glide.with(holder.itemView.getContext())
                .load(book.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform() // Avoids unnecessary transformations
                .dontAnimate() // Prevents animations that may cause issues
                .placeholder(R.drawable.book_svgrepo_com) // Temporary image while loading
                .error(R.drawable.book_svgrepo_com) // Shows error image if load fails
                .into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, Book_OverView.class);
            intent.putExtra("book_id", book.getId());
            intent.putExtra("book",book);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author,bookRating;
        ImageView image;

        public BookViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitle);
            author = itemView.findViewById(R.id.bookRating);
            image = itemView.findViewById(R.id.bookImage);
            bookRating=itemView.findViewById(R.id.bookauthor);
//            bookRating=itemView.findViewById(R.id.)
        }
    }
}
