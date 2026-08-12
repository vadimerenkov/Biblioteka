package vadimerenkov.biblioteka.movies.presentation.util

import androidx.compose.runtime.Composable
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.alphabet
import biblioteka.core.presentation.generated.resources.date_finished
import biblioteka.core.presentation.generated.resources.rating
import biblioteka.core.presentation.generated.resources.release_date
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.movies.presentation.list.SortingBy

@Composable
fun SortingBy.toText(): String {
	return when (this) {
		SortingBy.ALPHABET -> stringResource(Res.string.alphabet)
		SortingBy.DATE_FINISHED -> stringResource(Res.string.date_finished)
		SortingBy.RELEASE_DATE -> stringResource(Res.string.release_date)
		SortingBy.RATING -> stringResource(Res.string.rating)
	}
}