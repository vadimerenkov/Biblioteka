package vadimerenkov.biblioteka.books.presentation.book_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.domain.BooksRepository
import vadimerenkov.biblioteka.books.presentation.settings.getBookSorting
import vadimerenkov.biblioteka.books.presentation.settings.getBookView
import vadimerenkov.biblioteka.books.presentation.settings.saveBookSorting
import vadimerenkov.biblioteka.books.presentation.settings.saveBookView
import vadimerenkov.biblioteka.core.domain.settings.Settings

class BookListViewModel(
	private val repository: BooksRepository,
	private val settings: Settings
): ViewModel() {

	var state by mutableStateOf(BookListState())

	init {
		repository
			.getAllBooks()
			.onStart {
				val sorting = settings.getBookSorting()
				val view = settings.getBookView()
				state = state.copy(
					sortingBy = sorting,
					view = view
				)
			}
			.onEach { books ->
				state = state.copy(
					books = sortBooks(books)
				)
			}.launchIn(viewModelScope)
	}

	fun onAction(action: BookListAction) {
		when (action) {
			is BookListAction.SortingChange -> {
				state = state.copy(sortingBy = action.sorting)
				val books = sortBooks(state.books)
				state = state.copy(books = books)
				viewModelScope.launch {
					settings.saveBookSorting(action.sorting)
				}
			}
			is BookListAction.ChangeView -> {
				state = state.copy(view = action.view)
				viewModelScope.launch {
					settings.saveBookView(action.view)
				}
			}
			else -> Unit
		}
	}

	private fun sortBooks(books: List<Book>): List<Book> {
		val books = when(state.sortingBy) {
			SortingBy.DATE_FINISHED -> books.sortedByDescending { it.finishedOn }
			SortingBy.RATING -> books.sortedByDescending { it.rating }
			SortingBy.DATE_PUBLISHED -> books.sortedByDescending { it.firstPublishYear }
		}
		return books
	}
}