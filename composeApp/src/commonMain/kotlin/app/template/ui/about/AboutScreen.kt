package app.template.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.template.shared.AppInfo
import app.template.shared.resources.Res
import app.template.shared.resources.back
import app.template.shared.resources.version
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import org.jetbrains.compose.resources.stringResource

/** The app's name and version, and the licences of every library it ships, which the stores ask apps to show. */
@Composable
fun AboutScreen(
    versionName: String,
    onBack: () -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.safeDrawingPadding().padding(16.dp)) {
            TextButton(onClick = onBack) { Text(stringResource(Res.string.back)) }
            Text(AppInfo.NAME, style = MaterialTheme.typography.headlineSmall)
            Text(stringResource(Res.string.version, versionName))
            // The build writes the shipped libraries' licences into the resources (AboutLibraries)
            val libraries by produceLibraries { Res.readBytes("files/aboutlibraries.json").decodeToString() }
            LibrariesContainer(libraries, Modifier.fillMaxSize())
        }
    }
}
