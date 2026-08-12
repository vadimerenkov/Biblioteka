package vadimerenkov.biblioteka.movies.presentation.list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.dropped
import biblioteka.core.presentation.generated.resources.edit_movie
import biblioteka.core.presentation.generated.resources.finished
import biblioteka.core.presentation.generated.resources.not_started
import biblioteka.core.presentation.generated.resources.want_to_watch
import biblioteka.core.presentation.generated.resources.watching
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.ListDivider
import vadimerenkov.biblioteka.movies.domain.Movie
import vadimerenkov.biblioteka.movies.presentation.list.MovieListAction
import vadimerenkov.biblioteka.movies.presentation.list.MovieListState
import vadimerenkov.biblioteka.movies.presentation.list.SortingBy
import vadimerenkov.biblioteka.movies.presentation.util.filterLocal
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MovieGrid(
	state: MovieListState,
	onAction: (MovieListAction) -> Unit,
) {
	LazyVerticalGrid(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		columns = GridCells.Adaptive(200.dp)
	) {
		val started = state.movies
			.filter { it.status == CompletionStatus.STARTED }
			.filterLocal(state.showOnlyLocal)
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.watching),
				size = started.size
			)
		}
		items(
			items = started,
			key = { it.id }
		) { movie ->
			GridMovieItem(
				movie = movie,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val wantTo = state.movies
			.filter { it.status == CompletionStatus.WANT_TO }
			.filterLocal(state.showOnlyLocal)
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.want_to_watch),
				size = wantTo.size
			)
		}
		items(
			items = wantTo,
			key = { it.id }
		) { movie ->
			GridMovieItem(
				movie = movie,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val notStarted = state.movies
			.filter { it.status == CompletionStatus.NOT_STARTED }
			.filterLocal(state.showOnlyLocal)
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.not_started),
				size = notStarted.size
			)
		}
		items(
			items = notStarted,
			key = { it.id }
		) { movie ->
			GridMovieItem(
				movie = movie,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val finished = state.movies
			.filter { it.status == CompletionStatus.FINISHED }
			.filterLocal(state.showOnlyLocal)
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.finished),
				size = finished.size
			)
		}
		items(
			items = finished,
			key = { it.id }
		) { movie ->
			GridMovieItem(
				movie = movie,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val dropped = state.movies
			.filter { it.status == CompletionStatus.DROPPED }
			.filterLocal(state.showOnlyLocal)
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.dropped),
				size = dropped.size
			)
		}
		items(
			items = dropped,
			key = { it.id }
		) { movie ->
			GridMovieItem(
				movie = movie,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GridMovieItem(
	movie: Movie,
	sortingBy: SortingBy,
	onAction: (MovieListAction) -> Unit
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val backgroundColor by animateColorAsState(
		if (isHovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
	)

	var menuOpen by remember { mutableStateOf(false) }

	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.clip(RoundedCornerShape(12.dp))
			.background(backgroundColor)
			.onClick(
				matcher = PointerMatcher.mouse(PointerButton.Secondary),
				onClick = {
					menuOpen = true
				}
			)
			.clickable(
				indication = null,
				interactionSource = interactionSource
			) {
				onAction(MovieListAction.OnMovieClick(movie.id))
			}
			.padding(8.dp)
	) {
		DropdownMenu(
			expanded = menuOpen,
			onDismissRequest = { menuOpen = false }
		) {
			DropdownMenuItem(
				text = {
					Text(
						text = stringResource(Res.string.edit_movie)
					)
				},
				onClick = {
					onAction(MovieListAction.OnEditMovieClick(movie.id))
				}
			)
		}
		when (sortingBy) {
			SortingBy.ALPHABET -> {}
			SortingBy.DATE_FINISHED -> {
				val month = movie.finishedOn?.month?.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()) ?: ""
				val year = movie.finishedOn?.year?.toString() ?: ""
				Text(
					text = "$month $year",
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			SortingBy.RELEASE_DATE -> {
				Text(
					text = movie.releaseDate?.toString() ?: "",
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			SortingBy.RATING -> {
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = movie.rating?.toString() ?: "",
						color = MaterialTheme.colorScheme.onBackground
					)
					if (movie.rating != null) {
						Icon(
							imageVector = Icons.Default.Star,
							contentDescription = null,
							tint = MaterialTheme.colorScheme.primary
						)
					}
				}
			}
		}
		AsyncImage(
			model = movie.posterPath,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.width(200.dp)
				.aspectRatio(2/3f)
		)
		Text(
			text = movie.title,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis
		)
	}
}