package app.template

import app.template.data.SettingsStore

/** Manual dependency injection: the app's long-lived objects, built once from what the [platform] gives. */
class AppContainer(
    platform: Platform,
) {
    /** What the app remembers between launches. */
    val settings = SettingsStore(platform.filesDir)

    /** The store's updates of the app. */
    val updates = platform.updates(settings)

    /** What the person has unlocked, the store and ads. */
    val monetization = platform.monetization
}
