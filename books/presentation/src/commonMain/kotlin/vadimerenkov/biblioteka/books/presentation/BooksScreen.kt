package vadimerenkov.biblioteka.books.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.presentation.book_details.BookDetailsScreen
import vadimerenkov.biblioteka.books.presentation.book_edit.BookEditScreen
import vadimerenkov.biblioteka.books.presentation.book_edit.BookEditViewModel
import vadimerenkov.biblioteka.books.presentation.book_list.BookListScreen
import vadimerenkov.biblioteka.books.presentation.search_books.SearchBooksScreen
import java.util.UUID

@Composable
fun BooksScreen() {
	val backStack = remember { mutableStateListOf<NavKey>(BookListRoute) }
	NavDisplay(
		backStack = backStack,
		sceneStrategies = listOf(DialogSceneStrategy()),
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator()
		),
		entryProvider = entryProvider {
			entry<BookListRoute> {
				BookListScreen(
					onBookClick = { book ->
						backStack.add(BookDetailsRoute(book))
					},
					onAddClick = {
						backStack.add(BookSearchRoute)
					},
					onEditBookClick = {
						backStack.add(BookEditRoute(it))
					}
				)
			}
			entry<BookSearchRoute>() {
				SearchBooksScreen(
					onBookClick = { book ->
						backStack.add(BookDetailsRoute(book))
					},
					onCustomClick = {
						backStack.add(BookEditRoute(
							Book(
								id = UUID.randomUUID().toString(),
								title = ""
							)
						))
					},
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
			entry<BookEditRoute> {
				BookEditScreen(
					viewModel = koinViewModel<BookEditViewModel> { parametersOf(it.book) },
					onConfirmClick = {
						backStack.removeLastOrNull()
					},
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
			entry<BookDetailsRoute> {
				BookDetailsScreen(
					viewModel = koinViewModel { parametersOf(it.book) },
					onBackClick = {
						backStack.removeLastOrNull()
					}
				)
			}
		}
	)
}