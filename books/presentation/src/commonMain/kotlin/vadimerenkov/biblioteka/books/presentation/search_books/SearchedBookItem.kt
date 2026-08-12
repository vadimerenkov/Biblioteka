package vadimerenkov.biblioteka.books.presentation.search_books

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.book
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import vadimerenkov.biblioteka.books.domain.Book
import kotlin.math.round

@Composable
fun SearchedBookItem(
	book: Book,
	onAction: (SearchBooksAction) -> Unit
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				onAction(SearchBooksAction.OnBookClick(book))
			}
	) {
		AsyncImage(
			model = book.coverUrl ?: Res.getUri("drawable/book.svg"),
			placeholder = painterResource(Res.drawable.book),
			error = painterResource(Res.drawable.book),
			contentDescription = null,
			modifier = Modifier
				.height(200.dp)
				.aspectRatio(2/3f)
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Text(
				text = book.title,
				color = MaterialTheme.colorScheme.onBackground
			)
			Text(
				text = book.authors.joinToString(),
				color = MaterialTheme.colorScheme.onBackground
			)
			Text(
				text = book.firstPublishYear.toString(),
				color = MaterialTheme.colorScheme.onBackground
			)
			if (book.avgOLRating != null) {
				val rating = round(book.avgOLRating!! * 10) / 10.0 * 2
				Row {
					Icon(
						imageVector = Icons.Default.Star,
						tint = MaterialTheme.colorScheme.primary,
						contentDescription = null
					)
					Text(
						text = "${rating} (${book.numberOLRatings})",
						color = MaterialTheme.colorScheme.onBackground
					)
				}
			}
		}
	}
}