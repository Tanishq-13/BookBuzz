package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Book;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> bookList;
    private List<String> documentIds;
    private Context context;
    private OnItemClickListener listener;

    public BookAdapter(Context context, List<Book> bookList, List<String> documentIds) {
        this.context = context;
        this.bookList = bookList;
        this.documentIds = documentIds;
    }

    public interface OnItemClickListener {
        void onItemClick(String documentId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        String imageUrl = book.getImageURL();
        holder.bind(book, documentIds.get(position));

            holder.titleTextView.setText(book.getTitle());
            holder.authorTextView.setText(book.getAuthor());
            Picasso.get().load(imageUrl).into(holder.imageview);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private ImageView imageview;
        private TextView authorTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            authorTextView = itemView.findViewById(R.id.author);

            imageview=itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(documentIds.get(position));
                    }
                }
            });
        }

        public void bind(Book book, String documentId) {
            titleTextView.setText(book.getTitle());
        }
    }
}
