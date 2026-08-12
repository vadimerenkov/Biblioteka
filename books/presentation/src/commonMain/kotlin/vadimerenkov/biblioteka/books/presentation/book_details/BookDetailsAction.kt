package vadimerenkov.biblioteka.books.presentation.book_details

import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

sealed interface BookDetailsAction {
	data class OnRatingClick(val rating: Int): BookDetailsAction
	data class StatusChange(val status: CompletionStatus): BookDetailsAction
	data object DismissDialog: BookDetailsAction
	data class ConfirmFinishedDate(val date: LocalDate): BookDetailsAction
}