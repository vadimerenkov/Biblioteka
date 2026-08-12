package vadimerenkov.biblioteka.core.presentation.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.onClick
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.backHandler(
	onBackClick: () -> Unit
): Modifier {
	return this
		.onClick(
			matcher = PointerMatcher.mouse(PointerButton.Back),
			onClick = onBackClick
		)
}