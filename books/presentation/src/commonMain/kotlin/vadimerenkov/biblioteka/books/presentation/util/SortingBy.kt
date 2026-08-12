package vadimerenkov.biblioteka.books.presentation.util

import androidx.compose.runtime.Composable
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.date_finished
import biblioteka.core.presentation.generated.resources.date_published
import biblioteka.core.presentation.generated.resources.rating
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.books.presentation.book_list.SortingBy

@Composable
fun SortingBy.toText(): String {
	return when (this) {
		SortingBy.DATE_FINISHED -> stringResource(Res.string.date_finished)
		SortingBy.RATING -> stringResource(Res.string.rating)
		SortingBy.DATE_PUBLISHED -> stringResource(Res.string.date_published)
	}
}