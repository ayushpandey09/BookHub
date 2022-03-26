package com.ayush.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data  class BookEntity (
    @PrimaryKey val book_id:Int,
    @ColumnInfo(name = "Book_name") val book_name:String,
    @ColumnInfo(name = "Book_author") val book_author:String,
    @ColumnInfo(name = "Book_ratings") val book_Rating:String,
    @ColumnInfo(name = "Book_Price") val book_price:String,
   @ColumnInfo(name = "Book_image") val book_Image:String
)