package com.example.testapplication002.ui.main.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.testapplication002.R


class FavouritesAdapter(private val list: List<String>, private val redirectCallback: (input: String) ->Unit?) :
    RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_cell_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val positionString = list[position]
        holder.textView.text = positionString
        holder.textView.setOnClickListener { view ->
            redirectCallback((view as TextView).text.toString())
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.favourites_cell_view)
    }
}