package vadimerenkov.biblioteka.shows.presentation.edit

import io.github.vinceglb.filekit.PlatformFile
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.shows.domain.TvSeason

sealed interface ShowEditAction {

	data object OnSearchClick: ShowEditAction
	data class ChangeImageFile(val image: PlatformFile): ShowEditAction
	data class StatusChange(val status: CompletionStatus): ShowEditAction
	data class RatingChange(val rating: Int): ShowEditAction
	data object CloseSearchWindow: ShowEditAction
	data class LoadSelectedShow(val tmdbId: Long): ShowEditAction
	data object OnConfirmClick: ShowEditAction
	data class OnDeleteSeasonClick(val season: TvSeason): ShowEditAction
	data class SelectFolderForSeason(val seasonId: String, val folder: PlatformFile): ShowEditAction
}