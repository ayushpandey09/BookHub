package com.ayush.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.VoiceInteractor
import android.content.Context
import android.content.Intent
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.view.textclassifier.TextLanguage
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ayush.bookhub.R
import com.ayush.bookhub.adapter.DashboardRecyclerAdapter
import com.ayush.bookhub.model.Book
import com.ayush.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var recycleDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    val bookInfo = arrayListOf<Book>()

    var ratingComparator = Comparator<Book> { book1, book2 ->
        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            book1.bookNamed.compareTo(book2.bookNamed, true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }


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
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recycleDashboard = view.findViewById(R.id.recycleDashboard)

        layoutManager = LinearLayoutManager(activity)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    println("Response is $it")
                    try {
                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfo.add(bookObject)
                                recyclerAdapter =
                                    DashboardRecyclerAdapter(activity as Context, bookInfo)
                                recycleDashboard.adapter = recyclerAdapter
                                recycleDashboard.layoutManager = layoutManager

                            }

                        } else (
                                Toast.makeText(
                                    activity as Context,
                                    "Some error occurred !!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                )
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occured!!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "c85c8005f72418"
                        return headers
                    }

                }
            queue.add(jsonObjectRequest)

        } else {

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.action_sort) {
            Collections.sort(bookInfo, ratingComparator)
            bookInfo.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}