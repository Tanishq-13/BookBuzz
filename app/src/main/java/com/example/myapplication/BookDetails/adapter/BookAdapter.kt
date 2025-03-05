package com.example.myapplication.BookDetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.BookDetails.dataclass.Book;

import com.example.myapplication.R

class BookAdapter(private val books: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.bookImage)
        val titleText: TextView = view.findViewById(R.id.bookTitle)
        val ratingText: TextView = view.findViewById(R.id.courseRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_launchpage, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.imageView.setImageResource(book.imageResource)
        holder.titleText.text = book.title
        holder.ratingText.text = "⭐ ${book.rating}"
    }

    override fun getItemCount() = books.size
}
