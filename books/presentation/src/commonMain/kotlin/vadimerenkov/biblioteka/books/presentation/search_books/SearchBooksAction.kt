package vadimerenkov.biblioteka.books.presentation.search_books

import vadimerenkov.biblioteka.books.domain.Book

sealed interface SearchBooksAction {
	data object OnSubmitPress: SearchBooksAction
	data class OnBookClick(val book: Book): SearchBooksAction
	data class ApiChange(val api: BooksApi): SearchBooksAction
	data object OnCustomClick: SearchBooksAction
}