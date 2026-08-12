package vadimerenkov.biblioteka.shows.presentation.search

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.core.domain.util.onFailure
import vadimerenkov.biblioteka.core.domain.util.onSuccess
import vadimerenkov.biblioteka.shows.domain.TvRepository

class ShowSearchViewModel(
	initialSearch: String = "",
	private val repository: TvRepository
): ViewModel() {

	var state by mutableStateOf(ShowSearchState())

	init {
		if (initialSearch.isNotBlank()) {
			state.searchBarState.setTextAndPlaceCursorAtEnd(initialSearch)
			onAction(ShowSearchAction.OnSubmitClick)
		}
	}

	fun onAction(action: ShowSearchAction) {
		when (action) {
			ShowSearchAction.OnSubmitClick -> {
				state = state.copy(isLoading = true)
				viewModelScope.launch {
					repository.searchTvShow(state.searchBarState.text.toString())
						.onSuccess { shows ->
							state = state.copy(
								searchedShows = shows,
								isLoading = false
							)
						}
						.onFailure {
							state = state.copy(
								isLoading = false
							)
						}
				}
			}
			is ShowSearchAction.SelectShow -> {
				state = state.copy(selectedShow = action.show)
			}
			else -> Unit
		}
	}
}