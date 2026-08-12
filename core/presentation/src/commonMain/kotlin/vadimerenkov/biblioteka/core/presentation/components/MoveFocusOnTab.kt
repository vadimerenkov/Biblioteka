package vadimerenkov.biblioteka.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Modifier.moveFocusOnTab(
	focusManager: FocusManager = LocalFocusManager.current
) = onPreviewKeyEvent {
	if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
		focusManager.moveFocus(
			if (it.isShiftPressed) FocusDirection.Previous
			else FocusDirection.Next
		)
		return@onPreviewKeyEvent true
	}
	false
}