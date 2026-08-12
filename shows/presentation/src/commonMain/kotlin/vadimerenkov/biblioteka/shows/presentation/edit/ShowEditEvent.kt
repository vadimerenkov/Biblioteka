package vadimerenkov.biblioteka.shows.presentation.edit

sealed interface ShowEditEvent {
	data object FinishedWriting: ShowEditEvent
}