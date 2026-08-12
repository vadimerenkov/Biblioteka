package vadimerenkov.biblioteka.movies.presentation.edit

sealed interface MovieEditEvent {
	data object WritingFinished: MovieEditEvent
}