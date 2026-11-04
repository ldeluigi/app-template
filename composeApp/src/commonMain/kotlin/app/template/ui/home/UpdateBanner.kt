package app.template.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.template.shared.resources.Res
import app.template.shared.resources.update_available
import app.template.shared.resources.update_downloading
import app.template.shared.resources.update_later
import app.template.shared.resources.update_now
import app.template.shared.resources.update_ready
import app.template.shared.resources.update_restart
import app.template.update.UpdateState
import org.jetbrains.compose.resources.stringResource

/** The store's update, when there is one: "Update" or "Later", then progress, then "Restart". */
@Composable
fun UpdateBanner(
    state: UpdateState,
    onUpdate: () -> Unit,
    onLater: () -> Unit,
    onInstall: () -> Unit,
) {
    if (state == UpdateState.Idle) return
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val text =
                when (state) {
                    UpdateState.ReadyToInstall -> Res.string.update_ready
                    UpdateState.Downloading -> Res.string.update_downloading
                    else -> Res.string.update_available
                }
            Text(stringResource(text))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                when (state) {
                    UpdateState.Available -> {
                        TextButton(onClick = onLater) { Text(stringResource(Res.string.update_later)) }
                        TextButton(onClick = onUpdate) { Text(stringResource(Res.string.update_now)) }
                    }

                    UpdateState.ReadyToInstall -> {
                        TextButton(onClick = onInstall) { Text(stringResource(Res.string.update_restart)) }
                    }

                    else -> {}
                }
            }
        }
    }
}
