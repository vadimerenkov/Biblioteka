package vadimerenkov.biblioteka.movies.presentation.edit

import androidx.compose.foundation.text.input.TextFieldState
import vadimerenkov.biblioteka.movies.domain.Movie

data class MovieEditState(
	val movie: Movie? = null,
	val titleState: TextFieldState = TextFieldState(),
	val descriptionState: TextFieldState = TextFieldState(),
	val revenueState: TextFieldState = TextFieldState(),
	val originCountriesState: TextFieldState = TextFieldState(),
	val productionCompaniesState: TextFieldState = TextFieldState(),
	val originalTitleState: TextFieldState = TextFieldState(),
	val showSearchScreen: Boolean = false
) {
	val isValid: Boolean
		get() = titleState.text.isNotBlank()
}
