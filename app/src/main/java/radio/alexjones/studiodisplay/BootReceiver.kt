package radio.alexjones.studiodisplay

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class BootReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("BootReceiver", "Boot completed, starting service")
            val activityIntent = Intent(context, BackgroundLightingService::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startService(activityIntent)
        } else {
            Log.w("BootReceiver", "Unknown action: ${intent.action}")
        }
    }
}