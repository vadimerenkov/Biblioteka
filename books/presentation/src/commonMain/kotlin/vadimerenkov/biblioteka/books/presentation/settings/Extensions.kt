package vadimerenkov.biblioteka.books.presentation.settings

import androidx.datastore.preferences.core.stringPreferencesKey
import vadimerenkov.biblioteka.books.presentation.book_list.BookView
import vadimerenkov.biblioteka.books.presentation.book_list.SortingBy
import vadimerenkov.biblioteka.core.domain.settings.Settings

val BOOKS_SORTING = stringPreferencesKey("BOOKS_SORTING")
val BOOKS_VIEW = stringPreferencesKey("BOOKS_VIEW")

suspend fun Settings.saveBookSorting(sorting: SortingBy) {
	saveSetting(BOOKS_SORTING, sorting.name)
}

suspend fun Settings.getBookSorting(): SortingBy {
	val t = getSetting(BOOKS_SORTING) ?: return SortingBy.DATE_FINISHED
	return SortingBy.valueOf(t)
}

suspend fun Settings.saveBookView(view: BookView) {
	saveSetting(BOOKS_VIEW, view.name)
}

suspend fun Settings.getBookView(): BookView {
	val t = getSetting(BOOKS_VIEW) ?: return BookView.GRID
	return BookView.valueOf(t)
}