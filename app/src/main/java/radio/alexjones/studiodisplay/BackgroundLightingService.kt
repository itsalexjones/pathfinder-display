package radio.alexjones.studiodisplay

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import radio.alexjones.studiodisplay.data.SettingsRepository
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundLightingService() : LifecycleService() {

    companion object {
        private const val LOG_TAG = "BackgroundLightingService"
    }

    private var pathfinderIp  = ""
    private var onAirSlot: String? = null
    private var micLiveSlot: String? = null
    private var client: PathfinderTcpClient? = null

    @Inject
    lateinit var settingsRepository: SettingsRepository


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i(LOG_TAG, "Starting")
        setLedState(LedCommand.LED_ON)

        lifecycleScope.launch {
            settingsRepository.getServerLocation
                .combine(settingsRepository.getOnAirSlot) { serverLocation, onAir ->
                    Pair(serverLocation, onAir)
                }
                .combine(settingsRepository.getMicLiveSlot) { pair, micLive ->
                    Triple(pair.first, pair.second, micLive)
                }
                .collectLatest { (pathfinderIp, onAirSlot, micLiveSlot) ->
                    Log.i(
                        LOG_TAG,
                        "Pathfinder IP: $pathfinderIp, On Air Slot: $onAirSlot, Mic Live Slot: $micLiveSlot"
                    )
                    client = PathfinderTcpClient(pathfinderIp, onAirSlot, micLiveSlot, lifecycleScope)
                    client?.start()
                }
        }

        Toast.makeText(this, "Background Lighting Service Started", Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    // https://yixu-elec.com/call-led-indicators-meeting-roomdisplay/
    fun setLedState(state: LedCommand) {
        Log.d(LOG_TAG, "setLedState: $state")
        when (state) {
            LedCommand.BRIGHTNESS_UP -> sendLedCommand("0x00")
            LedCommand.BRIGHTNESS_DOWN -> sendLedCommand("0x01")
            LedCommand.LED_OFF -> sendLedCommand("0X66000000") // Note this sets RGB to 0 0 0 to avoid having to call LED_ON again
            LedCommand.LED_ON -> sendLedCommand("0x03")
            LedCommand.RED -> sendLedCommand("0x04")
            LedCommand.GREEN -> sendLedCommand("0x05")
            LedCommand.BLUE -> sendLedCommand("0x06")
            LedCommand.WHITE -> sendLedCommand("0x07")
            LedCommand.ORANGE -> sendLedCommand("0x08")
            LedCommand.DISCO -> sendLedCommand("0x0B")
        }
    }

    private fun sendLedCommand(command: String) {
        adbCommand("echo w $command > /sys/devices/platform/led_con_h/zigbee_reset")
    }

    private fun adbCommand(command: String): String {
        var process: Process? = null
        var os: DataOutputStream? = null
        var excResult = ""

        try {
            process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("$command\n")
            os.writeBytes("exit\n")
            os.flush()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val stringBuffer = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                stringBuffer.append("$line ")
            }

            excResult = stringBuffer.toString()
            if (excResult.isEmpty()) {
                Log.d("$LOG_TAG:ADBCommand", "Response NULL")
            } else {
                Log.d("$LOG_TAG:ADBCommand", "Response: $excResult")
            }

            os.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return excResult
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOG_TAG, "onDestory Called.")
        client?.stop()
        setLedState(LedCommand.LED_OFF)
        lifecycleScope.cancel()
    }
}