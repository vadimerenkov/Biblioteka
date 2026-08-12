package vadimerenkov.biblioteka.movies.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.budget
import biblioteka.core.presentation.generated.resources.confirm
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.origin_countries
import biblioteka.core.presentation.generated.resources.production_companies
import biblioteka.core.presentation.generated.resources.revenue
import biblioteka.core.presentation.generated.resources.watch
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.RatingStars
import vadimerenkov.biblioteka.core.presentation.util.backHandler
import vadimerenkov.biblioteka.movies.presentation.util.existsLocally
import vadimerenkov.biblioteka.movies.presentation.util.toText
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

private val DIVIDER_WIDTH = 200.dp

@Composable
fun MovieDetailsScreen(
	viewModel: MovieDetailsViewModel = koinViewModel(),
	onBackClick: () -> Unit
) {
	MovieDetailsRoot(
		state = viewModel.state,
		onBackClick = onBackClick,
		onAction = viewModel::onAction
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsRoot(
	state: MovieDetailsState,
	onAction: (MovieDetailsAction) -> Unit,
	onBackClick: () -> Unit
) {
	if (state.showFinishedDialog) {
		val datePickerState = rememberDatePickerState(
			initialSelectedDate = LocalDate.now()
		)
		Dialog(
			onDismissRequest = {
				onAction(MovieDetailsAction.DismissDialog)
			}
		) {
			Column(
				modifier = Modifier
					.background(MaterialTheme.colorScheme.background)
			) {
				DatePicker(
					state = datePickerState
				)
				Button(
					onClick = {
						onAction(MovieDetailsAction.ConfirmFinishedDate(datePickerState.getSelectedDate()!!))
						onAction(MovieDetailsAction.DismissDialog)
					},
					modifier = Modifier
						.align(Alignment.End)
				) {
					Text(
						text = stringResource(Res.string.confirm)
					)
				}
			}
		}
	}

	Box(modifier = Modifier
		.fillMaxSize()
		.background(MaterialTheme.colorScheme.background)
		.backHandler {
			onBackClick()
		}
	) {
		AsyncImage(
			model = state.movie?.backdropPath,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.fillMaxSize()
				.align(Alignment.BottomCenter)
		)
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.fillMaxSize()
				.background(
					brush = Brush.verticalGradient(listOf(Color.Black, Color.Black.copy(alpha = 0.6f)))
				)
				.padding(16.dp)
		) {
			IconButton(
				onClick = onBackClick,
			) {
				Icon(
					imageVector = Icons.Default.KeyboardArrowLeft,
					contentDescription = stringResource(Res.string.go_back),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			state.movie?.let { movie ->
				Column(
					verticalArrangement = Arrangement.spacedBy(8.dp),
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier
						.width(IntrinsicSize.Max)
				) {
					AsyncImage(
						model = movie.posterPath,
						contentScale = ContentScale.Crop,
						contentDescription = null,
						modifier = Modifier
							.width(300.dp)
							.aspectRatio(2/3f)
					)
					Button(
						enabled = movie.existsLocally(),
						onClick = {
							onAction(MovieDetailsAction.OnWatchClick)
						},
						modifier = Modifier
							.fillMaxWidth()
					) {
						Icon(
							imageVector = Icons.Default.PlayArrow,
							contentDescription = null
						)
						Text(
							text = stringResource(Res.string.watch)
						)
					}
					movie.runtime?.let { minutes ->
						Text(
							text = minutes.minutes.toString(),
							color = MaterialTheme.colorScheme.onBackground
						)
					}
					movie.tagline?.let { tagline ->
						if (tagline.isNotBlank()) {
							Text(
								text = tagline,
								color = MaterialTheme.colorScheme.onBackground,
								modifier = Modifier
									.widthIn(max = 300.dp)
							)
						}
					}
					if (movie.avgRating != null && movie.ratingsCount != null) {
						Row {
							Icon(
								imageVector = Icons.Default.Star,
								contentDescription = null,
								tint = MaterialTheme.colorScheme.primary
							)
							Text(
								text = "${movie.avgRating} (${movie.ratingsCount})",
								color = MaterialTheme.colorScheme.onBackground,
							)
						}
					}
					var expanded by remember { mutableStateOf(false) }
					ExposedDropdownMenuBox(
						expanded = expanded,
						onExpandedChange = { expanded = it }
					) {
						TextField(
							value = movie.status.toText(),
							onValueChange = {},
							readOnly = true,
							trailingIcon = {
								Icon(
									imageVector = Icons.Default.ArrowDropDown,
									contentDescription = null
								)
							},
							modifier = Modifier
								.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
						)
						ExposedDropdownMenu(
							expanded = expanded,
							onDismissRequest = { expanded = false }
						) {
							CompletionStatus.entries.forEach { status ->
								DropdownMenuItem(
									text = {
										Text(
											text = status.toText()
										)
									},
									onClick = {
										onAction(MovieDetailsAction.CompletionStatusChange(status))
										expanded = false
									}
								)
							}
						}
					}

				}
				Column(
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					Text(
						text = movie.title,
						fontSize = 36.sp,
						color = MaterialTheme.colorScheme.onBackground
					)
					movie.originalTitle?.let { title ->
						if (title != movie.title) {
							Text(
								text = "($title)",
								color = MaterialTheme.colorScheme.onBackground
							)
						}
					}
					movie.releaseDate?.let { date ->
						Text(
							text = date.toString(),
							color = MaterialTheme.colorScheme.onBackground
						)
					}
					if (movie.genres.isNotEmpty()) {
						Text(
							text = movie.genres.joinToString(),
							color = MaterialTheme.colorScheme.onBackground
						)
					}
					RatingStars(
						rating = movie.rating,
						onClick = { rating ->
							onAction(MovieDetailsAction.OnRatingClick(rating))
						}
					)
					movie.description?.let { description ->
						Text(
							text = description,
							color = MaterialTheme.colorScheme.onBackground,
							modifier = Modifier
								.widthIn(max = 1000.dp)
						)
					}
				}
				Spacer(modifier = Modifier.weight(1f))
				Column(
					horizontalAlignment = Alignment.End
				) {
					movie.budget?.let { budget ->
						val budgetString = stringResource(Res.string.budget)
						Text(
							text = "$budgetString $$budget",
							color = MaterialTheme.colorScheme.onBackground,
						)
					}
					movie.revenue?.let { revenue ->
						if (revenue > 0) {
							Text(
								text = stringResource(Res.string.revenue),
								color = MaterialTheme.colorScheme.secondary
							)
							HorizontalDivider(modifier = Modifier.widthIn(max = DIVIDER_WIDTH))
							Text(
								text = "$$revenue",
								color = MaterialTheme.colorScheme.onBackground,
							)
						}
					}
					if (movie.originCountries.isNotEmpty()) {
						Text(
							text = stringResource(Res.string.origin_countries),
							color = MaterialTheme.colorScheme.secondary
						)
						HorizontalDivider(modifier = Modifier.widthIn(max = DIVIDER_WIDTH))
						Column {
							movie.originCountries.forEach { country ->
								Text(
									text = country,
									color = MaterialTheme.colorScheme.onBackground
								)
							}
						}
					}
					if (movie.productionCompanies.isNotEmpty()) {
						Text(
							text = stringResource(Res.string.production_companies),
							color = MaterialTheme.colorScheme.secondary
						)
						HorizontalDivider(modifier = Modifier.widthIn(max = DIVIDER_WIDTH))
						Column {
							movie.productionCompanies.forEach { company ->
								Text(
									text = company,
									color = MaterialTheme.colorScheme.onBackground,
									textAlign = TextAlign.End,
									modifier = Modifier
										.fillMaxWidth()
								)
							}
						}
					}
				}
			}
		}
	}
}