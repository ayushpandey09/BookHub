package com.ayush.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.ayush.bookhub.R
import com.ayush.bookhub.adapter.FavouriteRecyclerAdapter
import com.ayush.bookhub.database.BookDatabase
import com.ayush.bookhub.database.BookEntity

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouriteFragment : Fragment() {
    private lateinit var recyclerFavourite : RecyclerView
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    var dbBookList = listOf<BookEntity>()
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerFavourite = view.findViewById(R.id.recycleFav)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout =view.findViewById(R.id.progressLayout)
        layoutManager = GridLayoutManager(activity as Context, 2)


        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        if(activity !=null)
        {
            progressLayout.visibility=View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourite.adapter=recyclerAdapter
            recyclerFavourite.layoutManager=layoutManager
        }
        return view

            }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<BookEntity>>() {

        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

            return db.bookDao().getAllBooks()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of AsyncTask<Void ,Void,List<BookEntity>>()
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}