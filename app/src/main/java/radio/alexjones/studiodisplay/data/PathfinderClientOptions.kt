package radio.alexjones.studiodisplay.data

data class PathfinderClientOptions(
    val server: String,
    val onAirSlot: String?,
    val micLiveSlot: String?,
    val username: String,
    val password: String
)
