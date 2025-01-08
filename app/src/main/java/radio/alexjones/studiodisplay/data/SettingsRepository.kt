/*
 * "THE BEER-WARE LICENSE"
 * Alex Jones (alex [at] alexjones [dot] radio) wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 */

package radio.alexjones.studiodisplay.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor (@ApplicationContext private val context: Context) {
    private companion object {
        val PATHFINDER_SERVER_LOCATION = stringPreferencesKey("pathfinder_server_location")
        val PATHFINDER_USER_NAME = stringPreferencesKey("pathfinder_user_name")
        val PATHFINDER_USER_PASSWORD = stringPreferencesKey("pathfinder_user_password")
        val PATHFINDER_PANEL_NAME = stringPreferencesKey("pathfinder_panel_name")
        val PATHFINDER_PANEL_PAGE = stringPreferencesKey("pathfinder_panel_page")
        val PATHFINDER_ON_AIR_SLOT = stringPreferencesKey("pathfinder_on_air_slot")
        val PATHFINDER_MIC_LIVE_SLOT = stringPreferencesKey("pathfinder_mic_live_slot")
    }

    // Getters
    val getServerLocation = context.dataStore.data.map { preferences ->
        val ip = preferences[PATHFINDER_SERVER_LOCATION] ?: ""
        Log.d("SettingsRepository", "getServerLocation: $ip")
        ip
    }

    val getUserName = context.dataStore.data.map { preferences ->
        val name = preferences[PATHFINDER_USER_NAME] ?: ""
        Log.d("SettingsRepository", "getUserName: $name")
        name
    }

    val getUserPassword = context.dataStore.data.map { preferences ->
        val ret = preferences[PATHFINDER_USER_PASSWORD] ?: ""
        Log.d("SettingsRepository", "getUserPassword: $ret")
        ret
    }

    val getPanelName = context.dataStore.data.map { preferences ->
        val ret = preferences[PATHFINDER_PANEL_NAME] ?: ""
        Log.d("SettingsRepository", "getPanelName: $ret")
        ret
    }

    val getPanelPage = context.dataStore.data.map { preferences ->
        val ret = preferences[PATHFINDER_PANEL_PAGE] ?: "index"
        Log.d("SettingsRepository", "getPanelPage: $ret")
        ret
    }

    val getOnAirSlot = context.dataStore.data.map { preferences ->
        val ret = preferences[PATHFINDER_ON_AIR_SLOT] ?: "On_Air"
        Log.d("SettingsRepository", "getOnAirSlot: $ret")
        ret
    }

    val getMicLiveSlot = context.dataStore.data.map { preferences ->
        val ret = preferences[PATHFINDER_MIC_LIVE_SLOT] ?: "Mic_Live"
        Log.d("SettingsRepository", "getMicLiveSlot: $ret")
        ret
    }


    // Setters
    suspend fun setServerLocation(location: String) {
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences()
                .apply { this[PATHFINDER_SERVER_LOCATION] = location }
        }
    }

    suspend fun setUserName(userName: String) {
        Log.d("SettingsRepository", "setUserName: $userName")
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences()
                .apply { this[PATHFINDER_USER_NAME] = userName }

        }
    }

    suspend fun setUserPassword(userPassword: String) {
        Log.d("SettingsRepository", "setUserPassword: $userPassword")
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences()
                .apply { this[PATHFINDER_USER_PASSWORD] = userPassword }
        }
    }

    suspend fun setPanelName(panelName: String) {
        Log.d("SettingsRepository", "setPanelName: $panelName")
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences()
                .apply { this[PATHFINDER_PANEL_NAME] = panelName } }
    }

    suspend fun setPanelPage(panelPage: String) {
        Log.d("SettingsRepository", "setPanelPage: $panelPage")
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences()
                .apply { this[PATHFINDER_PANEL_PAGE] = panelPage }
        }
    }

    suspend fun setOnAirSlot(onAirSlot: String) {
        Log.d("SettingsRepository", "setOnAirSlot: $onAirSlot")
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences()
                .apply { this[PATHFINDER_ON_AIR_SLOT] = onAirSlot }
        }
    }

    suspend fun setMicLiveSlot(micLiveSlot: String) {
        Log.d("SettingsRepository", "setMicLiveSlot: $micLiveSlot")
        context.dataStore.updateData { preferences ->
            preferences.toMutablePreferences()
                .apply { this[PATHFINDER_MIC_LIVE_SLOT] = micLiveSlot }
        }
    }
}