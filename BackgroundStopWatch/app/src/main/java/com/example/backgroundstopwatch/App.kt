package com.example.backgroundstopwatch

import android.app.Application
import com.example.backgroundstopwatch.utils.NotificationHelper

class BackgroundStopwatchApp: Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}