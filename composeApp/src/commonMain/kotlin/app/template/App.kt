package app.template

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.template.ui.about.AboutScreen
import app.template.ui.home.HomeScreen
import app.template.ui.theme.AppTheme
import kotlinx.serialization.Serializable

/** The app's screens, by their routes. */
@Serializable
private data object Home

@Serializable
private data object About

/** The whole app, the same on every platform: [versionName] is the version the store shows. */
@Composable
fun App(
    container: AppContainer,
    versionName: String,
) {
    AppTheme {
        val navigation = rememberNavController()
        NavHost(navigation, startDestination = Home) {
            composable<Home> {
                HomeScreen(container.updates, onAbout = { navigation.navigate(About) })
            }
            composable<About> {
                AboutScreen(versionName, onBack = navigation::popBackStack)
            }
        }
    }
}
