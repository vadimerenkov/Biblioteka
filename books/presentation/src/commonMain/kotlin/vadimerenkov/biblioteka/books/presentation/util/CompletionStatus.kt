package vadimerenkov.biblioteka.books.presentation.util

import androidx.compose.runtime.Composable
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.dropped
import biblioteka.core.presentation.generated.resources.finished
import biblioteka.core.presentation.generated.resources.not_started
import biblioteka.core.presentation.generated.resources.started
import biblioteka.core.presentation.generated.resources.want_to_read
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.core.domain.CompletionStatus

@Composable
fun CompletionStatus.toText(): String {
	return when (this) {
		CompletionStatus.NOT_STARTED -> stringResource(Res.string.not_started)
		CompletionStatus.WANT_TO -> stringResource(Res.string.want_to_read)
		CompletionStatus.STARTED -> stringResource(Res.string.started)
		CompletionStatus.FINISHED -> stringResource(Res.string.finished)
		CompletionStatus.DROPPED -> stringResource(Res.string.dropped)
	}
}