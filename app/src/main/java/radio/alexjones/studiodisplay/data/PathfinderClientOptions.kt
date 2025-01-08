/*
 * "THE BEER-WARE LICENSE"
 * Alex Jones (alex [at] alexjones [dot] radio) wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 */

package radio.alexjones.studiodisplay.data

data class PathfinderClientOptions(
    val server: String,
    val onAirSlot: String?,
    val micLiveSlot: String?,
    val username: String,
    val password: String
)
