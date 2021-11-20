package com.example.testapplication002.ui.main.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import java.util.Calendar

class ResultsPageViewModel() : ViewModel() {

    var text = "Hello World"

    private var year: Int = Calendar.getInstance().get(Calendar.YEAR)
    private var month: Int = Calendar.getInstance().get(Calendar.MONTH)
    private var day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    fun setDateComponents(yy: Int, mm: Int, dd: Int) {
        year = yy
        month = mm
        day = dd
    }
}