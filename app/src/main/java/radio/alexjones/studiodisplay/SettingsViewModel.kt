/*
 * "THE BEER-WARE LICENSE"
 * Alex Jones (alex [at] alexjones [dot] radio) wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 */

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
    var panelName by mutableStateOf("")
        private set
    var panelPage by mutableStateOf("")
        private set
    var onAirSlot by mutableStateOf("")
        private set
    var micLiveSlot by mutableStateOf("")
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
                settingsRepository.getPanelName.collect { panelName = it }
            }
            launch {
                settingsRepository.getPanelPage.collect { panelPage = it }
            }
            launch {
                settingsRepository.getOnAirSlot.collect { onAirSlot = it }
            }
            launch {
                settingsRepository.getMicLiveSlot.collect { micLiveSlot = it }
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

    fun onPanelNameChange(newPanelName: String) {
        panelName = newPanelName
    }

    fun onPanelPageChange(newPanelPage: String) {
        panelPage = newPanelPage
    }

    fun onOnAirSlotChange(newOnAirSlot: String) {
        onAirSlot = newOnAirSlot
    }

    fun onMicLiveSlotChange(newMicLiveSlot: String) {
        micLiveSlot = newMicLiveSlot
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
                settingsRepository.setPanelName(panelName)
            }
            launch {
                settingsRepository.setPanelPage(panelPage)
            }
            launch {
                settingsRepository.setOnAirSlot(onAirSlot)
            }
            launch {
                settingsRepository.setMicLiveSlot(micLiveSlot)
            }
        }
    }
}