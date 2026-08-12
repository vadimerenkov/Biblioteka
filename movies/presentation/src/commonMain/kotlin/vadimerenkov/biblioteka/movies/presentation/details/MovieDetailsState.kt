package vadimerenkov.biblioteka.movies.presentation.details

import vadimerenkov.biblioteka.movies.domain.Movie

data class MovieDetailsState(
	val movie: Movie? = null,
	val showFinishedDialog: Boolean = false
)
