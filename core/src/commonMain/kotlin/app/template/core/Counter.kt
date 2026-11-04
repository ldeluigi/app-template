package app.template.core

/**
 * A tally: the template's sample of pure logic, shared by Android and iOS and tested on both. Replace it with the app's
 * own model: plain Kotlin here, no platform and no UI.
 */
data class Counter(
    val count: Int = 0,
) {
    /** One more. */
    fun increment(): Counter = copy(count = count + 1)

    /** Back to zero. */
    fun reset(): Counter = Counter()
}
