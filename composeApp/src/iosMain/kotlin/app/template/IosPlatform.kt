package app.template

import app.template.update.AppUpdates
import app.template.update.SnoozeStore
import app.template.update.StoreListingUpdates
import app.template.update.appStoreRelease
import kotlinx.io.files.Path
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIApplication

/** iOS's part of the app: its private folder and the App Store's updates. */
class IosPlatform : Platform {
    override val filesDir: Path = Path(applicationSupport())

    override fun updates(snooze: SnoozeStore): AppUpdates =
        StoreListingUpdates(versionName, snooze, { appStoreRelease(bundleId) }, ::open)

    private val bundleId: String get() = checkNotNull(NSBundle.mainBundle.bundleIdentifier) { "No bundle id" }

    private fun applicationSupport(): String {
        @Suppress("UNCHECKED_CAST")
        val folders = NSFileManager.defaultManager.URLsForDirectory(NSApplicationSupportDirectory, NSUserDomainMask)
        return checkNotNull((folders.firstOrNull() as? NSURL)?.path) { "No Application Support folder" }
    }
}

/** Opens [link] in its app, or in the browser. */
internal fun open(link: String) {
    NSURL.URLWithString(link)?.let { UIApplication.sharedApplication.openURL(it, emptyMap<Any?, Any>(), null) }
}
