package app.template.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.template.shared.AppInfo
import app.template.shared.R
import app.template.ui.home.HomeScreen
import app.template.ui.theme.AppTheme
import app.template.update.NoUpdates
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs

/**
 * Draws the store listing's pictures into `composeApp/build/store-art` with the app's own code: the store icon, the
 * feature graphic and the phone screenshots, without transparency, as the stores ask. After each release the
 * machinery's store-listing workflow uploads the ones that changed, so they must come out the same, pixel for pixel,
 * until the app itself changes. It also keeps the iPhone app's icon in step with the launcher icon.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [35], qualifiers = "w360dp-h640dp-xxhdpi")
class StoreArtTest {
    @get:Rule
    val compose = createComposeRule()

    /** 512 × 512 px: the launcher icon, as Google Play shows it. */
    @Test
    fun storeIcon() = save(launcherIcon(ICON_PX), "store-icon.png")

    /**
     * 1024 × 1024 px: the same icon for the iPhone app, which keeps it in its asset catalogue. A change of the launcher
     * icon redraws it there and fails once, to be committed.
     */
    @Test
    fun iPhoneIcon() {
        val file = File("../iosApp/App/Assets.xcassets/AppIcon.appiconset/AppIcon.png")
        val drawn = opaque(launcherIcon(IPHONE_ICON_PX))
        val kept = file.takeIf { it.isFile }?.let(ImageIO::read)
        if (kept == null || difference(kept, drawn) > ICON_TOLERANCE) {
            ImageIO.write(drawn, "png", file)
            error("The iPhone app icon was out of date: it has been redrawn in $file, commit it")
        }
    }

    /** 1024 × 500 px: the icon and the app's name. */
    @Test
    fun featureGraphic() =
        render("feature-graphic.png", FEATURE_WIDTH, FEATURE_HEIGHT) {
            Row(
                Modifier.size(FEATURE_WIDTH, FEATURE_HEIGHT).background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(launcherIcon(ICON_PX).asImageBitmap(), null, Modifier.size(100.dp))
                Text(AppInfo.NAME, style = MaterialTheme.typography.displayMedium)
            }
        }

    /** 1080 × 1920 px: the home screen. */
    @Test
    fun homeScreenshot() =
        render("phone-screenshots/1-home.png", PHONE_WIDTH, PHONE_HEIGHT) {
            HomeScreen(NoUpdates(), onAbout = {})
        }

    /** The launcher icon, [px] wide. Its layers are cropped to the part a launcher shows (72 of their 108 units). */
    private fun launcherIcon(px: Int): Bitmap {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val bitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val layer = px * ADAPTIVE_LAYER / ADAPTIVE_VISIBLE
        val inset = (layer - px) / 2
        listOf(R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground).forEach { id ->
            val drawable = checkNotNull(ResourcesCompat.getDrawable(context.resources, id, null))
            drawable.setBounds(-inset, -inset, layer - inset, layer - inset)
            drawable.draw(canvas)
        }
        return bitmap
    }

    private fun render(
        name: String,
        width: Dp,
        height: Dp,
        content: @Composable () -> Unit,
    ) {
        compose.setContent {
            AppTheme {
                Box(Modifier.size(width, height).testTag(ART)) { content() }
            }
        }
        compose.waitForIdle()
        save(compose.onNodeWithTag(ART).captureToImage().asAndroidBitmap(), name)
    }

    /** Writes [bitmap] as a 24-bit PNG: the stores refuse transparency in their pictures. */
    private fun save(
        bitmap: Bitmap,
        name: String,
    ) {
        val file = File("build/store-art/$name").apply { parentFile?.mkdirs() }
        ImageIO.write(opaque(bitmap), "png", file)
        check(file.length() < MAX_BYTES) { "$name is ${file.length()} bytes: the stores take at most $MAX_BYTES" }
    }

    private fun opaque(bitmap: Bitmap): BufferedImage {
        val opaque = BufferedImage(bitmap.width, bitmap.height, BufferedImage.TYPE_INT_RGB)
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        opaque.setRGB(0, 0, bitmap.width, bitmap.height, pixels, 0, bitmap.width)
        return opaque
    }

    /** How far apart two images are, from 0 (the same) to 1: anti-aliasing differs a little between machines. */
    private fun difference(
        a: BufferedImage,
        b: BufferedImage,
    ): Double {
        if (a.width != b.width || a.height != b.height) return 1.0
        var total = 0L
        for (y in 0 until a.height) {
            for (x in 0 until a.width) {
                val p = a.getRGB(x, y)
                val q = b.getRGB(x, y)
                total += CHANNELS.sumOf { shift -> abs((p shr shift and BYTE) - (q shr shift and BYTE)).toLong() }
            }
        }
        return total.toDouble() / (a.width.toLong() * a.height * CHANNELS.size * BYTE)
    }

    private companion object {
        const val ART = "art"
        const val ICON_PX = 512
        const val IPHONE_ICON_PX = 1024
        const val ICON_TOLERANCE = 0.01
        const val BYTE = 0xFF
        val CHANNELS = listOf(16, 8, 0)
        const val ADAPTIVE_LAYER = 108
        const val ADAPTIVE_VISIBLE = 72
        const val MAX_BYTES = 8_000_000L
        val FEATURE_WIDTH = 1024.dp / 3
        val FEATURE_HEIGHT = 500.dp / 3
        val PHONE_WIDTH = 360.dp
        val PHONE_HEIGHT = 640.dp
    }
}
