/*
 * "THE BEER-WARE LICENSE"
 * Alex Jones (alex [at] alexjones [dot] radio) wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 */

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