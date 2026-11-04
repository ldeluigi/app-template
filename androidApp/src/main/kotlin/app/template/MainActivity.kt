package app.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import app.template.update.InAppUpdates

/** The single activity: it shows the shared app, edge to edge. */
class MainActivity : ComponentActivity() {
    private val container: AppContainer get() = (application as AppApplication).container

    // Google Play's own update screens start from the activity on screen
    private val updateScreens = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        (container.updates as? InAppUpdates)?.attach(updateScreens)
        setContent { App(container, BuildConfig.VERSION_NAME) }
    }
}
