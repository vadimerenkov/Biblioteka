package vadimerenkov.biblioteka.shows.presentation.util

import androidx.compose.runtime.Composable
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.dropped
import biblioteka.core.presentation.generated.resources.finished
import biblioteka.core.presentation.generated.resources.not_started
import biblioteka.core.presentation.generated.resources.want_to_watch
import biblioteka.core.presentation.generated.resources.watching
import org.jetbrains.compose.resources.stringResource
import vadimerenkov.biblioteka.core.domain.CompletionStatus

@Composable
fun CompletionStatus.toText(): String {
	return when (this) {
		CompletionStatus.NOT_STARTED -> stringResource(Res.string.not_started)
		CompletionStatus.WANT_TO -> stringResource(Res.string.want_to_watch)
		CompletionStatus.STARTED -> stringResource(Res.string.watching)
		CompletionStatus.FINISHED -> stringResource(Res.string.finished)
		CompletionStatus.DROPPED -> stringResource(Res.string.dropped)
	}
}