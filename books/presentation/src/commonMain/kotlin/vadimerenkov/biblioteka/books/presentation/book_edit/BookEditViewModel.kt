package vadimerenkov.biblioteka.books.presentation.book_edit

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.domain.BooksRepository

class BookEditViewModel(
	private val book: Book,
	private val repository: BooksRepository
): ViewModel() {

	private val eventChannel = Channel<BookEditEvent>()
	val events = eventChannel.receiveAsFlow()

	var state by mutableStateOf(BookEditState())

	init {
		state.titleTextState.setTextAndPlaceCursorAtEnd(book.title)
		state.authorTextState.setTextAndPlaceCursorAtEnd(book.authors.joinToString())
		state.publishedYearTextState.setTextAndPlaceCursorAtEnd(book.firstPublishYear?.toString() ?: "")
		state.descriptionTextState.setTextAndPlaceCursorAtEnd(book.description ?: "")
		state = state.copy(
			coverUrl = book.coverUrl,
			rating = book.rating,
			status = book.status,
			startedDate = book.startedOn,
			finishedDate = book.finishedOn
		)
	}

	fun onAction(
		action: BookEditAction
	) {
		when (action) {
			BookEditAction.OnConfirmClick -> {
				viewModelScope.launch {
					var coverUrl: String? = state.coverUrl
					state.coverUrl?.let { url ->
						val file = PlatformFile(url)
						if (file.exists()) {
							val compressedBytes = FileKit.compressImage(file)
							val directory = PlatformFile(FileKit.cacheDir, "thumbnails")
							if (!directory.exists()) {
								directory.createDirectories()
							}
							val compressedFile =
								PlatformFile(FileKit.cacheDir, "/thumbnails/${book.id}.jpg")
							compressedFile.write(compressedBytes)
							println(compressedFile.absolutePath())
							coverUrl = compressedFile.absolutePath()
						}
					}
					val book = book.copy(
						title = state.titleTextState.text.toString(),
						authors = state.authorTextState.text.split(", "),
						firstPublishYear = state.publishedYearTextState.text.toString().toIntOrNull(),
						description = state.descriptionTextState.text.toString(),
						coverUrl = coverUrl,
						status = state.status,
						rating = state.rating,
						finishedOn = state.finishedDate,
						startedOn = state.startedDate
					)
					repository.saveBook(book)
					eventChannel.send(BookEditEvent.WritingFinished)
				}
			}

			is BookEditAction.ChangeImageFile -> {
				state = state.copy(coverUrl = action.file.absolutePath())
			}

			is BookEditAction.RatingChange -> {
				state = state.copy(rating = action.rating)
			}
			is BookEditAction.StatusChange -> {
				state = state.copy(status = action.status)
			}

			is BookEditAction.FinishedDateChange -> {
				state = state.copy(finishedDate = action.date)
			}
			is BookEditAction.StartedDateChange -> {
				state = state.copy(startedDate = action.date)
			}
		}
	}
}