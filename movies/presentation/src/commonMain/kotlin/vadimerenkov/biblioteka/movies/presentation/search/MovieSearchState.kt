package vadimerenkov.biblioteka.movies.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import vadimerenkov.biblioteka.movies.domain.Movie

data class MovieSearchState(
	val isLoading: Boolean = false,
	val searchBarState: TextFieldState = TextFieldState(),
	val searchedMovies: List<Movie> = emptyList(),
	val selectedMovie: Movie? = null
)
