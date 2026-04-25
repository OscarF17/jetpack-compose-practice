package com.example.backgroundstopwatch.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class StopwatchService: Service() {
    // This is not a "Bound" service, so we return null
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // This is where the magic happens when we call startService()

        // START_STICKY tells Android: "If you kill me, restart me!"
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up coroutines here later
    }
}