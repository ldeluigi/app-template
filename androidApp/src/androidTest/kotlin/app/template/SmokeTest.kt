package app.template

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Runs on an emulator in CI: the real app starts, shows its home screen and counts. */
@RunWith(AndroidJUnit4::class)
class SmokeTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchesAndCounts() {
        compose.onNodeWithText("Count").assertIsDisplayed().performClick()
        compose.onNodeWithText("Counted 1 time").assertIsDisplayed()
    }
}
