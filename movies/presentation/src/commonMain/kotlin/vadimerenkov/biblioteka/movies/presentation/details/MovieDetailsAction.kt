package vadimerenkov.biblioteka.movies.presentation.details

import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

sealed interface MovieDetailsAction {
	data class CompletionStatusChange(val status: CompletionStatus): MovieDetailsAction
	data object DismissDialog: MovieDetailsAction
	data class ConfirmFinishedDate(val date: LocalDate): MovieDetailsAction
	data object OnWatchClick: MovieDetailsAction
	data class OnRatingClick(val rating: Int): MovieDetailsAction
}