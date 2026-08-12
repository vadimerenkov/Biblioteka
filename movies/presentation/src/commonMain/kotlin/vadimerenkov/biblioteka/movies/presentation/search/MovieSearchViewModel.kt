package vadimerenkov.biblioteka.movies.presentation.search

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.core.domain.util.onFailure
import vadimerenkov.biblioteka.core.domain.util.onSuccess
import vadimerenkov.biblioteka.movies.domain.MoviesRepository

class MovieSearchViewModel(
	private val initialSearch: String = "",
	private val repository: MoviesRepository
): ViewModel() {

	var state by mutableStateOf(MovieSearchState())

	init {
		if (initialSearch.isNotBlank()) {
			state.searchBarState.setTextAndPlaceCursorAtEnd(initialSearch)
			searchMovie(initialSearch)
		}
	}

	fun onAction(action: MovieSearchAction) {
		when (action) {
			MovieSearchAction.OnSubmitPress -> {
				val query = state.searchBarState.text.toString()
				searchMovie(query)
			}

			is MovieSearchAction.SelectMovie -> {
				state = state.copy(selectedMovie = action.movie)
			}

			else -> Unit
		}
	}

	fun searchMovie(query: String) {
		viewModelScope.launch {
			state = state.copy(isLoading = true)
			repository.searchMovie(query)
				.onSuccess { movies ->
					state = state.copy(searchedMovies = movies, isLoading = false)
				}
				.onFailure {
					state = state.copy(isLoading = false)
				}
		}
	}
}