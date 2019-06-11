package com.mlsdev.mlsdevstore.presentaion.utils

import android.content.Context
import android.net.ConnectivityManager

open class Utils(private val context: Context) {

    open fun isNetworkAvailable(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}