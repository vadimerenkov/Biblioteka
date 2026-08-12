package vadimerenkov.biblioteka.shows.presentation.edit

import androidx.compose.foundation.text.input.TextFieldState
import vadimerenkov.biblioteka.shows.domain.TvSeason
import vadimerenkov.biblioteka.shows.domain.TvShow

data class ShowEditState(
	val show: TvShow? = null,
	val titleState: TextFieldState = TextFieldState(),
	val descriptionState: TextFieldState = TextFieldState(),
	val originalTitleState: TextFieldState = TextFieldState(),
	val genresState: TextFieldState = TextFieldState(),
	val networksState: TextFieldState = TextFieldState(),
	val originCountriesState: TextFieldState = TextFieldState(),
	val taglineState: TextFieldState = TextFieldState(),
	val showSearchScreen: Boolean = false,
	val seasonsToDelete: List<TvSeason> = emptyList()
)
