package com.bc.env.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

enum class NetworkStatus {
    NONE,
    WIFI,
    CELLULAR,
    UNKNOWN
}

object NetworkUtil {
    fun getNetworkStatus(context: Context): NetworkStatus {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = cm.activeNetwork ?: return NetworkStatus.NONE

        val capabilities = cm.getNetworkCapabilities(network) ?: return NetworkStatus.NONE

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkStatus.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkStatus.CELLULAR
            else -> NetworkStatus.UNKNOWN
        }
    }

    fun isConnected(context: Context): Boolean {
        return getNetworkStatus(context) != NetworkStatus.NONE
    }
}