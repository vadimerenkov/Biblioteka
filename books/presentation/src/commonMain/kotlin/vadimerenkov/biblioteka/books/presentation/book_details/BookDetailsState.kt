package vadimerenkov.biblioteka.books.presentation.book_details

import vadimerenkov.biblioteka.books.domain.Book

data class BookDetailsState(
	val book: Book,
	val showFinishedDialog: Boolean = false
)
