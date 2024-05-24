package com.example.dehghani_hw2
import PowerConnectionReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import CheckStatusWorker
import androidx.compose.runtime.Composable
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import androidx.work.ExistingPeriodicWorkPolicy
import android.content.Context
import java.io.File
import com.google.gson.Gson
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState

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

        // Read and display JSON data
        val logs = readLogsFromJson(applicationContext)
        setContent {
            DisplayLogs(logs)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerConnectionReceiver)
    }

    @Composable
    fun DisplayLogs(logs: List<LogEntry>) {
        val lazyListState = rememberLazyListState()

        LazyColumn(state = lazyListState) {
            items(logs.reversed()) { log ->
                Text(text = "${log.timestamp},${log.event}, Status: ${log.status}")
            }
        }
    }

    private fun readLogsFromJson(context: Context): List<LogEntry> {
        val logList = mutableListOf<LogEntry>()
        val logFile = File(context.filesDir, "logs.json")
        if (logFile.exists()) {
            val json = logFile.readText()
            logList.addAll(Gson().fromJson(json, Array<LogEntry>::class.java))
        }
        return logList
    }
}
