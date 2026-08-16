package vadimerenkov.biblioteka.books.presentation.book_list.components

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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.delete_book
import biblioteka.core.presentation.generated.resources.dropped
import biblioteka.core.presentation.generated.resources.edit_book
import biblioteka.core.presentation.generated.resources.finished
import biblioteka.core.presentation.generated.resources.not_started
import biblioteka.core.presentation.generated.resources.reading_now
import biblioteka.core.presentation.generated.resources.want_to_read
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.presentation.book_list.BookListAction
import vadimerenkov.biblioteka.books.presentation.book_list.BookListState
import vadimerenkov.biblioteka.books.presentation.book_list.SortingBy
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.ListDivider
import vadimerenkov.biblioteka.core.presentation.components.PosterImage
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BookGrid(
	state: BookListState,
	onAction: (BookListAction) -> Unit
) {
	LazyVerticalGrid(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		columns = GridCells.Adaptive(minSize = 200.dp)
	) {
		val started = state.books.filter { it.status == CompletionStatus.STARTED }
		stickyHeader {
			ListDivider(stringResource(Res.string.reading_now), started.size)
		}
		items(
			items = started,
			key = { it.id }
		) { book ->
			BookGridItem(
				book = book,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val wantTo = state.books.filter { it.status == CompletionStatus.WANT_TO }
		stickyHeader {
			ListDivider(stringResource(Res.string.want_to_read), wantTo.size)
		}
		items(
			items = wantTo,
			key = { it.id }
		) { book ->
			BookGridItem(
				book = book,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val finished = state.books
			.filter { it.status == CompletionStatus.FINISHED }
		stickyHeader {
			ListDivider(stringResource(Res.string.finished), finished.size)
		}
		items(
			items = finished,
			key = { it.id }
		) { book ->
			BookGridItem(
				book = book,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val dropped = state.books.filter { it.status == CompletionStatus.DROPPED }
		stickyHeader {
			ListDivider(stringResource(Res.string.dropped), dropped.size)
		}
		items(
			items = dropped,
			key = { it.id }
		) { book ->
			BookGridItem(
				book = book,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
		val other = state.books
			.filter { it.status == CompletionStatus.NOT_STARTED }
		stickyHeader {
			ListDivider(stringResource(Res.string.not_started), other.size)
		}
		items(
			items = other,
			key = { it.id }
		) { book ->
			BookGridItem(
				book = book,
				onAction = onAction,
				sortingBy = state.sortingBy
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BookGridItem(
	book: Book,
	sortingBy: SortingBy,
	onAction: (BookListAction) -> Unit,
) {
	var menuOpen by remember { mutableStateOf(false) }
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val backgroundColor by animateColorAsState(
		if (isHovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
	)

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.clip(RoundedCornerShape(12.dp))
			.background(backgroundColor)
			.clickable(
				indication = null,
				interactionSource = interactionSource
			) {
				onAction(BookListAction.OnBookClick(book))
			}
			.onClick(
				matcher = PointerMatcher.mouse(PointerButton.Secondary),
				onClick = {
					menuOpen = true
				}
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
						text = stringResource(Res.string.delete_book)
					)
				},
				onClick = {
					menuOpen = false
				}
			)
			DropdownMenuItem(
				text = {
					Text(
						text = stringResource(Res.string.edit_book)
					)
				},
				onClick = {
					onAction(BookListAction.OnEditBookClick(book))
					menuOpen = false
				}
			)
		}
		when (sortingBy) {
			SortingBy.DATE_FINISHED -> {
				val month = book.finishedOn?.month?.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()) ?: ""
				val year = book.finishedOn?.year?.toString() ?: ""
				Text(
					text = "$month $year",
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			SortingBy.RATING -> {
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = book.rating?.toString() ?: "",
						color = MaterialTheme.colorScheme.onBackground
					)
					Icon(
						imageVector = Icons.Default.Star,
						contentDescription = null,
						tint = Color.Yellow
					)
				}
			}
			SortingBy.DATE_PUBLISHED -> {
				Text(
					text = book.firstPublishYear?.toString() ?: "",
					color = MaterialTheme.colorScheme.onBackground
				)
			}
		}
		PosterImage(
			imagePath = book.coverUrl
		)
		Text(
			text = book.title,
			textAlign = TextAlign.Center,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis,
			color = MaterialTheme.colorScheme.onBackground
		)
	}
}