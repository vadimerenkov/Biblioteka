package vadimerenkov.biblioteka.books.presentation.book_edit

sealed interface BookEditEvent {
	data object WritingFinished: BookEditEvent
}