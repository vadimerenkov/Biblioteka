package vadimerenkov.biblioteka.shows.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.confirm
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.search_tmdb
import biblioteka.core.presentation.generated.resources.submit
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.core.presentation.util.backHandler
import vadimerenkov.biblioteka.shows.domain.TvShow

@Composable
fun ShowSearchScreen(
	viewModel: ShowSearchViewModel = koinViewModel(),
	showConfirmButton: Boolean = false,
	onShowSelected: (Long) -> Unit,
	onBackClick: () -> Unit
) {
	ShowSearchRoot(
		state = viewModel.state,
		onAction = { action ->
			when (action) {
				is ShowSearchAction.OnConfirmClick -> {
					onShowSelected(action.tmdbId)
				}
				is ShowSearchAction.OnShowClick -> {
					onShowSelected(action.tmdbId)
				}
				else -> Unit
			}
			viewModel.onAction(action)
		},
		showConfirmButton = showConfirmButton,
		onBackClick = onBackClick
	)
}

@Composable
private fun ShowSearchRoot(
	state: ShowSearchState,
	onAction: (ShowSearchAction) -> Unit,
	showConfirmButton: Boolean = false,
	onBackClick: () -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.backHandler {
				onBackClick()
			}
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.fillMaxSize()
		) {
			Row {
				IconButton(
					onClick = onBackClick
				) {
					Icon(
						imageVector = Icons.Default.ArrowBack,
						contentDescription = stringResource(Res.string.go_back),
						tint = MaterialTheme.colorScheme.primary
					)
				}
				OutlinedTextField(
					state = state.searchBarState,
					placeholder = {
						Text(
							text = stringResource(Res.string.search_tmdb)
						)
					},
					trailingIcon = {
						IconButton(
							onClick = {
								onAction(ShowSearchAction.OnSubmitClick)
							}
						) {
							Icon(
								imageVector = Icons.Default.ArrowForward,
								contentDescription = stringResource(Res.string.submit)
							)
						}
					},
					modifier = Modifier
						.onPreviewKeyEvent {
							when (it.key) {
								Key.Enter -> {
									onAction(ShowSearchAction.OnSubmitClick)
									true
								}

								else -> false
							}
						}
				)
			}
			if (state.isLoading) {
				CircularProgressIndicator()
			} else {
				LazyColumn {
					items(
						items = state.searchedShows,
						key = { it.id }
					) { show ->
						SearchedShowItem(
							show = show,
							onClick = {
								if (showConfirmButton) {
									onAction(ShowSearchAction.SelectShow(show))
								}
								else {
									onAction(ShowSearchAction.OnShowClick(show.tmdbId!!))
								}
							},
							isSelected = state.selectedShow == show
						)
					}
				}
			}
		}
		if (showConfirmButton) {
			Button(
				onClick = {
					state.selectedShow?.let {
						onAction(ShowSearchAction.OnConfirmClick(it.tmdbId!!))
					}
				},
				modifier = Modifier
					.align(Alignment.BottomEnd)
			) {
				Text(
					text = stringResource(Res.string.confirm)
				)
			}
		}
	}
}

@Composable
private fun SearchedShowItem(
	show: TvShow,
	onClick: () -> Unit,
	isSelected: Boolean
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.fillMaxWidth()
			.background(
				if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
			)
			.clickable {
				onClick()
			}
	) {
		AsyncImage(
			model = show.posterPath,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.width(150.dp)
				.aspectRatio(2/3f)
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			Text(
				text = show.title,
				color = MaterialTheme.colorScheme.onBackground
			)
			Row {
				Icon(
					imageVector = Icons.Default.Star,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primary
				)
				Text(
					text = "${show.avgRating} (${show.ratingsCount})",
					color = MaterialTheme.colorScheme.onBackground
				)
			}
		}
	}
}