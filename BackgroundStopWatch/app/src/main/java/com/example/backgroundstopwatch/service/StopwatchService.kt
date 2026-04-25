package com.example.backgroundstopwatch.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.backgroundstopwatch.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StopwatchService: Service() {
    // 1. Define a scope that lives as long as the service
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var timerJob: Job? = null
    private var secondsElapsed = 0

    // This is not a "Bound" service, so we return null
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START -> startStopwatchService()
            Constants.ACTION_STOP -> stopService()
        }

        // Automatically restart service if is killed automatically by Android
        return START_STICKY
    }

    private fun startStopwatchService() {
        // Start as foreground service so it's not killed
        startForeground(Constants.NOTIFICATION_ID, buildNotification("00:00:00"))

        // Start the timer loop
        timerJob?.cancel() // Reset if already running
        timerJob = serviceScope.launch {
            while (isActive) {
                delay(1000)
                secondsElapsed++
                updateNotification(formatTime(secondsElapsed))
            }
        }
    }

    private fun updateNotification(timeText: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.notify(Constants.NOTIFICATION_ID, buildNotification(timeText))
    }

    // Create the notification that will be shown in the tray
    private fun buildNotification(content: String): android.app.Notification {
        return NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setContentTitle("Stopwatch Running")
            .setContentText("Elapsed Time: $content")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setOnlyAlertOnce(true) // Crucial: prevents the phone from vibrating every second
            .build()
    }

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun stopService() {
        timerJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up coroutines here later
    }
}