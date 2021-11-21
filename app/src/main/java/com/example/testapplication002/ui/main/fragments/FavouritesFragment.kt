package com.example.testapplication002.ui.main.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testapplication002.MainActivity
import com.example.testapplication002.R
import com.example.testapplication002.ui.main.adapters.FavouritesAdapter
import com.example.testapplication002.ui.main.viewModels.SharedViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class FavouritesFragment : Fragment() {

    lateinit var favList : ArrayList<String>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val prefs = activity?.getPreferences(MODE_PRIVATE)
        if(prefs?.getString("Favourites", "") == null) {
            favList = ArrayList()
        } else {
            val gson = Gson()
            val json: String = prefs.getString("Favourites", "")!!
            if (json.isEmpty()) {
                favList = ArrayList()
            } else {
                val type: Type = object : TypeToken<List<String?>?>() {}.getType()
                favList = gson.fromJson(json, type)
            }
        }
        val favouritesAdapter = FavouritesAdapter(favList) {
            resultsRedirectCallback(it)
        }
        recyclerView.adapter = favouritesAdapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        model.message.observe(viewLifecycleOwner, Observer {
            it?.let { it1 -> processEntryToFavourites(it1) }
        })
    }

    // Processes entry (add or remove) to the cache
    private fun processEntryToFavourites(it: String) {
        val data = it.split(" ")
        if (data[1] == "add" && !favList.contains(data[0]))
            favList.add(data[0])
        if(data[1] == "remove" && favList.contains(data[0]))
            favList.remove(data[0])
        recyclerView.adapter?.notifyDataSetChanged()
        updatePrefsForData()
    }

    // Favourites array is taken from the shared preferences, so we need to update it after each operation
    private fun updatePrefsForData() {
        val prefs = activity?.getPreferences(MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = (prefs?.edit() ?: null) as SharedPreferences.Editor
        val gson = Gson()
        val json = gson.toJson(favList)
        prefsEditor.putString("Favourites", json)
        prefsEditor.commit()
    }

    // callback to move the results fragment on the top
    private fun resultsRedirectCallback(input: String) {
        val parentActivity = this.activity as MainActivity
        val data = input.split("-")
        parentActivity.updateResultsFragment(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]))
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): FavouritesFragment {
            return FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}