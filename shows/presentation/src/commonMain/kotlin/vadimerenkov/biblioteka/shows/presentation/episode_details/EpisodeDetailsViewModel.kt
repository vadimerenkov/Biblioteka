package vadimerenkov.biblioteka.shows.presentation.episode_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.shows.domain.TvRepository

class EpisodeDetailsViewModel(
	private val id: String,
	private val repository: TvRepository
): ViewModel() {

	var state by mutableStateOf(EpisodeDetailsState())

	init {
		viewModelScope.launch {
			val episode = repository.getEpisode(id)
			state = state.copy(episode = episode)
		}
	}

	fun onAction(action: EpisodeDetailsAction) {
		when (action) {
			EpisodeDetailsAction.OnWatchClick -> {
				state.episode?.let {
					it.localPath?.let { path ->
						FileKit.openFileWithDefaultApplication(PlatformFile(path))
						state = state.copy(
							episode = state.episode?.copy(
								status = CompletionStatus.FINISHED
							)
						)
						saveEpisode()
						viewModelScope.launch {
							val show = repository.getShowBySeasonId(state.episode!!.seasonId)
							if (show.status == CompletionStatus.NOT_STARTED || show.status == CompletionStatus.WANT_TO) {
								repository.saveShow(show.copy(status = CompletionStatus.STARTED))
							}
						}
					}
				}
			}
			is EpisodeDetailsAction.CompletionStatusChange -> {
				state = state.copy(
					episode = state.episode?.copy(
						status = action.status
					)
				)
				saveEpisode()
			}
		}
	}

	private fun saveEpisode() {
		viewModelScope.launch {
			state.episode?.let {
				repository.saveEpisode(it)
			}
		}
	}
}