package vadimerenkov.biblioteka.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListDivider(
	text: String,
	size: Int
) {
	Column(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.secondaryContainer)
	) {
		HorizontalDivider()
		Text(
			text = "$text ($size)",
			color = MaterialTheme.colorScheme.onSecondaryContainer,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
		HorizontalDivider()
	}
}