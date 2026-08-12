package vadimerenkov.biblioteka.shows.presentation.search

import vadimerenkov.biblioteka.shows.domain.TvShow

sealed interface ShowSearchAction {
	data object OnSubmitClick: ShowSearchAction
	data class OnConfirmClick(val tmdbId: Long): ShowSearchAction
	data class SelectShow(val show: TvShow): ShowSearchAction
	data class OnShowClick(val tmdbId: Long): ShowSearchAction
}