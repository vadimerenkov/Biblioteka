package vadimerenkov.biblioteka.books.presentation.book_list

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
import biblioteka.core.presentation.generated.resources.add_new_book
import biblioteka.core.presentation.generated.resources.grid
import biblioteka.core.presentation.generated.resources.grid_view
import biblioteka.core.presentation.generated.resources.list
import biblioteka.core.presentation.generated.resources.list_view
import biblioteka.core.presentation.generated.resources.sort_by
import biblioteka.core.presentation.generated.resources.sort_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.presentation.book_list.components.BookGrid
import vadimerenkov.biblioteka.books.presentation.book_list.components.BookListColumn
import vadimerenkov.biblioteka.books.presentation.util.toText

@Composable
fun BookListScreen(
	viewModel: BookListViewModel = koinViewModel(),
	onBookClick: (Book) -> Unit,
	onEditBookClick: (Book) -> Unit,
	onAddClick: () -> Unit
) {
	BookListRoot(
		state = viewModel.state,
		onAction = { action ->
			when (action) {
				is BookListAction.OnBookClick -> onBookClick(action.book)
				BookListAction.OnAddClick -> onAddClick()
				is BookListAction.OnEditBookClick -> onEditBookClick(action.book)
				else -> Unit
			}
			viewModel.onAction(action)
		}
	)
}

@Composable
private fun BookListRoot(
	state: BookListState,
	onAction: (BookListAction) -> Unit,
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		Row {
			IconButton(
				onClick = {
					onAction(BookListAction.OnAddClick)
				},
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = stringResource(Res.string.add_new_book),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			IconButton(
				onClick = {
					onAction(BookListAction.ChangeView(BookView.GRID))
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
					onAction(BookListAction.ChangeView(BookView.LIST))
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
								onAction(BookListAction.SortingChange(sorting))
								expanded = false
							}
						)
					}
				}
			}
		}
		AnimatedContent(
			targetState = state.view
		) { view ->
			when (view) {
				BookView.GRID -> {
					BookGrid(
						state = state,
						onAction = onAction
					)
				}
				BookView.LIST -> {
					BookListColumn(
						state = state,
						onAction = onAction
					)
				}
			}
		}

	}
}

