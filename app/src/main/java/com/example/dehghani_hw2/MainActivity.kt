package com.example.dehghani_hw2
import PowerConnectionReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.ui.tooling.preview.Preview
import CheckStatusWorker
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import androidx.work.ExistingPeriodicWorkPolicy

class MainActivity : ComponentActivity() {
    private val powerConnectionReceiver = PowerConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(powerConnectionReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        })

        // Schedule the periodic worker
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            CheckStatusWorker::class.java, 15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "CheckStatusWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )

        setContent {
            MainContent()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerConnectionReceiver)
    }
}

@Composable
fun MainContent() {
    Column {
        Text(text = "Power Connection Status")
    }
}

@Preview
@Composable
fun PreviewMainContent() {
    MainContent()
}