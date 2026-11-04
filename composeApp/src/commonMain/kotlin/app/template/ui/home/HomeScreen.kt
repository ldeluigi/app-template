package app.template.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.template.shared.AppInfo
import app.template.shared.resources.Res
import app.template.shared.resources.about
import app.template.shared.resources.count
import app.template.shared.resources.counted
import app.template.shared.resources.reset
import app.template.update.AppUpdates
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/** The first screen: the app's name, the sample counter, the store's update banner and the way to About. */
@Composable
fun HomeScreen(
    updates: AppUpdates,
    onAbout: () -> Unit,
    model: HomeViewModel = viewModel { HomeViewModel() },
) {
    val counter by model.counter.collectAsStateWithLifecycle()
    val update by updates.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { updates.check() }
    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier.safeDrawingPadding().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UpdateBanner(update, onUpdate = updates::download, onLater = updates::snooze, onInstall = updates::install)
            Text(AppInfo.NAME, style = MaterialTheme.typography.headlineLarge)
            Text(pluralStringResource(Res.plurals.counted, counter.count, counter.count))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = model::increment) { Text(stringResource(Res.string.count)) }
                OutlinedButton(onClick = model::reset) { Text(stringResource(Res.string.reset)) }
            }
            TextButton(onClick = onAbout) { Text(stringResource(Res.string.about)) }
        }
    }
}
