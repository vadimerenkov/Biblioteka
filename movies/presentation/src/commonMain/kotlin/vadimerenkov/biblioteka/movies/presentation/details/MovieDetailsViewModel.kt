package vadimerenkov.biblioteka.movies.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import io.github.vinceglb.filekit.exists
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.movies.domain.Movie
import vadimerenkov.biblioteka.movies.domain.MoviesRepository
import java.time.LocalDate
import java.util.UUID

class MovieDetailsViewModel(
	private val id: String? = null,
	private val tmdbId: Long? = null,
	private val repository: MoviesRepository
): ViewModel() {

	var state by mutableStateOf(MovieDetailsState())

	init {
		when {
			id != null -> {
				viewModelScope.launch {
					val movie = repository.getMovie(id)
					state = state.copy(movie = movie)
				}
			}
			tmdbId != null -> {
				val movie = Movie(
					id = UUID.randomUUID().toString()
				)
				viewModelScope.launch {
					val updatedMovie = repository.getDetailsForMovie(movie, tmdbId)
					if (updatedMovie != null) {
						state = state.copy(movie = updatedMovie)
					}
				}
			}
		}
	}

	fun onAction(action: MovieDetailsAction) {
		when (action) {
			is MovieDetailsAction.CompletionStatusChange -> {
				when (action.status) {
					CompletionStatus.DROPPED,
					CompletionStatus.NOT_STARTED,
					CompletionStatus.WANT_TO -> {
						state = state.copy(movie = state.movie?.copy(
							status = action.status
						))
					}
					CompletionStatus.STARTED -> {
						state = state.copy(movie = state.movie?.copy(
							status = action.status,
							startedOn = LocalDate.now()
						))
					}
					CompletionStatus.FINISHED -> {
						state = state.copy(showFinishedDialog = true)
					}
				}
				saveMovie()
			}
			is MovieDetailsAction.ConfirmFinishedDate -> {
				state = state.copy(movie = state.movie?.copy(
					status = CompletionStatus.FINISHED,
					finishedOn = action.date
				))
				saveMovie()
			}
			MovieDetailsAction.DismissDialog -> {
				state = state.copy(showFinishedDialog = false)
			}

			MovieDetailsAction.OnWatchClick -> {
				val file = PlatformFile(state.movie!!.localPath!!)
				if (file.exists()) {
					FileKit.openFileWithDefaultApplication(file)
				}
			}

			is MovieDetailsAction.OnRatingClick -> {
				state = state.copy(movie = state.movie?.copy(
					rating = action.rating
				))
				saveMovie()
			}
		}
	}

	private fun saveMovie() {
		viewModelScope.launch {
			if (state.movie != null) {
				repository.saveMovie(state.movie!!)
			}
		}
	}
}