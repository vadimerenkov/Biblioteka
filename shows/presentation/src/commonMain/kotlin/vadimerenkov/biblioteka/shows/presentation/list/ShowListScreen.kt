package vadimerenkov.biblioteka.shows.presentation.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.add_show
import biblioteka.core.presentation.generated.resources.folder
import biblioteka.core.presentation.generated.resources.grid
import biblioteka.core.presentation.generated.resources.grid_view
import biblioteka.core.presentation.generated.resources.list
import biblioteka.core.presentation.generated.resources.list_view
import biblioteka.core.presentation.generated.resources.parse_folder
import biblioteka.core.presentation.generated.resources.sort_by
import biblioteka.core.presentation.generated.resources.sort_icon
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.shows.presentation.list.components.ShowColumn
import vadimerenkov.biblioteka.shows.presentation.list.components.ShowGrid
import vadimerenkov.biblioteka.shows.presentation.util.toText

@Composable
fun ShowListScreen(
	viewModel: ShowListViewModel = koinViewModel(),
	onShowClick: (String) -> Unit,
	onEditShowClick: (String) -> Unit,
	onAddShowClick: () -> Unit,
) {
	ShowListScreenRoot(
		state = viewModel.state,
		onAction = { action ->
			when (action) {
				is ShowListAction.OnShowClick -> {
					onShowClick(action.id)
				}
				is ShowListAction.OnEditShowClick -> {
					onEditShowClick(action.id)
				}
				ShowListAction.OnAddShowClick -> {
					onAddShowClick()
				}
				else -> Unit
			}
			viewModel.onAction(action)
		}
	)
}

@Composable
private fun ShowListScreenRoot(
	state: ShowListState,
	onAction: (ShowListAction) -> Unit,
) {

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.background(MaterialTheme.colorScheme.background)
			.fillMaxSize()
	) {
		val launcher = rememberDirectoryPickerLauncher { directory ->
			if (directory != null) {
				onAction(ShowListAction.OnFolderSelected(directory))
			}
		}
		Row {
			IconButton(
				onClick = {
					onAction(ShowListAction.OnAddShowClick)
				}
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = stringResource(Res.string.add_show),
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
					onAction(ShowListAction.ViewChange(ShowView.GRID))
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
					onAction(ShowListAction.ViewChange(ShowView.LIST))
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
								onAction(ShowListAction.SortingChange(sorting))
								expanded = false
							}
						)
					}
				}
			}
		}
		AnimatedContent(state.view) { view ->
			when (view) {
				ShowView.GRID -> {
					ShowGrid(
						state = state,
						onAction = onAction
					)
				}
				ShowView.LIST -> {
					ShowColumn(
						state = state,
						onAction = onAction
					)
				}
			}
		}
	}
}