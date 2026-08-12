package vadimerenkov.biblioteka.movies.presentation.list

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
import vadimerenkov.biblioteka.movies.domain.Movie
import vadimerenkov.biblioteka.movies.domain.MoviesRepository
import vadimerenkov.biblioteka.movies.presentation.settings.getMovieSorting
import vadimerenkov.biblioteka.movies.presentation.settings.getMovieView
import vadimerenkov.biblioteka.movies.presentation.settings.getShowOnlyLocal
import vadimerenkov.biblioteka.movies.presentation.settings.saveMovieSorting
import vadimerenkov.biblioteka.movies.presentation.settings.saveMovieView
import vadimerenkov.biblioteka.movies.presentation.settings.saveShowOnlyLocal

class MovieListViewModel(
	private val repository: MoviesRepository,
	private val settings: Settings
): ViewModel() {

	var state by mutableStateOf(MovieListState())

	init {
		repository
			.getAllMovies()
			.onStart {
				val showLocal = settings.getShowOnlyLocal()
				val view = settings.getMovieView()
				val sorting = settings.getMovieSorting()

				state = state.copy(
					showOnlyLocal = showLocal,
					view = view,
					sortingBy = sorting
				)
			}
			.onEach { movies ->
				state = state.copy(
					movies = movies
				)
			}.launchIn(viewModelScope)
	}

	fun onAction(action: MovieListAction) {
		when (action) {
			is MovieListAction.OnDirectoryChosen -> {
				viewModelScope.launch {
					repository.parseFolderAndSave(action.directory)
				}
			}
			is MovieListAction.ChangeShowLocal -> {
				state = state.copy(
					showOnlyLocal = action.show
				)
				viewModelScope.launch {
					settings.saveShowOnlyLocal(action.show)
				}
			}
			is MovieListAction.ChangeSortingBy -> {
				state = state.copy(
					sortingBy = action.sortingBy,
					movies = state.movies.sorted(action.sortingBy)
				)
				viewModelScope.launch {
					settings.saveMovieSorting(action.sortingBy)
				}
			}
			is MovieListAction.ChangeView -> {
				state = state.copy(
					view = action.view
				)
				viewModelScope.launch {
					settings.saveMovieView(action.view)
				}
			}
			else -> Unit
		}
	}

	private fun List<Movie>.sorted(sortingBy: SortingBy): List<Movie> {
		return when (sortingBy) {
			SortingBy.ALPHABET -> this.sortedBy { it.title }
			SortingBy.DATE_FINISHED -> this.sortedByDescending { it.finishedOn }
			SortingBy.RELEASE_DATE -> this.sortedByDescending { it.releaseDate }
			SortingBy.RATING -> this.sortedByDescending { it.rating }
		}
	}
}