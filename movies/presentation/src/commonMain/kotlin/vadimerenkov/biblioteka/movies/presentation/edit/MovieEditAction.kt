package vadimerenkov.biblioteka.movies.presentation.edit

import io.github.vinceglb.filekit.PlatformFile
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

sealed interface MovieEditAction {
	data class ChangeImageFile(val platformFile: PlatformFile): MovieEditAction
	data class StatusChange(val status: CompletionStatus): MovieEditAction
	data class RatingChange(val rating: Int): MovieEditAction
	data class ReleaseDateChange(val date: LocalDate?): MovieEditAction
	data class FinishedDateChange(val date: LocalDate?): MovieEditAction
	data class StartedDateChange(val date: LocalDate?): MovieEditAction
	data object OnConfirmClick: MovieEditAction
	data object OnSearchClick: MovieEditAction
	data object DismissSearchScreen: MovieEditAction
	data class LoadSelectedMovie(val tmdbId: Long): MovieEditAction
}