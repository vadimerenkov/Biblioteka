package vadimerenkov.biblioteka.shows.presentation.episode_details

import vadimerenkov.biblioteka.core.domain.CompletionStatus

sealed interface EpisodeDetailsAction {
	data object OnWatchClick: EpisodeDetailsAction
	data class CompletionStatusChange(val status: CompletionStatus): EpisodeDetailsAction
}