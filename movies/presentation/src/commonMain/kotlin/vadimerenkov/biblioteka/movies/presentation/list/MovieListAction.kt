package vadimerenkov.biblioteka.movies.presentation.list

import io.github.vinceglb.filekit.PlatformFile

sealed interface MovieListAction {
	data class OnDirectoryChosen(val directory: PlatformFile): MovieListAction
	data class OnMovieClick(val id: String? = null, val tmdbId: Long? = null): MovieListAction
	data class OnEditMovieClick(val id: String): MovieListAction
	data object OnAddMovieClick: MovieListAction
	data class ChangeShowLocal(val show: Boolean): MovieListAction
	data class ChangeSortingBy(val sortingBy: SortingBy): MovieListAction
	data class ChangeView(val view: MovieView): MovieListAction
}