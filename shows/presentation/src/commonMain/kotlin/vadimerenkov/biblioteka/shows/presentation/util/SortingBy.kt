package vadimerenkov.biblioteka.shows.presentation.util

import androidx.compose.runtime.Composable
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.alphabet
import biblioteka.core.presentation.generated.resources.first_air_date
import biblioteka.core.presentation.generated.resources.last_air_date
import biblioteka.core.presentation.generated.resources.rating
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.shows.presentation.list.SortingBy

@Composable
fun SortingBy.toText(): String {
	return when (this) {
		SortingBy.ALPHABET -> stringResource(Res.string.alphabet)
		SortingBy.RATING -> stringResource(Res.string.rating)
		SortingBy.FIRST_AIR_DATE -> stringResource(Res.string.first_air_date)
		SortingBy.LAST_AIR_DATE -> stringResource(Res.string.last_air_date)
	}
}