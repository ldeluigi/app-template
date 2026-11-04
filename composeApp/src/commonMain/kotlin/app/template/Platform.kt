package app.template

import app.template.monetization.Free
import app.template.monetization.Monetization
import app.template.update.AppUpdates
import app.template.update.SnoozeStore
import kotlinx.io.files.Path

/**
 * What each platform gives the app: where it keeps its files, its store's updates, and purchases and ads. Everything
 * else is the same on every platform, built from these by [AppContainer]. A capability only one platform has gets an
 * interface here, a real implementation on that platform and a null object on the other.
 */
interface Platform {
    /** The app's private folder, for its settings and data. */
    val filesDir: Path

    /** The store's updates of the app; the "Later" choice is kept in [snooze]. */
    fun updates(snooze: SnoozeStore): AppUpdates

    /** Purchases and ads: none until a platform plugs a store or an ad network in. */
    val monetization: Monetization get() = Free
}
