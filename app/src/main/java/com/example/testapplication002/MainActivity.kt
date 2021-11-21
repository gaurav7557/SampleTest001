package com.example.testapplication002

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.DatePicker
import com.example.testapplication002.ui.main.adapters.SectionsPagerAdapter
import com.example.testapplication002.ui.main.fragments.ResultsFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private val currentCalenderInstance = Calendar.getInstance()

    private val RESULTS_FRAGMENT_ID = 0
    private val FAVOUTITES_FRAGMENT_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { _ ->
            val datePickerDialog = DatePickerDialog(this,
                    {
                        datePicker: DatePicker, year: Int, month: Int, day: Int ->
                        Log.d("Hello", "display :: "+year+" "+month+" "+day)
                        updateResultsFragment(year, month, day)
                    },
                    currentCalenderInstance.get(Calendar.YEAR),
                    currentCalenderInstance.get(Calendar.MONTH),
                    currentCalenderInstance.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = currentCalenderInstance.timeInMillis
            datePickerDialog.show()
        }
    }

    private fun updateResultsFragment(year: Int, month: Int, day: Int) {
        val resultsFragment = supportFragmentManager.findFragmentByTag("android:switcher:" + viewPager.id + ":" + RESULTS_FRAGMENT_ID) as ResultsFragment
        resultsFragment.updateViewModel(year, month, day)
        viewPager.currentItem = RESULTS_FRAGMENT_ID
    }
}