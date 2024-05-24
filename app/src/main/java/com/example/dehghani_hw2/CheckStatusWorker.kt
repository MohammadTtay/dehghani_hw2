import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dehghani_hw2.LogEntry
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.Gson

class CheckStatusWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val bluetoothStatus = isBluetoothEnabled()
        val airplaneModeStatus = isAirplaneModeOn(applicationContext)
        val isPowerConnected = isPowerConnected(applicationContext)
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        saveLogToFile(applicationContext, timestamp, "Power Connected", if ( isPowerConnected ) "On" else "Off")
        saveLogToFile(applicationContext, timestamp, "Bluetooth", if (bluetoothStatus) "Enabled" else "Disabled")
        saveLogToFile(applicationContext, timestamp, "Airplane Mode", if (airplaneModeStatus) "Enabled" else "Disabled")

        return Result.success()
    }

    private fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.isEnabled ?: false
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return android.provider.Settings.Global.getInt(context.contentResolver, android.provider.Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }
    private fun isPowerConnected(context: Context): Boolean {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, intentFilter)
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
    }

    private fun saveLogToFile(context: Context, timestamp: String, event: String, status: String) {
        val logEntry = LogEntry(timestamp, event, status)
        val logList = mutableListOf<LogEntry>()

        // Read existing logs
        val logFile = File(context.filesDir, "logs.json")
        if (logFile.exists()) {
            val json = logFile.readText()
            logList.addAll(Gson().fromJson(json, Array<LogEntry>::class.java))
        }

        // Add new log
        logList.add(logEntry)

        // Write to file
        FileWriter(logFile).use { writer ->
            writer.write(Gson().toJson(logList))
        }
    }
}
