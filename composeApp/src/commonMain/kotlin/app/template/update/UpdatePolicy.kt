package app.template.update

/** How insistently to offer an available update. */
enum class UpdatePrompt {
    /** Say nothing (Play's own auto-update will usually install it). */
    NONE,

    /** Show a banner; the update downloads in the background and installs on restart. */
    FLEXIBLE,

    /** Block the app until the update is installed. */
    IMMEDIATE,
}

/**
 * Decides how to offer an update. The release pipeline sets the update priority from the release type:
 * major = 5, feature = 3, fix or dependency bump = 1. Stores without priorities have it told from the version numbers.
 */
object UpdatePolicy {
    /** The priority the release pipeline gives a major release. */
    const val MAJOR_PRIORITY = 5

    /** The priority the release pipeline gives a feature release. */
    const val FEATURE_PRIORITY = 3

    /** The priority the release pipeline gives a fix or a dependency bump. */
    const val FIX_PRIORITY = 1

    /** Priority from which an update is forced (major releases). */
    const val IMMEDIATE_PRIORITY = 4

    /** Priority from which a banner is shown straight away (feature releases). */
    const val BANNER_PRIORITY = 3

    /** Days after which even a low-priority update is offered with a banner. */
    const val QUIET_DAYS = 14

    /** Days after which a flexible update becomes immediate. */
    const val ESCALATION_DAYS = 30

    /** How long "Later" hides the banner. */
    const val SNOOZE_MILLIS = 3L * 24 * 60 * 60 * 1000

    /** The prompt for an update with [priority], available for [stalenessDays] days, given an optional snooze. */
    fun decide(
        priority: Int,
        stalenessDays: Int?,
        snoozedUntilMs: Long?,
        nowMs: Long,
    ): UpdatePrompt {
        val days = stalenessDays ?: 0
        val wanted =
            when {
                priority >= IMMEDIATE_PRIORITY -> UpdatePrompt.IMMEDIATE
                days >= ESCALATION_DAYS -> UpdatePrompt.IMMEDIATE
                priority >= BANNER_PRIORITY || days >= QUIET_DAYS -> UpdatePrompt.FLEXIBLE
                else -> UpdatePrompt.NONE
            }
        val snoozed = snoozedUntilMs != null && nowMs < snoozedUntilMs
        return if (wanted == UpdatePrompt.FLEXIBLE && snoozed) UpdatePrompt.NONE else wanted
    }

    /** The priority the release pipeline gave [newest], told from its version number and the [installed] one's. */
    fun priority(
        installed: String,
        newest: String,
    ): Int {
        val (major, feature) = numbers(installed)
        val (newMajor, newFeature) = numbers(newest)
        return when {
            newMajor != major -> MAJOR_PRIORITY
            newFeature != feature -> FEATURE_PRIORITY
            else -> FIX_PRIORITY
        }
    }

    /** Whether [version] comes after [than], number by number: `1.10.0` comes after `1.9.3`. */
    fun isNewer(
        version: String,
        than: String,
    ): Boolean =
        numbers(version)
            .zip(numbers(than))
            .firstOrNull { (new, old) -> new != old }
            ?.let { (new, old) -> new > old } ?: false

    /** A version's major, minor and patch numbers; what isn't a number counts as 0. */
    private fun numbers(version: String): List<Int> =
        version
            .split('.')
            .map { it.toIntOrNull() ?: 0 }
            .plus(List(VERSION_NUMBERS) { 0 })
            .take(VERSION_NUMBERS)

    private const val VERSION_NUMBERS = 3
}
