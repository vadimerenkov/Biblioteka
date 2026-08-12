package vadimerenkov.biblioteka.books.presentation.search_books

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.custom
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.search_google
import biblioteka.core.presentation.generated.resources.search_ol
import biblioteka.core.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.presentation.util.toText
import vadimerenkov.biblioteka.core.presentation.util.backHandler

@Composable
fun SearchBooksScreen(
	viewModel: SearchBooksViewModel = koinViewModel(),
	onBookClick: (Book) -> Unit,
	onCustomClick: () -> Unit,
	onBackClick: () -> Unit
) {
	SearchBooksScreenRoot(
		state = viewModel.state,
		onBackClick = onBackClick,
		onAction = { action ->
			when (action) {
				is SearchBooksAction.OnBookClick -> onBookClick(action.book)
				SearchBooksAction.OnCustomClick -> onCustomClick()
				else -> Unit
			}
			viewModel.onAction(action)
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBooksScreenRoot(
	state: SearchBooksState,
	onAction: (SearchBooksAction) -> Unit,
	onBackClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.backHandler {
				onBackClick()
			}
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
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
			OutlinedTextField(
				state = state.searchBarState,
				trailingIcon = {
					IconButton(
						onClick = {
							onAction(SearchBooksAction.OnSubmitPress)
						}
					) {
						Icon(
							imageVector = Icons.Default.ArrowForward,
							contentDescription = stringResource(Res.string.submit)
						)
					}
				},
				placeholder = {
					Text(
						text = when (state.api) {
							BooksApi.OPEN_LIBRARY -> stringResource(Res.string.search_ol)
							BooksApi.GOOGLE -> stringResource(Res.string.search_google)
						}
					)
				},
				modifier = Modifier
					.onPreviewKeyEvent {
						when (it.key) {
							Key.Enter -> {
								onAction(SearchBooksAction.OnSubmitPress)
								true
							}
							else -> false
						}
					}
			)
			var expanded by remember { mutableStateOf(false) }
			ExposedDropdownMenuBox(
				expanded = expanded,
				onExpandedChange = { expanded = it }
			) {
				TextField(
					value = state.api.toText(),
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
					onDismissRequest =  { expanded = false }
				) {
					BooksApi.entries.forEach { api ->
						DropdownMenuItem(
							text = {
								Text(
									text = api.toText()
								)
							},
							onClick = {
								onAction(SearchBooksAction.ApiChange(api))
								expanded = false
							}
						)
					}
				}
			}
			Button(
				onClick = {
					onAction(SearchBooksAction.OnCustomClick)
				}
			) {
				Text(
					text = stringResource(Res.string.custom)
				)
			}
		}
		if (state.isLoading) {
			CircularProgressIndicator()
		} else {
			LazyColumn(
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				items(
					items = state.searchedBooks,
					key = { it.id }
				) { book ->
					SearchedBookItem(
						book = book,
						onAction = onAction
					)
				}
			}
		}
	}
}