package vadimerenkov.biblioteka.books.presentation.book_list.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.dropped
import biblioteka.core.presentation.generated.resources.finished
import biblioteka.core.presentation.generated.resources.finished_on
import biblioteka.core.presentation.generated.resources.not_started
import biblioteka.core.presentation.generated.resources.reading_now
import biblioteka.core.presentation.generated.resources.want_to_read
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.presentation.book_list.BookListAction
import vadimerenkov.biblioteka.books.presentation.book_list.BookListState
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.ListDivider

@Composable
fun BookListColumn(
	state: BookListState,
	onAction: (BookListAction) -> Unit
) {
	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		val started = state.books.filter { it.status == CompletionStatus.STARTED }
		stickyHeader {
			ListDivider(stringResource(Res.string.reading_now), started.size)
		}
		items(
			items = started,
			key = { it.id }
		) { book ->
			BookListItem(
				book = book,
				onAction = onAction,
			)
		}

		val wantTo = state.books.filter { it.status == CompletionStatus.WANT_TO }
		stickyHeader {
			ListDivider(
				text = stringResource(Res.string.want_to_read),
				size = wantTo.size
			)
		}
		items(
			items = wantTo,
			key = { it.id }
		) { book ->
			BookListItem(
				book = book,
				onAction = onAction,
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
			BookListItem(
				book = book,
				onAction = onAction,
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
			BookListItem(
				book = book,
				onAction = onAction,
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
			BookListItem(
				book = book,
				onAction = onAction,
			)
		}
	}
}

@Composable
private fun BookListItem(
	book: Book,
	onAction: (BookListAction) -> Unit
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isHovered by interactionSource.collectIsHoveredAsState()

	val backgroundColor by animateColorAsState(
		if (isHovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background
	)

	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.background(backgroundColor)
			.clickable(
				indication = null,
				interactionSource = interactionSource
			) {
				onAction(BookListAction.OnBookClick(book))
			}
			.fillMaxWidth()
			.padding(horizontal = 16.dp)
	) {
		AsyncImage(
			model = book.coverUrl,
			contentScale = ContentScale.Crop,
			contentDescription = null,
			modifier = Modifier
				.width(100.dp)
				.aspectRatio(2/3f)
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Text(
				text = book.title,
				fontSize = 18.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			if (book.authors.isNotEmpty()) {
				Text(
					text = book.authors.joinToString(),
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			book.firstPublishYear?.let { year ->
				Text(
					text = year.toString(),
					color = MaterialTheme.colorScheme.onBackground
				)
			}
			book.rating?.let { rating ->
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
			book.finishedOn?.let { date ->
				val finishedOn = stringResource(Res.string.finished_on)
				Text(
					text = "$finishedOn $date",
					color = MaterialTheme.colorScheme.onBackground
				)
			}
		}
	}
}