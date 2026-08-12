package vadimerenkov.biblioteka.books.presentation.book_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.domain.BooksRepository
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

class BookDetailsViewModel(
	private val repository: BooksRepository,
	book: Book,
): ViewModel() {

	var state by mutableStateOf(BookDetailsState(book))

	init {
		viewModelScope.launch {
			val dbBook = repository.getBook(book.id)
			if (dbBook != null) {
				state = state.copy(
					book = dbBook
				)
			}
		}
	}

	fun onAction(action: BookDetailsAction) {
		when (action) {
			is BookDetailsAction.OnRatingClick -> {
				val book = state.book.copy(
					rating = action.rating
				)
				state = state.copy(
					book = book
				)
				viewModelScope.launch {
					repository.saveBook(book)
				}
			}
			is BookDetailsAction.StatusChange -> {
				when (action.status) {
					CompletionStatus.NOT_STARTED,
					CompletionStatus.WANT_TO,
					CompletionStatus.DROPPED -> {
						val book = state.book.copy(
							status = action.status
						)
						state = state.copy(
							book = book
						)
						viewModelScope.launch {
							repository.saveBook(book)
						}
					}
					CompletionStatus.STARTED -> {
						val book = state.book.copy(
							startedOn = LocalDate.now(),
							status = CompletionStatus.STARTED
						)
						state = state.copy(
							book = book
						)
						viewModelScope.launch {
							repository.saveBook(book)
						}
					}
					CompletionStatus.FINISHED -> {
						state = state.copy(showFinishedDialog = true)
					}
				}
			}

			BookDetailsAction.DismissDialog -> {
				state = state.copy(showFinishedDialog = false)
			}

			is BookDetailsAction.ConfirmFinishedDate -> {
				val book = state.book.copy(
					status = CompletionStatus.FINISHED,
					finishedOn = action.date
				)
				state = state.copy(book = book)
				viewModelScope.launch {
					repository.saveBook(book)
				}
			}
		}
	}
}