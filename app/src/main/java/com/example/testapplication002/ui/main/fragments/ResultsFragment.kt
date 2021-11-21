package com.example.testapplication002.ui.main.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.testapplication002.R
import com.example.testapplication002.ui.main.utils.CacheHelper
import com.example.testapplication002.ui.main.viewModels.ResultsPageViewModel
import com.example.testapplication002.ui.main.viewModels.SharedViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


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
    private lateinit var sharedViewModel: SharedViewModel
    private val client = OkHttpClient()
    private lateinit var cache : Cache

    private lateinit var headerTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var explanationView: TextView
    private lateinit var optionsLayout: LinearLayout
    private lateinit var favourites_toggle: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentCalenderInstance = Calendar.getInstance()
        pageViewModel = ViewModelProviders.of(this).get(ResultsPageViewModel::class.java).apply {
            setDateComponents(
                arguments?.getInt(ARG_YEAR) ?: currentCalenderInstance.get(Calendar.YEAR),
                arguments?.getInt(ARG_MONTH) ?: currentCalenderInstance.get(Calendar.MONTH),
                arguments?.getInt(ARG_DAY) ?: currentCalenderInstance.get(Calendar.DAY_OF_MONTH)
            )
        }
        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        checkCacheAndcallApiForResponse()
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
        optionsLayout = root.findViewById(R.id.options_layout)
        favourites_toggle = root.findViewById(R.id.favourites_toggle)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favourites_toggle.setOnCheckedChangeListener{ _ , isChecked ->
            if (isChecked) {
                sharedViewModel.process((dateTextView.text as String) + " " + "add")
            } else {
                sharedViewModel.process((dateTextView.text as String)  + " " + "remove")
            }
        }
    }

    fun updateViewModel(year: Int, month: Int, day: Int) {
        pageViewModel.setDateComponents(year, month, day)
        clearUIForDataUpdate()
        checkCacheAndcallApiForResponse()
    }

    private fun checkCacheAndcallApiForResponse() {
        val responseString = CacheHelper.retrieveData(context!!, pageViewModel.getDateFromComponents())
        if (responseString.isNullOrEmpty()) {
            callApiForResponse()
        } else {
            val result = JSONObject(responseString)
            context?.let { CacheHelper.saveData(it, "lastDate", result.getString("date")) }
            CoroutineScope(Dispatchers.IO).launch {
                updateUi(result.getString("title"),
                    result.getString("date"),
                    extractBitmapFromURL(result.getString("url"), context!!),
                    result.getString("explanation"))
            }
        }
    }

    private fun callApiForResponse() {
        val builder = Uri.Builder()
        builder.scheme(constants.URL_SCHEME)
            .authority(constants.URL_AUTHORITY)
            .appendPath(constants.PATH_PLANETARY)
            .appendPath(constants.PATH_APOD)
            .appendQueryParameter(constants.API_KEY_KEY, constants.API_KEY_VALUE)
            .appendQueryParameter(constants.DATE_KEY, pageViewModel.getDateFromComponents())

        CoroutineScope(Dispatchers.IO).launch{
            val request = Request.Builder()
                .url(builder.build().toString())
                .build()

            val response = client.newCall(request).execute()
            val responseString = response.body()?.string()
            if(response.isSuccessful) {
                val result = JSONObject(responseString)
                context?.let { CacheHelper.saveData(it, result.getString("date"), responseString) }
                context?.let { CacheHelper.saveData(it, "lastDate", result.getString("date")) }
                updateUi(result.getString("title"),
                    result.getString("date"),
                    extractBitmapFromURL(result.getString("url"), context!!),
                    result.getString("explanation"))
            }
        }
    }

    private fun updateUi(title: String, date: String, bitmap: Bitmap?, explanation: String) {
        CoroutineScope(Dispatchers.Main).launch {
            optionsLayout.visibility = View.VISIBLE
            headerTextView.text = title
            dateTextView.text = date
            explanationView.text = explanation
            imageView.setImageBitmap(bitmap)
            updateFavouritesState()
        }
    }

    private fun clearUIForDataUpdate() {
        headerTextView.text = "Loading..."
        imageView.setImageResource(R.drawable.ic_launcher_background)
        dateTextView.text = ""
        explanationView.text = ""
        favourites_toggle.isChecked = false
        optionsLayout.visibility = View.GONE
    }

    private fun updateFavouritesState() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val gson = Gson()
        if(prefs?.contains("Favourites") == true) {
            val json: String = prefs?.getString("Favourites", "")!!
            if (json.isNotEmpty()) {
                val type: Type = object : TypeToken<List<String?>?>() {}.getType()
                val favList: ArrayList<String> = gson.fromJson(json, type)
                favourites_toggle.isChecked = favList.contains(dateTextView.text)
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
        fun extractBitmapFromURL(src: String, context: Context): Bitmap? {
            return CacheHelper.retrieveImage(context, src)
                ?: try {
                    val input = java.net.URL(src).openStream()
                    val image = BitmapFactory.decodeStream(input)
                    CacheHelper.saveImage(context, src, image)
                    image
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("Exception", e.message.toString())
                    null
                }
        }
    }
}