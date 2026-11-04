package app.template.update

import app.template.currentTimeMs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** What the home screen shows about updates. */
sealed interface UpdateState {
    /** Nothing to show. */
    data object Idle : UpdateState

    /** A new version can be downloaded. */
    data object Available : UpdateState

    /** The new version is being downloaded in the background. */
    data object Downloading : UpdateState

    /** The new version is downloaded: restart to install it. */
    data object ReadyToInstall : UpdateState
}

/**
 * The store's updates of the app, offered on the home screen only, never in the middle of a task. Each platform asks
 * its own store; [UpdatePolicy] decides, the same everywhere, how hard to push.
 */
interface AppUpdates {
    /** The current update state. */
    val state: StateFlow<UpdateState>

    /** Asks the store for an update; forces it right away when the policy says so. */
    suspend fun check()

    /** The player tapped "Update". */
    fun download()

    /** The player tapped "Later": hide the banner for a few days. */
    fun snooze()

    /** Restarts the app into the downloaded version. */
    fun install()
}

/** Where the "Later" choice is remembered. */
interface SnoozeStore {
    /** Until when the update banner is hidden, if snoozed. */
    fun snoozedUntil(): Long?

    /** Hides the banner until [untilMs]. */
    fun snooze(untilMs: Long)
}

/** Hides the banner for [UpdatePolicy.SNOOZE_MILLIS] from now: what "Later" does. */
fun SnoozeStore.snoozeNow() = snooze(currentTimeMs() + UpdatePolicy.SNOOZE_MILLIS)

/** A platform that offers no updates from within the app (yet): the store app updates it by itself. */
class NoUpdates : AppUpdates {
    override val state: StateFlow<UpdateState> = MutableStateFlow<UpdateState>(UpdateState.Idle).asStateFlow()

    override suspend fun check() = Unit

    override fun download() = Unit

    override fun snooze() = Unit

    override fun install() = Unit
}
