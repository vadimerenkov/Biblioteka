package vadimerenkov.biblioteka.books.presentation.book_edit

import androidx.compose.foundation.text.input.TextFieldState
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

data class BookEditState(
	val titleTextState: TextFieldState = TextFieldState(),
	val descriptionTextState: TextFieldState = TextFieldState(),
	val authorTextState: TextFieldState = TextFieldState(),
	val publishedYearTextState: TextFieldState = TextFieldState(),
	val coverUrl: String? = null,
	val status: CompletionStatus = CompletionStatus.NOT_STARTED,
	val rating: Int? = null,
	val startedDate: LocalDate? = null,
	val finishedDate: LocalDate? = null
) {
	val isValid: Boolean
		get() {
			return titleTextState.text.isNotBlank()
		}
}
