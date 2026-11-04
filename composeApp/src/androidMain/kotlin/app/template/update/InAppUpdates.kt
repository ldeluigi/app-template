package app.template.update

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import app.template.currentTimeMs
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallException
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.requestAppUpdateInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Google Play in-app updates (the only update mechanism Play allows). Checked on the home screen only, never during
 * a task; on devices without Play (or debug builds) every check quietly finds nothing.
 */
class InAppUpdates(
    private val manager: AppUpdateManager,
    private val snoozeStore: SnoozeStore,
) : AppUpdates {
    private val mutableState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    private var info: AppUpdateInfo? = null

    /** Starts Play's update screens: registered by the screen showing the home screen ([attach]). */
    private var launcher: ActivityResultLauncher<IntentSenderRequest>? = null

    override val state: StateFlow<UpdateState> = mutableState.asStateFlow()

    /** Uses [launcher] to show Play's update screens, from the activity on screen. */
    fun attach(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        this.launcher = launcher
    }

    private val listener =
        InstallStateUpdatedListener { install ->
            mutableState.value =
                when (install.installStatus()) {
                    InstallStatus.DOWNLOADED -> UpdateState.ReadyToInstall
                    InstallStatus.DOWNLOADING, InstallStatus.PENDING -> UpdateState.Downloading
                    else -> mutableState.value
                }
        }

    override suspend fun check() {
        val current = requestInfo() ?: return
        info = current
        if (current.installStatus() == InstallStatus.DOWNLOADED) {
            mutableState.value = UpdateState.ReadyToInstall
            return
        }
        val inProgress = current.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
        val available = current.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
        val prompt =
            when {
                inProgress -> {
                    UpdatePrompt.IMMEDIATE
                }

                available -> {
                    UpdatePolicy.decide(
                        priority = current.updatePriority(),
                        stalenessDays = current.clientVersionStalenessDays(),
                        snoozedUntilMs = snoozeStore.snoozedUntil(),
                        nowMs = currentTimeMs(),
                    )
                }

                else -> {
                    UpdatePrompt.NONE
                }
            }
        when (prompt) {
            UpdatePrompt.IMMEDIATE -> start(current, AppUpdateType.IMMEDIATE)
            UpdatePrompt.FLEXIBLE -> mutableState.value = UpdateState.Available
            UpdatePrompt.NONE -> mutableState.value = UpdateState.Idle
        }
    }

    /** Downloads the update in the background. */
    override fun download() {
        val current = info ?: return
        manager.registerListener(listener)
        start(current, AppUpdateType.FLEXIBLE)
        mutableState.value = UpdateState.Downloading
    }

    override fun snooze() {
        snoozeStore.snoozeNow()
        mutableState.value = UpdateState.Idle
    }

    override fun install() {
        manager.unregisterListener(listener)
        manager.completeUpdate()
    }

    private fun start(
        current: AppUpdateInfo,
        type: Int,
    ) {
        val launcher = launcher ?: return
        if (current.isUpdateTypeAllowed(type)) {
            manager.startUpdateFlowForResult(current, launcher, AppUpdateOptions.newBuilder(type).build())
        }
    }

    /** What Google Play knows about updates, or `null` where there is no Play to ask. */
    private suspend fun requestInfo(): AppUpdateInfo? =
        try {
            manager.requestAppUpdateInfo()
        } catch (e: CancellationException) {
            throw e
        } catch (_: InstallException) {
            // Not installed from Google Play (debug builds, sideloads): no updates to offer.
            null
        } catch (_: RuntimeException) {
            // No Play Store to bind to (phones and emulators without it): Play throws an internal, obfuscated
            // RuntimeException ("Failed to bind to the service") that has no public type to catch.
            null
        }
}
