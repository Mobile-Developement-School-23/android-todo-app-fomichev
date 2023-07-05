package com.example.mytodoapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject
/**
 * The CheckConnection class provides a utility for checking network connectivity.
 * It determines whether the device is currently connected to the internet.
 */

class CheckConnection  @Inject constructor (
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