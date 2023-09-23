package com.rasel.moviedb.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.getString
import android.telephony.TelephonyManager
import com.paperflymerchantapp.data.network.NetworkState
import com.rasel.moviedb.data.preference.AppPrefsManager
import com.paperflymerchantapp.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class NetworkConnectionInterceptor(
    private val context: Context,
    private val prefsManager: AppPrefsManager
) : Interceptor {

    var DEVICE_NAME: String = Build.DEVICE
    private var device_unique_id: String = getIMEINumber(context).toString()
    private var DEVICE_FULL_INFO: String =
        Build.BRAND + " - " + DEVICE_NAME + " - " + device_unique_id

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkState.isNetworkAvailable(context)) throw NoInternetException("Make sure you have an active data connection")

        return chain.proceed(
            chain.request().newBuilder().also {
                it.addHeader("Accept", "application/json")
                    .addHeader("DEVICE_FULL_INFO", DEVICE_FULL_INFO)
                    .addHeader("LATITUDE", "")
                    .addHeader("LONGITUDE", "")
                    .addHeader("DEVICE_IDENTIFIER", getDeviceId(context)!!)
                    .addHeader("DEVICE_NAME", Build.BRAND + " " + Build.MODEL)
//                    .addHeader(
//                        "Authorization",
//                        basic(prefsManager.userName!!, prefsManager.password!!)
                    .addHeader("Authorization", "Bearer " + prefsManager.userToken)

                //basic("m10001", "123456789J")

            }.build()
        )

        /*    var request = chain.request()
            request = if (isNetworkAvailable())
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
          return  chain.proceed(request)*/

        /* val builder = chain.request().newBuilder()
         builder.addHeader("Accept", "application/json")*/

    }

    @Throws(SecurityException::class, NullPointerException::class)
    fun getIMEINumber(context: Context): String? {
        var deviceId: String? = ""
        try {
            deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } else {
                val mTelephony =
                    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (mTelephony.deviceId != null) {
                    mTelephony.deviceId
                } else {
                    getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return deviceId
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        return getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}