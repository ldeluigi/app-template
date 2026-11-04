package app.template

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSBundle
import platform.UIKit.UIViewController

/** The app's one screen controller, which the iOS app shows as its root. */
@Suppress("FunctionName", "unused") // Called from Swift, as UIKit names its factories.
fun MainViewController(): UIViewController = ComposeUIViewController { App(container, versionName) }

/** The app's long-lived objects, made once on the first screen. */
private val container: AppContainer by lazy { AppContainer(IosPlatform()) }

/** The version the App Store shows. */
internal val versionName: String
    get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "?"
