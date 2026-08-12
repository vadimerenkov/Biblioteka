package vadimerenkov.biblioteka.movies.presentation.edit

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.absolutePath
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.core.domain.util.saveThumbnail
import vadimerenkov.biblioteka.movies.domain.MoviesRepository

class MovieEditViewModel(
	private val id: String,
	private val repository: MoviesRepository
): ViewModel() {

	private val channel = Channel<MovieEditEvent>()
	val events = channel.receiveAsFlow()

	var state by mutableStateOf(MovieEditState())

	init {
		viewModelScope.launch {
			val movie = repository.getMovie(id)
			state.titleState.setTextAndPlaceCursorAtEnd(movie.title)
			state.descriptionState.setTextAndPlaceCursorAtEnd(movie.description ?: "")
			state.revenueState.setTextAndPlaceCursorAtEnd(movie.revenue?.toString() ?: "")
			state.productionCompaniesState.setTextAndPlaceCursorAtEnd(movie.productionCompanies.joinToString())
			state.originCountriesState.setTextAndPlaceCursorAtEnd(movie.originCountries.joinToString())
			state.originalTitleState.setTextAndPlaceCursorAtEnd(movie.originalTitle ?: "")
			state = state.copy(
				movie = movie
			)
		}
	}

	fun onAction(action: MovieEditAction) {
		when (action) {
			is MovieEditAction.ChangeImageFile -> {
				state = state.copy(movie = state.movie?.copy(posterPath = action.platformFile.absolutePath()))
			}
			MovieEditAction.DismissSearchScreen -> {
				state = state.copy(showSearchScreen = false)
			}
			is MovieEditAction.FinishedDateChange -> {
				state = state.copy(movie = state.movie?.copy(finishedOn = action.date))
			}
			MovieEditAction.OnConfirmClick -> {
				viewModelScope.launch {
					var posterPath = state.movie?.posterPath
					posterPath?.let { path ->
						posterPath = saveThumbnail(path, state.movie!!.id)
					}
					val movie = state.movie?.copy(
						title = state.titleState.text.toString(),
						description = state.descriptionState.text.toString(),
						revenue = state.revenueState.text.toString().trim().toIntOrNull(),
						productionCompanies = state.productionCompaniesState.text.toString().split(", "),
						originCountries = state.originCountriesState.text.toString().split(", "),
						originalTitle = state.originalTitleState.text.toString(),
						posterPath = posterPath
					)
					repository.saveMovie(movie!!)
					channel.send(MovieEditEvent.WritingFinished)
				}
			}
			MovieEditAction.OnSearchClick -> {
				state = state.copy(showSearchScreen = true)
			}
			is MovieEditAction.RatingChange -> {
				state = state.copy(movie = state.movie?.copy(rating = action.rating))
			}
			is MovieEditAction.ReleaseDateChange -> {
				state = state.copy(movie = state.movie?.copy(releaseDate = action.date))
			}
			is MovieEditAction.StartedDateChange -> {
				state = state.copy(movie = state.movie?.copy(startedOn = action.date))
			}
			is MovieEditAction.StatusChange -> {
				state = state.copy(movie = state.movie?.copy(status = action.status))
			}
			is MovieEditAction.LoadSelectedMovie -> {
				state = state.copy(showSearchScreen = false)
				state.movie?.let { movie ->
					viewModelScope.launch {
						val detailedMovie = repository.getDetailsForMovie(movie, action.tmdbId)
						if (detailedMovie != null) {
							state = state.copy(movie = detailedMovie)
							state.titleState.setTextAndPlaceCursorAtEnd(detailedMovie.title)
							state.descriptionState.setTextAndPlaceCursorAtEnd(detailedMovie.description ?: "")
							state.revenueState.setTextAndPlaceCursorAtEnd((detailedMovie.revenue ?: 0).toString())
							state.originalTitleState.setTextAndPlaceCursorAtEnd(detailedMovie.originalTitle ?: "")
							state.originCountriesState.setTextAndPlaceCursorAtEnd(detailedMovie.originCountries.joinToString())
							state.productionCompaniesState.setTextAndPlaceCursorAtEnd(detailedMovie.productionCompanies.joinToString())
						}
					}
				}
			}
		}
	}
}