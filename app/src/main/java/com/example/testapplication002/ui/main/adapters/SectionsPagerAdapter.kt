package com.example.testapplication002.ui.main.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.testapplication002.R
import com.example.testapplication002.ui.main.fragments.FavouritesFragment
import com.example.testapplication002.ui.main.fragments.ResultsFragment
import java.util.*

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    private var currentSetYear: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var currentSetMonth: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var currentSetDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 1)
            return FavouritesFragment.newInstance(position + 1)
        else
            return ResultsFragment.newInstance(currentSetYear, currentSetMonth, currentSetDay)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}