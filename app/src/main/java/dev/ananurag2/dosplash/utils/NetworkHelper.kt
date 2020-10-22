package dev.ananurag2.dosplash.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Network Utility to detect availability or unavailability of Internet connection
 */
object NetworkHelper : ConnectivityManager.NetworkCallback() {

    private val networkLiveState: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Returns instance of [LiveData] which can be observed for network changes.
     */
    fun getNetworkLiveData(context: Context): LiveData<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), this)
        }

        var isConnected = false

        // Retrieve current status of connectivity
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)

            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        networkLiveState.postValue(isConnected)

        return networkLiveState
    }

    /**
     * Returns [Boolean] which can be used to determine network state.
     * `true` means connected.
     */
    fun getNetworkStatus() = networkLiveState.value ?: false

    override fun onAvailable(network: Network) {
        networkLiveState.postValue(true)
    }

    override fun onLost(network: Network) {
        networkLiveState.postValue(false)
    }
}