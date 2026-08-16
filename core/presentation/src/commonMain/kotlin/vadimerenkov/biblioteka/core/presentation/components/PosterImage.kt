package vadimerenkov.biblioteka.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.image
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun PosterImage(
	imagePath: String?
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.width(200.dp)
			.aspectRatio(2/3f)
			.border(
				width = 2.dp,
				color = MaterialTheme.colorScheme.primary
			)
	) {
		Icon(
			painter = painterResource(Res.drawable.image),
			contentDescription = null,
			tint = MaterialTheme.colorScheme.primary
		)
		AsyncImage(
			model = imagePath,
			contentDescription = null,
			contentScale = ContentScale.Crop
		)
	}
}