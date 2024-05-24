import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
public var Onn = 1
class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_POWER_CONNECTED) {
            // Power connected, show notification
            NotificationUtils.showNotification(context, "Power Connected", "Device is now charging")
            Onn = 1

        } else if (intent?.action == Intent.ACTION_POWER_DISCONNECTED) {
            // Power disconnected, show notification
            NotificationUtils.showNotification(context, "Power Disconnected", "Device is no longer charging")
            Onn = 0
        }
    }
}
