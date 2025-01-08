/*
 * "THE BEER-WARE LICENSE"
 * Alex Jones (alex [at] alexjones [dot] radio) wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 */

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