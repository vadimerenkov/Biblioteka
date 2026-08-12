package vadimerenkov.biblioteka.core.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RatingStars(
	rating: Int?,
	onClick: (Int) -> Unit
) {
	Row {
		repeat(10) { star ->
			TooltipArea(
				tooltip = {
					Surface(
						modifier = Modifier.shadow(4.dp),
						color = Color(255, 255, 210),
						shape = RoundedCornerShape(4.dp)
					) {
						Text(
							text = "${star + 1}",
							modifier = Modifier.padding(10.dp)
						)
					}
				},
				delayMillis = 0
			) {
				Icon(
					imageVector = Icons.Default.Star,
					contentDescription = null,
					tint = if (rating != null && rating >= star + 1) MaterialTheme.colorScheme.primary else Color.Gray,
					modifier = Modifier
						.clickable {
							onClick(star + 1)
						}
				)
			}
		}
	}
}