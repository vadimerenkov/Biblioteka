package vadimerenkov.biblioteka.movies.presentation.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import vadimerenkov.biblioteka.core.domain.settings.Settings
import vadimerenkov.biblioteka.movies.presentation.list.MovieView
import vadimerenkov.biblioteka.movies.presentation.list.SortingBy

val MOVIE_SHOW_LOCAL = booleanPreferencesKey("MOVIE_SHOW_LOCAL")
val MOVIE_VIEW = stringPreferencesKey("MOVIE_VIEW")
val MOVIE_SORTING = stringPreferencesKey("MOVIE_SORTING")

suspend fun Settings.saveShowOnlyLocal(show: Boolean) {
	this.saveSetting(MOVIE_SHOW_LOCAL, show)
}

suspend fun Settings.getShowOnlyLocal(): Boolean {
	return this.getSetting(MOVIE_SHOW_LOCAL) ?: false
}

suspend fun Settings.saveMovieView(view: MovieView) {
	this.saveSetting(MOVIE_VIEW, view.name)
}

suspend fun Settings.getMovieView(): MovieView {
	val t = getSetting(MOVIE_VIEW) ?: MovieView.GRID.name
	return MovieView.valueOf(t)
}

suspend fun Settings.saveMovieSorting(sorting: SortingBy) {
	this.saveSetting(MOVIE_SORTING, sorting.name)
}

suspend fun Settings.getMovieSorting(): SortingBy {
	val t = this.getSetting(MOVIE_SORTING) ?: SortingBy.ALPHABET.name
	return SortingBy.valueOf(t)
}