package vadimerenkov.biblioteka.books.presentation.book_list

import vadimerenkov.biblioteka.books.domain.Book

sealed interface BookListAction {
	data class OnBookClick(val book: Book): BookListAction
	data object OnAddClick: BookListAction
	data class OnEditBookClick(val book: Book): BookListAction
	data class SortingChange(val sorting: SortingBy): BookListAction
	data class ChangeView(val view: BookView): BookListAction
}