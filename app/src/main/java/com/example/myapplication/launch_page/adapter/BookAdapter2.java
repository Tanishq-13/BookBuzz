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

public class BookAdapter2 extends RecyclerView.Adapter<BookAdapter2.BookViewHolder> {
    private List<Book> books;
    Context context;

    public BookAdapter2(List<Book> books, Context context) {
        this.books = books;
        this.context=context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        Picasso.get()
//                .load(book.getImageUrl())
//                .into(holder.image);
        holder.field.setText(book.getField());
        String rating = String.format("⭐ %.1f", book.getAverageRating());
        holder.bookRating.setText(rating+"("+book.getNumberOfReviews()+")");
        holder.author.setText(book.getAuthor());
        if(book.getAverageRating()==0){
            holder.bookRating.setVisibility(View.INVISIBLE);
        }
        else
            holder.bookRating.setText(rating+" ("+book.getNumberOfReviews()+")");
        Log.d("check img and rev",book.getImageUrl()+" "+book.getAuthor());
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
        TextView title, author,bookRating,field;
        ImageView image;

        public BookViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newslabel);
            author = itemView.findViewById(R.id.newssubject);
            image = itemView.findViewById(R.id.thumbnailnews);
            bookRating=itemView.findViewById(R.id.timeofnews);
            field=itemView.findViewById(R.id.categoryBadge);
//            bookRating=itemView.findViewById(R.id.)
        }
    }
}
