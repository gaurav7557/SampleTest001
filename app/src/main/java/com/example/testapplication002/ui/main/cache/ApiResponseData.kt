package com.example.testapplication002.ui.main.cache

import java.io.Serializable

data class ApiResponseData(
    val date: String,
    val title: String,
    val url: String,
    val explanation: String
) : Serializable
