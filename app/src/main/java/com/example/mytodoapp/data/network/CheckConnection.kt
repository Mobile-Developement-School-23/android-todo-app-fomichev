package com.example.mytodoapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class CheckConnection(
    private val context: Context
) {
    fun isOnline(): Boolean {
        val connectMan = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectMan.activeNetwork
        if (network != null) {
            val networkCapability = connectMan.getNetworkCapabilities(network)
            return networkCapability!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapability.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }
}