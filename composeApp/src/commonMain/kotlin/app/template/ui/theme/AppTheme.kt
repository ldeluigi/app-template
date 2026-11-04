package app.template.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** The app's colours: one accent, light and dark. Make it the app's own. */
private val Accent = Color(0xFF3D5AFE)

/** Material 3 with the app's colours, following the phone's light or dark setting. */
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors =
        if (isSystemInDarkTheme()) {
            darkColorScheme(primary = Accent)
        } else {
            lightColorScheme(primary = Accent)
        }
    MaterialTheme(colorScheme = colors, content = content)
}
