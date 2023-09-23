package com.rasel.moviedb.application

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger
import com.rasel.moviedb.utils.CHANNEL_ID_RECEIVED_LEAVE_REQUEST
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // for file provider
        Thread.setDefaultUncaughtExceptionHandler(
            LocalFileUncaughtExceptionHandler(
                this,
                Thread.getDefaultUncaughtExceptionHandler()
            )
        )

        // used this logger throughout the app for logging
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        isLanguageSynced = false
    }

    companion object {
        lateinit var myAppInstance: GoApplication
        var isLanguageSynced: Boolean = false
    }

    init {
        myAppInstance = this
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Leave Request" //getString(R.string.channel_received_leave_request)
            val descriptionText =
                "Channel for Received Leave Request" //getString(R.string.channel_leave_request_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel =
                NotificationChannel(CHANNEL_ID_RECEIVED_LEAVE_REQUEST, name, importance).apply {
                    description = descriptionText
                }
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableLights(true)
            channel.lightColor = Color.RED
            // channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, )
            channel.enableVibration(true)
            channel.setShowBadge(true)


            // Register the channel with the system
            //   val notificationManager: NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, audioAttributes)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}