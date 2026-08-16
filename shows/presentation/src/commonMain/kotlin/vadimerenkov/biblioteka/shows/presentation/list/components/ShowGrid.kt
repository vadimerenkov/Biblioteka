package vadimerenkov.biblioteka.shows.presentation.list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.dropped
import biblioteka.core.presentation.generated.resources.edit_show
import biblioteka.core.presentation.generated.resources.finished
import biblioteka.core.presentation.generated.resources.not_started
import biblioteka.core.presentation.generated.resources.want_to_watch
import biblioteka.core.presentation.generated.resources.watching
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.ListDivider
import vadimerenkov.biblioteka.core.presentation.components.PosterImage
import vadimerenkov.biblioteka.shows.domain.TvShow
import vadimerenkov.biblioteka.shows.presentation.list.ShowListAction
import vadimerenkov.biblioteka.shows.presentation.list.ShowListState

@Composable
fun ShowGrid(
	state: ShowListState,
	onAction: (ShowListAction) -> Unit
) {
	LazyVerticalGrid(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		columns = GridCells.Adaptive(200.dp)
	) {
		val watching = state.shows.filter { it.status == CompletionStatus.STARTED }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.watching),
				size = watching.size
			)
		}
		items(
			items = watching,
			key = { it.id }
		) { show ->
			ShowGridItem(
				show = show,
				onAction = onAction
			)
		}

		val wantTo = state.shows.filter { it.status == CompletionStatus.WANT_TO }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.want_to_watch),
				size = wantTo.size
			)
		}
		items(
			items = wantTo,
			key = { it.id }
		) { show ->
			ShowGridItem(
				show = show,
				onAction = onAction
			)
		}

		val notStarted = state.shows.filter { it.status == CompletionStatus.NOT_STARTED }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.not_started),
				size = notStarted.size
			)
		}
		items(
			items = notStarted,
			key = { it.id }
		) { show ->
			ShowGridItem(
				show = show,
				onAction = onAction
			)
		}

		val finished = state.shows.filter { it.status == CompletionStatus.FINISHED }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.finished),
				size = finished.size
			)
		}
		items(
			items = finished,
			key = { it.id }
		) { show ->
			ShowGridItem(
				show = show,
				onAction = onAction
			)
		}

		val dropped = state.shows.filter { it.status == CompletionStatus.DROPPED }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.dropped),
				size = dropped.size
			)
		}
		items(
			items = dropped,
			key = { it.id }
		) { show ->
			ShowGridItem(
				show = show,
				onAction = onAction
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowGridItem(
	show: TvShow,
	onAction: (ShowListAction) -> Unit
) {
	var menuOpen by remember { mutableStateOf(false) }
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val backgroundColor by animateColorAsState(
		if (isHovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
	)

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier
			.clickable(
				interactionSource = interactionSource
			) {
				onAction(ShowListAction.OnShowClick(show.id))
			}
			.onClick(
				matcher = PointerMatcher.mouse(PointerButton.Secondary),
				onClick = {
					menuOpen = true
				}
			)
			.background(
				color = backgroundColor,
				shape = RoundedCornerShape(12.dp)
			)
			.padding(8.dp)
	) {
		DropdownMenu(
			expanded = menuOpen,
			onDismissRequest = { menuOpen = false }
		) {
			DropdownMenuItem(
				text = {
					Text(
						text = stringResource(Res.string.edit_show)
					)
				},
				onClick = {
					onAction(ShowListAction.OnEditShowClick(show.id))
				}
			)
		}
		PosterImage(show.posterPath)
		Text(
			text = show.title,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis
		)
	}
}