package vadimerenkov.biblioteka.movies.presentation.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.add_movie
import biblioteka.core.presentation.generated.resources.folder
import biblioteka.core.presentation.generated.resources.grid
import biblioteka.core.presentation.generated.resources.grid_view
import biblioteka.core.presentation.generated.resources.list
import biblioteka.core.presentation.generated.resources.list_view
import biblioteka.core.presentation.generated.resources.parse_folder
import biblioteka.core.presentation.generated.resources.show_only_local_movies
import biblioteka.core.presentation.generated.resources.sort_by
import biblioteka.core.presentation.generated.resources.sort_icon
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.movies.presentation.list.components.MovieColumn
import vadimerenkov.biblioteka.movies.presentation.list.components.MovieGrid
import vadimerenkov.biblioteka.movies.presentation.util.toText

@Composable
fun MovieListScreen(
	viewModel: MovieListViewModel = koinViewModel(),
	onMovieClick: (id: String?, tmdbId: Long?) -> Unit,
	onMovieEditClick: (id: String) -> Unit,
	onAddMovieClick: () -> Unit,
) {
	MovieListRoot(
		state = viewModel.state,
		onAction = { action ->
			when (action) {
				is MovieListAction.OnMovieClick -> onMovieClick(action.id, action.tmdbId)
				is MovieListAction.OnEditMovieClick -> onMovieEditClick(action.id)
				is MovieListAction.OnAddMovieClick -> onAddMovieClick()
				else -> Unit
			}
			viewModel.onAction(action)
		}
	)
}

@Composable
private fun MovieListRoot(
	state: MovieListState,
	onAction: (MovieListAction) -> Unit
) {
	val launcher = rememberDirectoryPickerLauncher { directory ->
		if (directory != null) {
			onAction(MovieListAction.OnDirectoryChosen(directory))
		}
	}
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		Row(
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
		) {
			IconButton(
				onClick = {
					onAction(MovieListAction.OnAddMovieClick)
				}
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = stringResource(Res.string.add_movie),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			IconButton(
				onClick = {
					launcher.launch()
				}
			) {
				Icon(
					painter = painterResource(Res.drawable.folder),
					contentDescription = stringResource(Res.string.parse_folder),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			IconButton(
				onClick = {
					onAction(MovieListAction.ChangeView(MovieView.GRID))
				},
			) {
				Icon(
					imageVector = vectorResource(Res.drawable.grid),
					contentDescription = stringResource(Res.string.grid_view),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			IconButton(
				onClick = {
					onAction(MovieListAction.ChangeView(MovieView.LIST))
				},
			) {
				Icon(
					imageVector = vectorResource(Res.drawable.list),
					contentDescription = stringResource(Res.string.list_view),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Box {
				var expanded by remember { mutableStateOf(false) }
				IconButton(
					onClick = {
						expanded = true
					},
				) {
					Icon(
						imageVector = vectorResource(Res.drawable.sort_icon),
						contentDescription = stringResource(Res.string.sort_by),
						tint = MaterialTheme.colorScheme.primary
					)
				}

				DropdownMenu(
					expanded = expanded,
					onDismissRequest = { expanded = false }
				) {
					SortingBy.entries.forEach { sorting ->
						DropdownMenuItem(
							text = {
								Text(
									text = sorting.toText()
								)
							},
							onClick = {
								onAction(MovieListAction.ChangeSortingBy(sorting))
								expanded = false
							}
						)
					}
				}
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.clickable {
						onAction(MovieListAction.ChangeShowLocal(!state.showOnlyLocal))
					}
					.border(
						width = 2.dp,
						color = MaterialTheme.colorScheme.primary,
						shape = RoundedCornerShape(12.dp)
					)
					.padding(start = 4.dp)
			) {
				Text(
					text = stringResource(Res.string.show_only_local_movies),
					color = MaterialTheme.colorScheme.primary
				)
				Checkbox(
					checked = state.showOnlyLocal,
					onCheckedChange = { show ->
						onAction(MovieListAction.ChangeShowLocal(show))
					}
				)
			}
		}
		AnimatedContent(state.view) { view ->
			when (view) {
				MovieView.GRID -> {
					MovieGrid(
						state = state,
						onAction = onAction
					)
				}
				MovieView.LIST -> {
					MovieColumn(
						state = state,
						onAction = onAction
					)
				}
			}
		}
	}
}