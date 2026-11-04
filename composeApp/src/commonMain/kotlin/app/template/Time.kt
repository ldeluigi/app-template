package app.template

import kotlin.time.Clock

/** The wall clock in milliseconds since 1970, as the app stores and compares times. */
fun currentTimeMs(): Long = Clock.System.now().toEpochMilliseconds()
