package vadimerenkov.biblioteka.books.presentation.book_edit

import io.github.vinceglb.filekit.PlatformFile
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

sealed interface BookEditAction {
	data object OnConfirmClick: BookEditAction
	data class ChangeImageFile(val file: PlatformFile): BookEditAction
	data class StatusChange(val status: CompletionStatus): BookEditAction
	data class RatingChange(val rating: Int?): BookEditAction
	data class StartedDateChange(val date: LocalDate?): BookEditAction
	data class FinishedDateChange(val date: LocalDate?): BookEditAction
}