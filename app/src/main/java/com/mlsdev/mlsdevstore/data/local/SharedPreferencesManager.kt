package com.mlsdev.mlsdevstore.data.local

import android.content.SharedPreferences

import com.google.gson.Gson

import java.lang.reflect.Type

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SharedPreferencesManager @Inject
constructor(private val gson: Gson, val sharedPreferences: SharedPreferences) {

    fun save(key: String, data: Any) {
        val json = gson.toJson(data)
        sharedPreferences.edit()
                .putString(key, json)
                .apply()
    }

    open fun remove(key: String) {
        sharedPreferences.edit()
                .remove(key)
                .apply()
    }

    operator fun <T> get(key: String, dataClass: Class<T>): T? {
        var data: T? = null
        val json = sharedPreferences.getString(key, null)

        if (json != null)
            data = gson.fromJson(json, dataClass)

        return data
    }

    operator fun <T> get(key: String, type: Type): T? {
        var data: T? = null
        val json = sharedPreferences.getString(key, null)

        if (json != null)
            data = gson.fromJson<T>(json, type)

        return data
    }
}

annotation class Key {
    companion object {
        const val APPLICATION_ACCESS_TOKEN = "application_access_token"
        const val RANDOM_CATEGORY_TREE_NODE = "random_category_tree_node"
    }
}