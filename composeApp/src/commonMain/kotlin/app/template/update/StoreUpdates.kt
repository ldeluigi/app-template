package app.template.update

import app.template.currentTimeMs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Instant

/** The newest version on a store's listing: its number, when it came out, and its page in the store. */
data class StoreRelease(
    val version: String,
    val releasedMs: Long,
    val page: String,
)

/**
 * Updates from a store that installs them itself, outside the app (the App Store). Its listing tells the newest
 * version, and [UpdatePolicy] decides as on Android, from the priority the release pipeline gives each kind of
 * version, told here from the version numbers. "Update" opens the store's page. Such a store can't be made to install
 * at once: an update the policy would force shows the banner, which comes back after "Later".
 */
class StoreListingUpdates(
    private val installed: String,
    private val snoozeStore: SnoozeStore,
    private val newest: suspend () -> StoreRelease?,
    private val open: (page: String) -> Unit,
) : AppUpdates {
    private val mutableState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    private var release: StoreRelease? = null

    override val state: StateFlow<UpdateState> = mutableState.asStateFlow()

    override suspend fun check() {
        val now = currentTimeMs()
        val newer = newest()?.takeIf { UpdatePolicy.isNewer(it.version, than = installed) }
        release = newer
        val prompt =
            newer?.let {
                UpdatePolicy.decide(
                    priority = UpdatePolicy.priority(installed, newer.version),
                    stalenessDays = ((now - newer.releasedMs) / DAY_MS).toInt(),
                    snoozedUntilMs = snoozeStore.snoozedUntil(),
                    nowMs = now,
                )
            } ?: UpdatePrompt.NONE
        mutableState.value = if (prompt == UpdatePrompt.NONE) UpdateState.Idle else UpdateState.Available
    }

    override fun download() {
        release?.let { open(it.page) }
    }

    override fun snooze() {
        snoozeStore.snoozeNow()
        mutableState.value = UpdateState.Idle
    }

    override fun install() = Unit

    private companion object {
        const val DAY_MS = 24L * 60 * 60 * 1000
    }
}

/** The App Store's public listing of an app, as its lookup (`itunes.apple.com/lookup?bundleId=…`) answers. */
object AppStoreListing {
    @Serializable
    private data class Lookup(
        val results: List<Entry> = emptyList(),
    )

    @Serializable
    private data class Entry(
        val version: String,
        val currentVersionReleaseDate: String,
        val trackViewUrl: String,
    )

    private val json = Json { ignoreUnknownKeys = true }

    /** The listing's newest version, or `null` when the app isn't in that store or the answer can't be read. */
    fun parse(answer: String): StoreRelease? =
        try {
            json.decodeFromString<Lookup>(answer).results.firstOrNull()?.let {
                StoreRelease(
                    version = it.version,
                    releasedMs = Instant.parse(it.currentVersionReleaseDate).toEpochMilliseconds(),
                    page = it.trackViewUrl,
                )
            }
        } catch (_: IllegalArgumentException) {
            // Not the lookup's JSON, or a date it doesn't write (kotlinx.serialization's errors are among these).
            null
        }
}
