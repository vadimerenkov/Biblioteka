package vadimerenkov.biblioteka.shows.presentation.details

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
import vadimerenkov.biblioteka.core.domain.util.onSuccess
import vadimerenkov.biblioteka.shows.domain.TvRepository
import java.time.LocalDate

class ShowDetailsViewModel(
	private val id: String?,
	private val tmdbId: Long?,
	private val repository: TvRepository
): ViewModel() {

	var state by mutableStateOf(ShowDetailsState())

	init {
		viewModelScope.launch {
			when {
				id != null -> {
					val show = repository.getShow(id)
					state = state.copy(show = show)
				}
				tmdbId != null -> {
					repository.getShowFromTmdb(tmdbId)
						.onSuccess { show ->
							val seasons = show.seasons.map { season ->
								season.copy(
									episodes = repository.fetchSeasonFromTmdb(season.id, season.seasonNumber, show.tmdbId!!)
								)
							}
							state = state.copy(show = show.copy(seasons = seasons))
						}
				}
			}
		}
	}

	fun onAction(action: ShowDetailsAction) {
		when (action) {
			is ShowDetailsAction.CompletionStatusChange -> {
				when (action.status) {
					CompletionStatus.NOT_STARTED,
					CompletionStatus.WANT_TO,
					CompletionStatus.DROPPED -> {
						state = state.copy(
							show = state.show?.copy(status = action.status)
						)
						saveShow()
					}
					CompletionStatus.STARTED -> {
						state = state.copy(
							show = state.show?.copy(
								status = action.status,
								startedOn = LocalDate.now()
							)
						)
						saveShow()
					}
					CompletionStatus.FINISHED -> {
						state = state.copy(
							showFinishedDialog = true
						)
					}
				}
			}
			is ShowDetailsAction.OnPlayClick -> {
				if (action.localPath != null) {
					val file = PlatformFile(action.localPath)
					if (file.exists()) {
						FileKit.openFileWithDefaultApplication(file)
						val episode = state.show!!.seasons
							.flatMap { it.episodes }
							.first { it.localPath == action.localPath }

						if (episode.status != CompletionStatus.FINISHED){
							state = state.copy(
								show = state.show?.copy(
									seasons = state.show!!.seasons.map { season ->
										if (season.episodes.contains(episode)) {
											season.copy(
												episodes = season.episodes.map {
													if (it == episode) {
														it.copy(status = CompletionStatus.FINISHED)
													} else it
												}
											)
										} else season
									}
								)
							)
							viewModelScope.launch {
								repository.saveEpisode(episode.copy(status = CompletionStatus.FINISHED))
							}
						}
					}
				}
				if (state.show!!.status == CompletionStatus.NOT_STARTED || state.show!!.status == CompletionStatus.WANT_TO) {
					state = state.copy(
						show = state.show!!.copy(
							status = CompletionStatus.STARTED
						)
					)
				}
				saveShow()
			}
			is ShowDetailsAction.OnRatingClick -> {
				state = state.copy(
					show = state.show?.copy(
						rating = action.rating
					)
				)
				saveShow()
			}
			is ShowDetailsAction.ConfirmFinishedDate -> {
				state = state.copy(
					show = state.show?.copy(
						status = CompletionStatus.FINISHED,
						finishedOn = action.date
					)
				)
				saveShow()
			}
			ShowDetailsAction.DismissDialog -> {
				state = state.copy(
					showFinishedDialog = false
				)
			}

			else -> Unit
		}
	}

	private fun saveShow() {
		viewModelScope.launch {
			state.show?.let {
				repository.saveShow(it)
				repository.saveSeasons(it.seasons)
				repository.saveEpisodes(it.seasons.flatMap { it.episodes })
			}
		}
	}
}