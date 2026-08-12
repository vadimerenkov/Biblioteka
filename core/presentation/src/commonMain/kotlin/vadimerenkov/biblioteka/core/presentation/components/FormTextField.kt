package vadimerenkov.biblioteka.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormTextField(
	text: String,
	state: TextFieldState,
	modifier: Modifier = Modifier
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.widthIn(max = 600.dp)
	) {
		Text(
			text = text,
			fontSize = 18.sp,
			color = MaterialTheme.colorScheme.onBackground
		)
		OutlinedTextField(
			state = state,
			modifier = Modifier
				.moveFocusOnTab()
		)
	}
}