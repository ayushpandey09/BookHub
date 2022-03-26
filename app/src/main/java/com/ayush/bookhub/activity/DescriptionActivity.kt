package com.ayush.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.bookhub.R
import com.ayush.bookhub.database.BookDatabase
import com.ayush.bookhub.database.BookEntity
import com.ayush.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    //declrntion
    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDecs: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
   //define
        txtBookName = findViewById(R.id.txtBookName)

        txtBookAuthor = findViewById(R.id.txtBookAuthor)

        txtBookPrice = findViewById(R.id.txtBookPrice)

        txtBookRating = findViewById(R.id.txtBookRating)

        txtBookDecs = findViewById(R.id.BookDesc)

        btnAddToFav = findViewById(R.id.BtnAddToFav)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        imgBookImage = findViewById(R.id.imgBookIcon)

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
            println("bookId = $bookId")

        } else {
            finish()
            Toast.makeText(this@DescriptionActivity, "Unexpected error", Toast.LENGTH_SHORT).show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(this@DescriptionActivity, "Unexpected error", Toast.LENGTH_SHORT).show()

        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    try {
                        println("response is $it")
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookjsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val bookImageUrl = bookjsonObject.getString("image")
                            txtBookName.text = bookjsonObject.getString("name")
                            txtBookAuthor.text = bookjsonObject.getString("author")
                            txtBookPrice.text = bookjsonObject.getString("price")
                            txtBookRating.text = bookjsonObject.getString("rating")
                            Picasso.get().load(bookjsonObject.getString("image")).into(imgBookImage)
                            txtBookDecs.text = bookjsonObject.getString("description")

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookDecs.text.toString(), bookImageUrl
                            )
                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                btnAddToFav.text = "Remove From Fav"
                                val favColor =
                                    ContextCompat.getColor(applicationContext, R.color.colorFav)
                                btnAddToFav.setBackgroundColor(favColor)
                            } else {
                                btnAddToFav.text = "Add to Fav"
                                val nofavColor = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorPrimary
                                )
                                btnAddToFav.setBackgroundColor(nofavColor)

                            }
                            btnAddToFav.setOnClickListener {
                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                        .get()
                                ) {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Added to Fav",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddToFav.text = "Remove From Fav"
                                        val favColor =
                                            ContextCompat.getColor(applicationContext, R.color.colorFav)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Error occured",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddToFav.text = "Add to Fav"
                                        val nofavColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorPrimary)
                                    }
                                } else { val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                        val res=async.get()
                                    if (res){ Toast.makeText(
                                        this@DescriptionActivity,
                                        "Removed from Fav",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                        btnAddToFav.text = "Add to Fav"
                                        val nofavColor =
                                            ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                        btnAddToFav.setBackgroundColor(nofavColor)

                                    }else{
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Error occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddToFav.text = "Remove From Fav"
                                        val favColor =
                                            ContextCompat.getColor(applicationContext, R.color.colorFav)
                                        btnAddToFav.setBackgroundColor(favColor)

                                    }
                                }
                            }


                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some error occurred !!!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some error occurred !!!",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                }, Response.ErrorListener
                {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some error occurred !!!",
                        Toast.LENGTH_SHORT
                    ).show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Contents-types"] = "application/json"
                        headers["token"] = "c85c8005f72418"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()

        }

    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        * */

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

                    // Check DB if the book is favourite or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null

                }

                2 -> {

                    // Save the book into DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }

                3 -> {

                    // Remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }
            }
            return false
        }

    }
}