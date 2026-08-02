package com.example.llm.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object JsonUtils {

    inline fun <reified T> loadJSONFromAsset(context: Context, fileName: String): T? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(jsonString, type)
    }

    fun toJson(obj: Any): String {
        return Gson().toJson(obj)
    }

    inline fun <reified T> fromJson(json: String): T? {
        return try {
            Gson().fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}