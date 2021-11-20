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
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { _ ->
            val currentCalenderInstance = Calendar.getInstance()
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener {
                        datePicker: DatePicker, year: Int, month: Int, day: Int ->
                        Log.d("Hello", "display :: "+year+" "+month+" "+day)
                    },
                    currentCalenderInstance.get(Calendar.YEAR),
                    currentCalenderInstance.get(Calendar.MONTH),
                    currentCalenderInstance.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}