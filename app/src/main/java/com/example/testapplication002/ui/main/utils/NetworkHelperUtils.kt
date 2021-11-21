package com.example.testapplication002.ui.main.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import com.example.testapplication002.R

object ApiConstants {
    public val URL_SCHEME = "https"
    public val URL_AUTHORITY ="api.nasa.gov"
    public val PATH_PLANETARY = "planetary"
    public val PATH_APOD = "apod"
    public val API_KEY_KEY = "api_key"
    public val DATE_KEY = "date"
}

object NetworkHelperUtils {
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else {
                    val networkInfo=connectivityManager.activeNetworkInfo
                    return networkInfo!=null && networkInfo.isConnected
                }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    fun buildURL(context: Context, apiParamValue: String) : String {
        val builder = Uri.Builder()
        builder.scheme(ApiConstants.URL_SCHEME)
            .authority(ApiConstants.URL_AUTHORITY)
            .appendPath(ApiConstants.PATH_PLANETARY)
            .appendPath(ApiConstants.PATH_APOD)
            .appendQueryParameter(ApiConstants.API_KEY_KEY,
                context.getString(R.string.nasa_api_key))
            .appendQueryParameter(ApiConstants.DATE_KEY, apiParamValue)
        return builder.build().toString()
    }
}