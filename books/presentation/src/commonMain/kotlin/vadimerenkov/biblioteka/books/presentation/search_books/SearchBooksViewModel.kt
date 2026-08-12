package vadimerenkov.biblioteka.books.presentation.search_books

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.books.domain.BooksRepository
import vadimerenkov.biblioteka.core.domain.util.onFailure
import vadimerenkov.biblioteka.core.domain.util.onSuccess

class SearchBooksViewModel(
	private val booksRepository: BooksRepository
): ViewModel() {

	var state by mutableStateOf(SearchBooksState())
		private set

	fun onAction(action: SearchBooksAction) {
		when (action) {
			SearchBooksAction.OnSubmitPress -> searchBooks()
			is SearchBooksAction.ApiChange -> {
				state = state.copy(api = action.api)
			}
			else -> Unit
		}
	}

	private fun searchBooks() {
		state = state.copy(isLoading = true)
		viewModelScope.launch {
			val query = state.searchBarState.text.toString()
			if (query.isNotBlank()) {
				when (state.api) {
					BooksApi.OPEN_LIBRARY -> {
						booksRepository.searchBooksOpenLibrary(query)
							.onSuccess { books ->
								state = state.copy(searchedBooks = books, isLoading = false)
							}
							.onFailure {
								state = state.copy(isLoading = false)
							}
					}
					BooksApi.GOOGLE -> {
						booksRepository.searchBooksGoogle(query)
							.onSuccess { books ->
								state = state.copy(searchedBooks = books, isLoading = false)
							}
							.onFailure {
								state = state.copy(isLoading = false)
							}
					}
				}
			}
		}
	}
}