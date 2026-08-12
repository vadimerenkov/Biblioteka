package vadimerenkov.biblioteka.shows.presentation.list

import vadimerenkov.biblioteka.shows.domain.TvShow

data class ShowListState(
	val shows: List<TvShow> = emptyList(),
	val view: ShowView = ShowView.GRID,
	val sorting: SortingBy = SortingBy.ALPHABET
)
