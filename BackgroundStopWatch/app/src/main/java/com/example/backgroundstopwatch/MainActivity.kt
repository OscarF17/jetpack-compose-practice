package com.example.backgroundstopwatch

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.backgroundstopwatch.service.StopwatchService
import com.example.backgroundstopwatch.ui.theme.BackgroundStopWatchTheme
import com.example.backgroundstopwatch.utils.Constants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermission()
        setContent {
            BackgroundStopWatchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StopwatchScreen()
                }
            }
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }
}

@Composable
fun StopwatchScreen() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                val intent = Intent(context, StopwatchService::class.java).apply {
                    action = Constants.ACTION_START
                }

                // In Android 8.0+, we use startForegroundService
                context.startForegroundService(intent)
            }
        ) {
            Text("Start service")
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Stop Button
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(0.6f),
            onClick = {
                val intent = Intent(context, StopwatchService::class.java).apply {
                    action = Constants.ACTION_STOP
                }
                context.startService(intent)
            }
        ) {
            Text("Stop Service")
        }
    }
}