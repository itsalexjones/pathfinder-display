package radio.alexjones.studiodisplay

import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ActivityLauncherButton(target: Class<*>, text: String, modifier: Modifier = Modifier,) {
    val context = LocalContext.current

    Button(
        onClick = {
            val intent = Intent(context, target)
            context.startActivity(intent)
        },
        modifier = modifier)
        {
            Text(text)
        }
}