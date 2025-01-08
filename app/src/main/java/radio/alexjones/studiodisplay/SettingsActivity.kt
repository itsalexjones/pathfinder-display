package radio.alexjones.studiodisplay

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingsActivity", "onCreate called ${viewModel.serverLocation}")
        setContent {
            Settings(viewModel) {
                finish()
            }
        }
    }
}

@Composable
fun Settings(viewModel: SettingsViewModel, onSave: () -> Unit) {
    Row {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.serverLocation,
                onValueChange = { viewModel.onServerLocationChange(it) },
                label = { Text("Pathfinder Server") }
            )
            OutlinedTextField(
                value = viewModel.userName,
                onValueChange = { viewModel.onUserNameChange(it) },
                label = { Text("User") }
            )
            OutlinedTextField(
                value = viewModel.userPassword,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { viewModel.onUserPasswordChange(it) },
                label = { Text("Password") }
            )

            Button(onClick = {
                viewModel.saveSettings()
                onSave()
            }) {
                Text("Save")
            }
        }
        Column (
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.panelName,
                onValueChange = { viewModel.onPanelNameChange(it) },
                label = { Text("Panel Name") }
            )
            OutlinedTextField(
                value = viewModel.panelPage,
                onValueChange = { viewModel.onPanelPageChange(it) },
                label = { Text("Panel Page") }
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.onAirSlot,
                onValueChange = { viewModel.onOnAirSlotChange(it) },
                label = { Text("Blue LED Slot Name") }
            )
            OutlinedTextField(
                value = viewModel.micLiveSlot,
                onValueChange = { viewModel.onMicLiveSlotChange(it) },
                label = { Text("Red LED Live Slot Name") }
            )
        }
    }

}