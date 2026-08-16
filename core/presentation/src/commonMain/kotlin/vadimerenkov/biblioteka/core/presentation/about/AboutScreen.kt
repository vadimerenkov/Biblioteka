package vadimerenkov.biblioteka.core.presentation.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.download_new_version
import biblioteka.core.presentation.generated.resources.you_have_latest_version
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.autasker.update_checker.UpdateChecker
import vadimerenkov.biblioteka.core.presentation.BuildKonfig

@Composable
fun AboutScreen() {
	val version = BuildKonfig.versionName
	var newVersionAvailable by remember { mutableStateOf(false) }

	LaunchedEffect(true) {
		newVersionAvailable = UpdateChecker.checkForUpdates()
	}

	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Biblioteka v. $version",
				fontSize = 18.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			Text(
				text = "Vadim Erenkov © 2026",
				fontSize = 18.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			val uriHandler = LocalUriHandler.current
			Button(
				enabled = newVersionAvailable,
				onClick = {
					uriHandler.openUri("https://github.com/vadimerenkov/Biblioteka/releases/latest")
				}
			) {
				Text(
					text = if (newVersionAvailable) stringResource(Res.string.download_new_version) else stringResource(Res.string.you_have_latest_version)
				)
			}
		}
	}
}