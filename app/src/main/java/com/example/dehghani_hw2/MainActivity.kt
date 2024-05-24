package com.example.dehghani_hw2

import PowerConnectionReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    private val powerConnectionReceiver = PowerConnectionReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(powerConnectionReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        })

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