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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.dropped
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
import kotlin.time.Duration.Companion.minutes

@Composable
fun MovieColumn(
	state: MovieListState,
	onAction: (MovieListAction) -> Unit
) {
	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		val watching = state.movies.filter { it.status == CompletionStatus.STARTED }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.watching),
				size = watching.size
			)
		}
		items(
			items = watching,
			key = { it.id }
		) { movie ->
			MovieListItem(
				movie = movie,
				onAction = onAction
			)
		}

		val wantTo = state.movies.filter { it.status == CompletionStatus.WANT_TO }
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
			MovieListItem(
				movie = movie,
				onAction = onAction
			)
		}

		val notWatched = state.movies.filter { it.status == CompletionStatus.NOT_STARTED }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.not_started),
				size = notWatched.size
			)
		}
		items(
			items = notWatched,
			key = { it.id }
		) { movie ->
			MovieListItem(
				movie = movie,
				onAction = onAction
			)
		}

		val finished = state.movies.filter { it.status == CompletionStatus.FINISHED }
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
			MovieListItem(
				movie = movie,
				onAction = onAction
			)
		}

		val dropped = state.movies.filter { it.status == CompletionStatus.DROPPED }
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
			MovieListItem(
				movie = movie,
				onAction = onAction
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieListItem(
	movie: Movie,
	onAction: (MovieListAction) -> Unit
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val backgroundColor by animateColorAsState(
		if (isHovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
	)
	var menuOpen by remember { mutableStateOf(false) }

	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.clickable(
				indication = null,
				interactionSource = interactionSource
			) {
				onAction(MovieListAction.OnMovieClick(movie.id))
			}
			.onClick(
				matcher = PointerMatcher.mouse(PointerButton.Secondary),
				onClick = {
					menuOpen = true
				}
			)
			.background(backgroundColor)
			.fillMaxWidth()
			.padding(horizontal = 16.dp)
	) {
		AsyncImage(
			model = movie.posterPath,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.width(100.dp)
				.aspectRatio(2/3f)
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Text(
				text = movie.title,
				fontSize = 18.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			movie.releaseDate?.let { date ->
				Text(
					text = date.toString(),
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			movie.rating?.let { rating ->
				Row {
					repeat(rating) {
						Icon(
							imageVector = Icons.Default.Star,
							contentDescription = null,
							tint = MaterialTheme.colorScheme.primary,
							modifier = Modifier
								.size(16.dp)
						)
					}
				}
			}
			movie.runtime?.let { runtime ->
				Text(
					text = runtime.minutes.toString(),
					color = MaterialTheme.colorScheme.onBackground
				)
			}
		}
	}
}