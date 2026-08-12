package vadimerenkov.biblioteka.shows.presentation.edit

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
import vadimerenkov.biblioteka.core.domain.util.onFailure
import vadimerenkov.biblioteka.core.domain.util.onSuccess
import vadimerenkov.biblioteka.core.domain.util.saveThumbnail
import vadimerenkov.biblioteka.shows.domain.TvRepository

class ShowEditViewModel(
	val id: String,
	val repository: TvRepository
): ViewModel() {

	var state by mutableStateOf(ShowEditState())

	private val eventsChannel = Channel<ShowEditEvent>()
	val events = eventsChannel.receiveAsFlow()

	init {
		viewModelScope.launch {
			val show = repository.getShow(id)
			state = state.copy(show = show)
			state.titleState.setTextAndPlaceCursorAtEnd(show.title)
			state.descriptionState.setTextAndPlaceCursorAtEnd(show.description ?: "")
			state.originalTitleState.setTextAndPlaceCursorAtEnd(show.originalTitle ?: "")
		}
	}

	fun onAction(action: ShowEditAction) {
		when (action) {
			is ShowEditAction.ChangeImageFile -> {
				state = state.copy(
					show = state.show?.copy(
						posterPath = action.image.absolutePath()
					)
				)
			}
			ShowEditAction.OnSearchClick -> {
				state = state.copy(showSearchScreen = true)
			}
			is ShowEditAction.RatingChange -> {
				state = state.copy(
					show = state.show?.copy(
						rating = action.rating
					)
				)
			}
			is ShowEditAction.StatusChange -> {
				state = state.copy(
					show = state.show?.copy(
						status = action.status
					)
				)
			}
			ShowEditAction.CloseSearchWindow -> {
				state = state.copy(showSearchScreen = false)
			}

			is ShowEditAction.LoadSelectedShow -> {
				state = state.copy(showSearchScreen = false)
				viewModelScope.launch {
					repository.getShowFromTmdb(action.tmdbId)
						.onSuccess { show ->

							state.titleState.setTextAndPlaceCursorAtEnd(show.title)
							state.descriptionState.setTextAndPlaceCursorAtEnd(show.description ?: "")
							state.originalTitleState.setTextAndPlaceCursorAtEnd(show.originalTitle ?: "")

							val seasonsWithEpisodes = show.seasons.map { season ->
								season.copy(
									episodes = repository.fetchSeasonFromTmdb(season.id, season.seasonNumber, show.tmdbId!!),
									showId = state.show!!.id
								)
							}

							state = state.copy(
								show = show.copy(
									id = state.show!!.id,
									localPaths = state.show!!.localPaths,
									seasons = seasonsWithEpisodes
								),
								seasonsToDelete = state.show!!.seasons
							)
						}
						.onFailure {

						}
				}
			}
			ShowEditAction.OnConfirmClick -> {
				state.show?.let { show ->
					viewModelScope.launch {
						val alreadyExists = repository.checkIfAlreadyExists(show)
						println("Already exists? $alreadyExists")
						if (alreadyExists && show.tmdbId != null) {
							println("Updating local paths")
							repository.updateLocalPaths(show.localPaths, show.tmdbId!!, show.id)
						}
						else {
							var posterPath = show.posterPath
							posterPath?.let { path ->
								posterPath = saveThumbnail(
									path = path,
									id = show.id
								)
							}

							val show = state.show!!.copy(
								posterPath = posterPath,
								title = state.titleState.text.toString(),
								description = state.descriptionState.text.toString(),
								originalTitle = state.originalTitleState.text.toString()
							)

							repository.saveShow(show)
							repository.saveSeasons(show.seasons)
							repository.deleteSeasons(state.seasonsToDelete)
						}
						eventsChannel.send(ShowEditEvent.FinishedWriting)
					}
				}
			}

			is ShowEditAction.OnDeleteSeasonClick -> {
				state = state.copy(
					seasonsToDelete = state.seasonsToDelete + action.season,
					show = state.show?.copy(seasons = state.show!!.seasons - action.season)
				)
			}

			is ShowEditAction.SelectFolderForSeason -> {
				val season = state.show!!.seasons.first { it.id == action.seasonId }
				val updatedSeason = repository.assignFolderToSeason(action.folder, season)
				state = state.copy(
					show = state.show?.copy(
						seasons = state.show!!.seasons - season + updatedSeason
					)
				)
			}
		}
	}
}