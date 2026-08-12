package vadimerenkov.biblioteka.movies.presentation.search

import vadimerenkov.biblioteka.movies.domain.Movie

sealed interface MovieSearchAction {
	data object OnSubmitPress: MovieSearchAction
	data class SelectMovie(val movie: Movie): MovieSearchAction
	data object OnConfirmClick: MovieSearchAction
}