package radio.alexjones.studiodisplay

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import radio.alexjones.studiodisplay.data.PathfinderClientOptions
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ConnectException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.UnknownHostException

class PathfinderTcpClient(
    private val clientOptions: PathfinderClientOptions,
    private val s: CoroutineScope
) {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null
    private var micLive: Boolean = false
    private var onAir: Boolean = false
    private val netScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        private const val PATHFINDER_PORT = 9600
        private const val LOG_TAG = "PathfinderClient"
    }

    fun start() {

        netScope.launch {
            Log.d(LOG_TAG, "Starting Pathfinder Client for ${clientOptions.server}")
            Log.d(LOG_TAG, "On Air Slot: $clientOptions.onAirSlot")
            Log.d(LOG_TAG, "Mic Live Slot: $clientOptions.micLiveSlot")
            while (netScope.isActive) {
                try {
                    runClient()
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Exception in client", e)
                    s.launch {
                        withContext(Dispatchers.Main) {
                            BackgroundLightingService().setLedState(LedCommand.ORANGE)
                        }
                    }
                    delay(5000) // Wait before retrying
                }
            }
        }
    }

    fun stop() {
        netScope.cancel()
        close()
    }

    private fun sendCommand(command: String, wait: Boolean): String? {
        Log.d(LOG_TAG, "Send: $command")
        try {
            writer?.write("$command\r\n")
            writer?.flush()
            if (wait) {
                val response = reader?.readLine()
                Log.d(LOG_TAG, "resp $response")
                return response
            }
            return null
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Exception sending command to Pathfinder", e)
        }
        return null
    }

    private fun close() {
        try {
            reader?.close()
            writer?.close()
            socket?.close()
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Exception closing", e)
        }
    }

    private fun runClient() {
        Log.d(LOG_TAG, "Starting client")
        try {
            socket = Socket()
            val inetAddress = InetAddress.getByName(clientOptions.server)
            socket?.connect(InetSocketAddress(inetAddress, PATHFINDER_PORT), 1000)
        } catch (e: UnknownHostException) {
            Log.e(LOG_TAG, "Unable to connect to pathfinder. Unknown host")
            throw e
        } catch (e: ConnectException) {
            Log.e(LOG_TAG, "Generic connect exception connecting to pathfinder")
            throw e
        }
        writer = PrintWriter(socket!!.outputStream, true)
        reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
        if (!socket!!.isConnected) {
            Log.e(
                LOG_TAG,
                "Socket not connected after create to ${clientOptions.server}:$PATHFINDER_PORT"
            )
            throw Exception("Failed to connect to pathfinder")
        }

        Log.i(LOG_TAG, "Connected to pathfinder at ${clientOptions.server}:$PATHFINDER_PORT")
        val r = sendCommand("LOGIN ${clientOptions.username} ${clientOptions.password}", true)
        if (r != null && "login successful" !in r) {
            Log.e(LOG_TAG, "Failed to log in to pathfinder. Response: $r")
            throw Exception("Failed to log into pathfinder")
        }
        Log.i(LOG_TAG, "Logged into Pathfinder")

        val onAirResp = sendCommand("get MemorySlots#0.MemorySlot#${clientOptions.onAirSlot} SlotValue", true)
        if (onAirResp != null && "indi MemorySlots#0.MemorySlot#${clientOptions.onAirSlot}" in onAirResp) {
            onAir = "SlotValue=ON" in onAirResp
            Log.i(LOG_TAG, "ON AIR set to $onAir")
        }
        val micLiveResp =
            sendCommand("get MemorySlots#0.MemorySlot#${clientOptions.micLiveSlot} SlotValue SlotValue", true)
        if (micLiveResp != null && "indi MemorySlots#0.MemorySlot#${clientOptions.micLiveSlot}" in micLiveResp) {
            micLive = "SlotValue=ON" in micLiveResp
            Log.i(LOG_TAG, "MIC LIVE set to $micLive")
        }
        processLighting()
        sendCommand("sub MemorySlots#0.MemorySlot#${clientOptions.onAirSlot} SlotValue", false)
        sendCommand("sub MemorySlots#0.MemorySlot#${clientOptions.micLiveSlot} SlotValue", false)

        while (true) {
            val r = reader?.readLine()
            Log.d(LOG_TAG, "resp $r")
            if (r == null) {
                Log.w(LOG_TAG, "Received null response")
            }
            if (r != null && "indi MemorySlots#0.MemorySlot#${clientOptions.onAirSlot}" in r) {
                onAir = "SlotValue=ON" in r
                Log.i(LOG_TAG, "ON AIR set to $onAir")
            }
            if (r != null && "indi MemorySlots#0.MemorySlot#${clientOptions.micLiveSlot}" in r) {
                micLive = "SlotValue=ON" in r
                Log.i(LOG_TAG, "MIC LIVE set to $micLive")
            }
            processLighting()
        }
    }

    private fun processLighting() {
        Log.d(LOG_TAG,"processLighting Start")
        if (micLive) {
            Log.d(LOG_TAG,"mic live")
            s.launch {
                withContext(Dispatchers.Main) {
                    BackgroundLightingService().setLedState(LedCommand.RED)
                }
            }
            return
        }
        if (onAir) {
            Log.d(LOG_TAG,"on air")
            s.launch {
                withContext(Dispatchers.Main) {
                    BackgroundLightingService().setLedState(LedCommand.BLUE)
                }
            }
            return
        }
        Log.d(LOG_TAG, "nothing, light off")
        s.launch {
            withContext(Dispatchers.Main) {
                BackgroundLightingService().setLedState(LedCommand.LED_OFF)
            }
        }
    }
}