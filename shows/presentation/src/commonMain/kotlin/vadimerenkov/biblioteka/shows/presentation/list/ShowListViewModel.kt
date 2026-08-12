package vadimerenkov.biblioteka.shows.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.core.domain.settings.Settings
import vadimerenkov.biblioteka.shows.domain.TvRepository
import vadimerenkov.biblioteka.shows.domain.TvShow
import vadimerenkov.biblioteka.shows.presentation.settings.getShowSorting
import vadimerenkov.biblioteka.shows.presentation.settings.getShowView

class ShowListViewModel(
	private val repository: TvRepository,
	private val settings: Settings
): ViewModel() {

	var state by mutableStateOf(ShowListState())

	init {
		repository.getAllShows()
			.onStart {
				val view = settings.getShowView()
				val sorting = settings.getShowSorting()
				state = state.copy(
					view = view,
					sorting = sorting
				)
			}
			.onEach { shows ->
				state = state.copy(
					shows = shows
				)
			}.launchIn(viewModelScope)
	}

	fun onAction(action: ShowListAction) {
		when (action) {
			is ShowListAction.OnFolderSelected -> {
				viewModelScope.launch {
					repository.parseFolderAndSave(action.folder)
				}
			}
			is ShowListAction.ViewChange -> {
				state = state.copy(
					view = action.view
				)
			}
			is ShowListAction.SortingChange -> {
				state = state.copy(
					shows = state.shows.sorted(action.sorting)
				)
			}
			else -> Unit
		}
	}

	private fun List<TvShow>.sorted(sorting: SortingBy): List<TvShow> {
		return when (sorting) {
			SortingBy.ALPHABET -> this.sortedBy { it.title }
			SortingBy.RATING -> this.sortedByDescending { it.rating }
			SortingBy.FIRST_AIR_DATE -> this.sortedByDescending { it.firstAirDate }
			SortingBy.LAST_AIR_DATE -> this.sortedByDescending { it.lastAirDate }
		}
	}
}