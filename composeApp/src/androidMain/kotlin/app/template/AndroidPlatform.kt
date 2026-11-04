package app.template

import android.content.Context
import app.template.update.AppUpdates
import app.template.update.InAppUpdates
import app.template.update.SnoozeStore
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import kotlinx.io.files.Path

/** Android's part of the app: its private folder and Google Play's in-app updates. */
class AndroidPlatform(
    private val context: Context,
) : Platform {
    override val filesDir: Path = Path(context.filesDir.path)

    override fun updates(snooze: SnoozeStore): AppUpdates =
        InAppUpdates(AppUpdateManagerFactory.create(context), snooze)
}
