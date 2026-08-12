package vadimerenkov.biblioteka.shows.presentation.settings

import androidx.datastore.preferences.core.stringPreferencesKey
import vadimerenkov.biblioteka.core.domain.settings.Settings
import vadimerenkov.biblioteka.shows.presentation.list.ShowView
import vadimerenkov.biblioteka.shows.presentation.list.SortingBy

val SHOW_VIEW = stringPreferencesKey("SHOW_VIEW")
val SHOW_SORTING = stringPreferencesKey("SHOW_SORTING")

suspend fun Settings.saveShowView(view: ShowView) {
	saveSetting(SHOW_VIEW, view.name)
}

suspend fun Settings.getShowView(): ShowView {
	val t = getSetting(SHOW_VIEW) ?: ShowView.GRID.name
	return ShowView.valueOf(t)
}

suspend fun Settings.saveShowSorting(sortingBy: SortingBy) {
	saveSetting(SHOW_SORTING, sortingBy.name)
}

suspend fun Settings.getShowSorting(): SortingBy {
	val t = getSetting(SHOW_SORTING) ?: SortingBy.ALPHABET.name
	return SortingBy.valueOf(t)
}