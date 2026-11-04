package app.template

import android.app.Application

/** Holds the app's long-lived objects for as long as the app runs. */
class AppApplication : Application() {
    /** Everything the screens need, built once. */
    val container: AppContainer by lazy { AppContainer(AndroidPlatform(this)) }
}
