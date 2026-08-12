package vadimerenkov.biblioteka.movies.presentation.list

import vadimerenkov.biblioteka.movies.domain.Movie

data class MovieListState(
	val movies: List<Movie> = emptyList(),
	val showOnlyLocal: Boolean = false,
	val sortingBy: SortingBy = SortingBy.ALPHABET,
	val view: MovieView = MovieView.GRID
)
