package vadimerenkov.biblioteka.books.presentation.search_books

import androidx.compose.foundation.text.input.TextFieldState
import vadimerenkov.biblioteka.books.domain.Book

data class SearchBooksState(
	val isLoading: Boolean = false,
	val searchBarState: TextFieldState = TextFieldState(),
	val searchedBooks: List<Book> = emptyList(),
	val api: BooksApi = BooksApi.OPEN_LIBRARY
)
