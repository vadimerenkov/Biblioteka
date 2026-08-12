package vadimerenkov.biblioteka.shows.presentation.details

import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

sealed interface ShowDetailsAction {
	data class CompletionStatusChange(val status: CompletionStatus): ShowDetailsAction
	data class OnRatingClick(val rating: Int): ShowDetailsAction
	data class OnPlayClick(val localPath: String?): ShowDetailsAction
	data object DismissDialog: ShowDetailsAction
	data class ConfirmFinishedDate(val date: LocalDate): ShowDetailsAction
	data class OnEpisodeClick(val id: String): ShowDetailsAction
}