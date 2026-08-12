package vadimerenkov.biblioteka.shows.presentation.details

import vadimerenkov.biblioteka.shows.domain.TvShow

data class ShowDetailsState(
	val show: TvShow? = null,
	val showFinishedDialog: Boolean = false
)
