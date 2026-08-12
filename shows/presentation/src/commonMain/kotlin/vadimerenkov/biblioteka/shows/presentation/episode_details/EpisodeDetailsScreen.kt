package vadimerenkov.biblioteka.shows.presentation.episode_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.watch
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.util.backHandler
import vadimerenkov.biblioteka.shows.presentation.util.existsLocally
import vadimerenkov.biblioteka.shows.presentation.util.toText
import kotlin.time.Duration.Companion.minutes

@Composable
fun EpisodeDetailsScreen(
	viewModel: EpisodeDetailsViewModel = koinViewModel(),
	onBackClick: () -> Unit
) {
	EpisodeDetailsRoot(
		state = viewModel.state,
		onAction = viewModel::onAction,
		onBackClick = onBackClick
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodeDetailsRoot(
	state: EpisodeDetailsState,
	onAction: (EpisodeDetailsAction) -> Unit,
	onBackClick: () -> Unit,
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.backHandler {
				onBackClick()
			}
	) {
		IconButton(
			onClick = onBackClick
		) {
			Icon(
				imageVector = Icons.Default.ArrowBack,
				contentDescription = stringResource(Res.string.go_back),
				tint = MaterialTheme.colorScheme.primary
			)
		}
		state.episode?.let { episode ->
			Column(
				verticalArrangement = Arrangement.spacedBy(16.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.width(IntrinsicSize.Max)
			) {
				AsyncImage(
					model = episode.posterPath,
					contentDescription = null,
					modifier = Modifier
						.width(600.dp)
						.aspectRatio(3/2f)
				)
				Button(
					enabled = episode.existsLocally(),
					onClick = {
						onAction(EpisodeDetailsAction.OnWatchClick)
					},
					modifier = Modifier
						.fillMaxWidth()
				) {
					Icon(
						imageVector = Icons.Default.PlayArrow,
						contentDescription = null
					)
					Text(
						text = stringResource(Res.string.watch)
					)
				}
				episode.runtime?.let { runtime ->
					Text(
						text = runtime.minutes.toString(),
						color = MaterialTheme.colorScheme.onBackground,
					)
				}
				var expanded by remember { mutableStateOf(false) }
				ExposedDropdownMenuBox(
					expanded = expanded,
					onExpandedChange = { expanded = it }
				) {
					TextField(
						value = episode.status.toText(),
						onValueChange = {},
						readOnly = true,
						trailingIcon = {
							Icon(
								imageVector = Icons.Default.ArrowDropDown,
								contentDescription = null
							)
						},
						modifier = Modifier
							.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
					)
					ExposedDropdownMenu(
						expanded = expanded,
						onDismissRequest = { expanded = false }
					) {
						CompletionStatus.entries.forEach { status ->
							DropdownMenuItem(
								text = {
									Text(
										text = status.toText()
									)
								},
								onClick = {
									onAction(EpisodeDetailsAction.CompletionStatusChange(status))
									expanded = false
								}
							)
						}
					}
				}
			}
			Column(
				verticalArrangement = Arrangement.spacedBy(16.dp),
				modifier = Modifier
					.padding(16.dp)
			) {
				Row {
					if (episode.episodeNumber != null) {
						Text(
							text = "${episode.episodeNumber}. ",
							color = MaterialTheme.colorScheme.onBackground,
							fontSize = 36.sp
						)
					}
					Text(
						text = episode.title,
						color = MaterialTheme.colorScheme.onBackground,
						fontSize = 36.sp
					)
				}
				episode.description?.let { description ->
					Text(
						text = description,
						color = MaterialTheme.colorScheme.onBackground
					)
				}
			}
		}
	}
}