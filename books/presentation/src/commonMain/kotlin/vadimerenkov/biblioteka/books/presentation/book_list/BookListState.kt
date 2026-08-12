package vadimerenkov.biblioteka.books.presentation.book_list

import vadimerenkov.biblioteka.books.domain.Book

data class BookListState(
	val books: List<Book> = emptyList(),
	val sortingBy: SortingBy = SortingBy.DATE_FINISHED,
	val view: BookView = BookView.GRID
)
