package com.example.testapplication002.ui.main.fragments

import android.arch.lifecycle.ViewModelProviders
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.testapplication002.R
import com.example.testapplication002.ui.main.viewModels.ResultsPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.Calendar


object constants {
    public val URL_SCHEME = "https"
    public val URL_AUTHORITY ="api.nasa.gov"
    public val PATH_PLANETARY = "planetary"
    public val PATH_APOD = "apod"
    public val API_KEY_KEY = "api_key"
    public val API_KEY_VALUE = "zyfcpeL8hF4k7PdZSkssdi7usDuBjEIjkKhAbMaY"
    public val DATE_KEY = "date"
}

class ResultsFragment : Fragment() {

    private lateinit var pageViewModel: ResultsPageViewModel
    val client = OkHttpClient()
    private lateinit var headerTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var explanationView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentCalenderInstance = Calendar.getInstance()
        pageViewModel = ViewModelProviders.of(this).get(ResultsPageViewModel::class.java).apply {
            setDateComponents(
                arguments?.getInt(ARG_YEAR) ?: currentCalenderInstance.get(Calendar.YEAR),
                arguments?.getInt(ARG_MONTH) ?: currentCalenderInstance.get(Calendar.MONTH),
                arguments?.getInt(ARG_MONTH) ?: currentCalenderInstance.get(Calendar.DAY_OF_MONTH)
            )
        }
        callApiForResponse()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_results, container, false)
        headerTextView = root.findViewById(R.id.title_label)
        dateTextView = root.findViewById(R.id.date_label)
        imageView = root.findViewById(R.id.image_view)
        explanationView = root.findViewById(R.id.explanation_label)
        return root
    }

    fun callApiForResponse() {
        val builder = Uri.Builder()
        builder.scheme(constants.URL_SCHEME)
            .authority(constants.URL_AUTHORITY)
            .appendPath(constants.PATH_PLANETARY)
            .appendPath(constants.PATH_APOD)
            .appendQueryParameter(constants.API_KEY_KEY, constants.API_KEY_VALUE)
            .appendQueryParameter(constants.DATE_KEY, "2018-11-19")

        CoroutineScope(Dispatchers.IO).launch{
            val request = Request.Builder()
                .url(builder.build().toString())
                .method("GET", null)
                .build()
            val response = client.newCall(request).execute()
            val responseString = response.body()?.string()
            if(response.isSuccessful) {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = JSONObject(responseString)
                    headerTextView.text = result.getString("title")
                    dateTextView.text = result.getString("date")
                    updateBitmapFromURL(result.getString("url"), imageView);
                    explanationView.text = result.getString("explanation")
                }
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_YEAR = "Year"
        private const val ARG_MONTH = "Month"
        private const val ARG_DAY = "Day"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(year: Int, month: Int, day: Int): ResultsFragment {
            return ResultsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_YEAR, year)
                    putInt(ARG_MONTH, month)
                    putInt(ARG_DAY, day)
                }
            }
        }

        @JvmStatic
        fun updateBitmapFromURL(src: String, imageView: ImageView) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val input = java.net.URL(src).openStream()
                    val image = BitmapFactory.decodeStream(input)
                    CoroutineScope(Dispatchers.Main).launch {
                        imageView.setImageBitmap(image)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("Exception", e.message.toString())
                }
            }
        }
    }
}