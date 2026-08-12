package vadimerenkov.biblioteka.shows.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import vadimerenkov.biblioteka.shows.domain.TvShow

data class ShowSearchState(
	val isLoading: Boolean = false,
	val searchBarState: TextFieldState = TextFieldState(),
	val searchedShows: List<TvShow> = emptyList(),
	val selectedShow: TvShow? = null
)
