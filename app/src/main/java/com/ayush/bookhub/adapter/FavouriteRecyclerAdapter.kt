package com.ayush.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ayush.bookhub.R
import com.ayush.bookhub.activity.DescriptionActivity
import com.ayush.bookhub.database.BookEntity
import com.ayush.bookhub.model.Book
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, private val bookList: List<BookEntity>) :
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: LinearLayout = view.findViewById(R.id.llFavContent)
        val txtBookName: TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        val imgBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single_row, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(
        holder: FavouriteViewHolder,
        position: Int
    ) {
        val book = bookList[position]

        holder.txtBookName.text = book.book_name
        holder.txtBookAuthor.text = book.book_author
        holder.txtBookPrice.text = book.book_price
        holder.txtBookRating.text = book.book_Rating
        Picasso.get().load(book.book_Image).into(holder.imgBookImage)
        holder.llContent.setOnClickListener {
            val intent = Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.book_id.toString())
            context.startActivity(intent)

        }
    }


}