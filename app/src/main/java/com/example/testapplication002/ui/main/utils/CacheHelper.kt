package com.example.testapplication002.ui.main.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit

object CacheHelper {
    var cacheLifeHour = 7 * 24

    fun getCacheDirectory(context: Context): String {
        return context.getCacheDir().getPath()
    }

    fun saveData(context: Context, key: String, value: String?) {
        var key = key
        try {
            key = URLEncoder.encode(key, "UTF-8")
            val cache = File(getCacheDirectory(context) + "/" + key + ".srl")
            val out: ObjectOutput = ObjectOutputStream(FileOutputStream(cache))
            out.writeUTF(value)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun retrieveData(context: Context, key: String): String {
        var key = key
        try {
            key = URLEncoder.encode(key, "UTF-8")
            val cache = File(getCacheDirectory(context) + "/" + key + ".srl")
            if (cache.exists()) {
                val lastModDate = Date(cache.lastModified())
                val now = Date()
                val diffInMillisec: Long = now.getTime() - lastModDate.getTime()
                var diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec)
                diffInSec /= 60
                diffInSec /= 60
                val hours = diffInSec % 24
                if (hours > cacheLifeHour) {
                    cache.delete()
                    return ""
                }
                val `in` = ObjectInputStream(FileInputStream(cache))
                val value: String = `in`.readUTF()
                `in`.close()
                return value
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun saveImage(context: Context, key: String, bitmap: Bitmap?) {
        var key = key
        try {
            key = URLEncoder.encode(key, "UTF-8")
            val cache = File(getCacheDirectory(context) + "/" + key + ".srl")
            val out = FileOutputStream(cache)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun retrieveImage(context: Context, key: String): Bitmap? {
        var key = key
        try {
            key = URLEncoder.encode(key, "UTF-8")
            val cache = File(getCacheDirectory(context) + "/" + key + ".srl")
            if (cache.exists()) {
                val lastModDate = Date(cache.lastModified())
                val now = Date()
                val diffInMillisec: Long = now.getTime() - lastModDate.getTime()
                var diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec)
                diffInSec /= 60
                diffInSec /= 60
                val hours = diffInSec % 24
                if (hours > cacheLifeHour) {
                    cache.delete()
                    return null
                }
                val `in` = FileInputStream(cache)
                var options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap = BitmapFactory.decodeStream(`in`,null, options)
                `in`.close()
                return bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}