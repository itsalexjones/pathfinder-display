package radio.alexjones.studiodisplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dagger.hilt.android.AndroidEntryPoint
import radio.alexjones.studiodisplay.data.SettingsRepository
import radio.alexjones.studiodisplay.ui.theme.GlobalStudioDisplayTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val view = LocalView.current
            val panelUrl by settingsRepository.getDisplayUrl
                .collectAsState(initial = "")
            Log.i("MainActivity", "Panel URL: $panelUrl")

            GlobalStudioDisplayTheme {
                    DefaultComponent(panelUrl)
            }
            LaunchedEffect(Unit) {
                startBackgroundLightingService()
            }
            LaunchedEffect(Unit) { // Hide the system ui elements
                val window = (view.context as? ComponentActivity)?.window ?: return@LaunchedEffect
                val windowInsetsController =
                    WindowCompat.getInsetsController(window, view)
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    private fun startBackgroundLightingService() {
        val serviceIntent = Intent(this, BackgroundLightingService::class.java)
        startService(serviceIntent)
    }


}

@Composable
fun DefaultComponent(panelUrl: String) {
    Log.i("DefaultComponent", "Panel URL: $panelUrl")
    Box {
        FullScreenWebView(panelUrl)
        ActivityLauncherButton(
            target = SettingsActivity::class.java,
            text = "Settings",
            modifier = Modifier
                .size(125.dp)
                .alpha(0f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GlobalStudioDisplayTheme {
        DefaultComponent("https://lbc.co.uk")
    }
}

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

