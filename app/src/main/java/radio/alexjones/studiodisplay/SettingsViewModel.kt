package radio.alexjones.studiodisplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import radio.alexjones.studiodisplay.data.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    var serverLocation by mutableStateOf("")
        private set
    var userName by mutableStateOf("")
        private set
    var userPassword by mutableStateOf("")
        private set
    var onAirSlot by mutableStateOf("")
        private set
    var micLiveSlot by mutableStateOf("")
        private set
    var displayUrl by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            launch {
                settingsRepository.getServerLocation.collect { serverLocation = it }
            }
            launch {
                settingsRepository.getUserName.collect { userName = it }
            }
            launch {
                settingsRepository.getUserPassword.collect { userPassword = it }
            }
            launch {
                settingsRepository.getOnAirSlot.collect { onAirSlot = it }
            }
            launch {
                settingsRepository.getMicLiveSlot.collect { micLiveSlot = it }
            }
            launch {
                settingsRepository.getDisplayUrl.collect { displayUrl = it }
            }
        }
    }

    fun onServerLocationChange(newServerLocation: String) {
        serverLocation = newServerLocation
    }

    fun onUserNameChange(newUserName: String) {
        userName = newUserName
    }

    fun onUserPasswordChange(newUserPassword: String) {
        userPassword = newUserPassword
    }

    fun onOnAirSlotChange(newOnAirSlot: String) {
        onAirSlot = newOnAirSlot
    }

    fun onMicLiveSlotChange(newMicLiveSlot: String) {
        micLiveSlot = newMicLiveSlot
    }

    fun onDisplayUrlChange(newDisplayUrl: String) {
        displayUrl = newDisplayUrl
    }

    fun saveSettings() {
        viewModelScope.launch {
            launch {
                settingsRepository.setServerLocation(serverLocation)
            }
            launch {
                settingsRepository.setUserName(userName)
            }
            launch {
                settingsRepository.setUserPassword(userPassword)
            }
            launch {
                settingsRepository.setOnAirSlot(onAirSlot)
            }
            launch {
                settingsRepository.setMicLiveSlot(micLiveSlot)
            }
            launch {
                settingsRepository.setDisplayUrl(displayUrl)
            }
        }
    }
}