package app.template

import org.junit.Test

class BuildVersionTest {
    @Test
    fun theBuildHasASemanticVersion() {
        assert(Regex("""\d+\.\d+\.\d+.*""").matches(BuildConfig.VERSION_NAME)) { BuildConfig.VERSION_NAME }
    }
}
