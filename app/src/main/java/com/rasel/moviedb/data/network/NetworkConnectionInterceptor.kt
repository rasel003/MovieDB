package com.rasel.moviedb.data.network

import android.content.Context
import com.paperflymerchantapp.data.network.NetworkState
import com.paperflymerchantapp.utils.NoInternetException
import com.rasel.moviedb.data.preference.AppPrefsManager
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(
    private val context: Context,
    private val prefsManager: AppPrefsManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkState.isNetworkAvailable(context)) throw NoInternetException("Make sure you have an active data connection")

        return chain.proceed(
            chain.request().newBuilder().also {
                it.addHeader("Accept", "application/json")
            }.build()
        )
    }

}