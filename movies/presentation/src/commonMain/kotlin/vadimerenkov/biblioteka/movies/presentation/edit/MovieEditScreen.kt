package vadimerenkov.biblioteka.movies.presentation.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.confirm
import biblioteka.core.presentation.generated.resources.description
import biblioteka.core.presentation.generated.resources.finished_on
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.origin_countries
import biblioteka.core.presentation.generated.resources.original_title
import biblioteka.core.presentation.generated.resources.production_companies
import biblioteka.core.presentation.generated.resources.release_date
import biblioteka.core.presentation.generated.resources.revenue
import biblioteka.core.presentation.generated.resources.search_tmdb
import biblioteka.core.presentation.generated.resources.started_on
import biblioteka.core.presentation.generated.resources.title
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.FormDateField
import vadimerenkov.biblioteka.core.presentation.components.FormTextField
import vadimerenkov.biblioteka.core.presentation.util.backHandler
import vadimerenkov.biblioteka.movies.presentation.search.MovieSearchScreen
import vadimerenkov.biblioteka.movies.presentation.search.MovieSearchViewModel
import vadimerenkov.biblioteka.movies.presentation.util.toText

@Composable
fun MovieEditScreen(
	viewModel: MovieEditViewModel = koinViewModel(),
	onBackClick: () -> Unit,
) {
	LaunchedEffect(viewModel.events) {
		withContext(Dispatchers.Main.immediate) {
			viewModel.events.collect { event ->
				when (event) {
					MovieEditEvent.WritingFinished -> onBackClick()
				}
			}
		}
	}

	MovieEditRoot(
		state = viewModel.state,
		onAction = viewModel::onAction,
		onBackClick = onBackClick
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MovieEditRoot(
	state: MovieEditState,
	onAction: (MovieEditAction) -> Unit,
	onBackClick: () -> Unit
) {
	if (state.showSearchScreen) {
		Dialog(
			onDismissRequest = {
				onAction(MovieEditAction.DismissSearchScreen)
			}
		) {
			Window(
				onCloseRequest = {
					onAction(MovieEditAction.DismissSearchScreen)
				}
			) {
				val viewModel: MovieSearchViewModel = koinViewModel { parametersOf(state.titleState.text.toString()) }
				MovieSearchScreen(
					viewModel = viewModel,
					showConfirmButton = true,
					onConfirmClick = { tmdbId ->
						onAction(MovieEditAction.LoadSelectedMovie(tmdbId))
					},
					onMovieClick = {},
					onBackClick = {
						onAction(MovieEditAction.DismissSearchScreen)
					}
				)
			}
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.backHandler {
				onBackClick()
			}
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
		) {
			IconButton(
				onClick = onBackClick
			) {
				Icon(
					imageVector = Icons.Default.KeyboardArrowLeft,
					contentDescription = stringResource(Res.string.go_back),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			IconButton(
				onClick = {
					onAction(MovieEditAction.OnSearchClick)
				}
			) {
				Icon(
					imageVector = Icons.Default.Search,
					contentDescription = stringResource(Res.string.search_tmdb),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(modifier = Modifier.weight(1f))
		}
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			val launcher = rememberFilePickerLauncher(
				type = FileKitType.Image
			) { image ->
				if (image != null) {
					onAction(MovieEditAction.ChangeImageFile(image))
				}
			}
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.height(350.dp)
						.aspectRatio(2 / 3f)
						.border(
							width = 2.dp,
							color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
							shape = RoundedCornerShape(16.dp)
						)
						.clip(RoundedCornerShape(16.dp))
						.clickable {
							launcher.launch()
						}

				) {
					if (state.movie?.posterPath == null) {
						Column(
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							Icon(
								imageVector = Icons.Default.Add,
								contentDescription = null,
								tint = MaterialTheme.colorScheme.onBackground
							)
							Text(
								text = "Add cover image",
								color = MaterialTheme.colorScheme.onBackground
							)
						}
					} else {
						AsyncImage(
							model = state.movie.posterPath,
							contentDescription = null,
							contentScale = ContentScale.Crop
						)
					}
				}
				var expanded by remember { mutableStateOf(false) }
				ExposedDropdownMenuBox(
					expanded = expanded,
					onExpandedChange = { expanded = it }
				) {
					TextField(
						value = state.movie?.status!!.toText(),
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
									onAction(MovieEditAction.StatusChange(status))
									expanded = false
								}
							)
						}
					}
				}
				Row {
					repeat(10) { star ->
						TooltipArea(
							tooltip = {
								Surface(
									modifier = Modifier.shadow(4.dp),
									color = Color(255, 255, 210),
									shape = RoundedCornerShape(4.dp)
								) {
									Text(
										text = "${star + 1}",
										modifier = Modifier.padding(10.dp)
									)
								}
							},
							delayMillis = 0
						) {
							Icon(
								imageVector = Icons.Default.Star,
								contentDescription = null,
								tint = if (state.movie?.rating != null && state.movie.rating!! >= star + 1) MaterialTheme.colorScheme.primary else Color.Gray,
								modifier = Modifier
									.clickable {
										onAction(MovieEditAction.RatingChange(star + 1))
									}
							)
						}
					}
				}
			}
			Column(
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				FormTextField(
					text = stringResource(Res.string.title),
					state = state.titleState
				)
				FormTextField(
					text = stringResource(Res.string.original_title),
					state = state.originalTitleState
				)
				FormTextField(
					text = stringResource(Res.string.description),
					state = state.descriptionState
				)
				FormDateField(
					text = stringResource(Res.string.release_date),
					date = state.movie?.releaseDate,
					onDateChange = { date ->
						onAction(MovieEditAction.ReleaseDateChange(date))
					}
				)
				FormDateField(
					text = stringResource(Res.string.started_on),
					date = state.movie?.startedOn,
					onDateChange = { date ->
						onAction(MovieEditAction.StartedDateChange(date))
					}
				)
				FormDateField(
					text = stringResource(Res.string.finished_on),
					date = state.movie?.finishedOn,
					onDateChange = { date ->
						onAction(MovieEditAction.FinishedDateChange(date))
					}
				)

				Button(
					onClick = {
						onAction(MovieEditAction.OnConfirmClick)
					},
					enabled = state.isValid
				) {
					Text(
						text = stringResource(Res.string.confirm)
					)
				}
			}
			Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
				FormTextField(
					text = stringResource(Res.string.revenue),
					state = state.revenueState
				)
				FormTextField(
					text = stringResource(Res.string.origin_countries),
					state = state.originCountriesState,
				)
				FormTextField(
					text = stringResource(Res.string.production_companies),
					state = state.productionCompaniesState
				)
			}
		}
	}
}