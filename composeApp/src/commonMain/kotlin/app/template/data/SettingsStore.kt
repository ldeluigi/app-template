package app.template.data

import app.template.update.SnoozeStore
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/** What the app remembers between launches. Add fields with defaults: older files still read. */
@Serializable
data class Settings(
    /** Until when the update banner stays hidden, after "Later". */
    val updateSnoozedUntil: Long? = null,
)

/** [Settings] in a JSON file of the app's private folder, read once and written on every change. */
class SettingsStore(
    dir: Path,
) : SnoozeStore {
    private val file = Path(dir, "settings.json")
    private val json = Json { ignoreUnknownKeys = true }

    /** The current settings. */
    var settings: Settings = read()
        private set

    /** Changes the settings and saves them. */
    fun update(change: (Settings) -> Settings) {
        settings = change(settings)
        file.parent?.let { SystemFileSystem.createDirectories(it) }
        SystemFileSystem.sink(file).buffered().use { it.writeString(json.encodeToString(settings)) }
    }

    override fun snoozedUntil(): Long? = settings.updateSnoozedUntil

    override fun snooze(untilMs: Long) = update { it.copy(updateSnoozedUntil = untilMs) }

    private fun read(): Settings =
        try {
            if (SystemFileSystem.exists(file)) {
                json.decodeFromString(SystemFileSystem.source(file).buffered().use { it.readString() })
            } else {
                Settings()
            }
        } catch (_: SerializationException) {
            // A damaged file must not stop the app: it starts again from the defaults.
            Settings()
        }
}
