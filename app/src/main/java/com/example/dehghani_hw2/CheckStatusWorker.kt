import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


class CheckStatusWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val bluetoothStatus = isBluetoothEnabled()
        val airplaneModeStatus = isAirplaneModeOn(applicationContext)

        Log.d("worker_airplane", "Bluetooth Enabled: $bluetoothStatus, Airplane Mode: $airplaneModeStatus")

        return Result.success()
    }

    private fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.isEnabled ?: false
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return android.provider.Settings.Global.getInt(context.contentResolver, android.provider.Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }
}
