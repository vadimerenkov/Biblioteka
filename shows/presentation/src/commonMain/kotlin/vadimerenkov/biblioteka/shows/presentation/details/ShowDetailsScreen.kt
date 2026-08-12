package vadimerenkov.biblioteka.shows.presentation.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.confirm
import biblioteka.core.presentation.generated.resources.finished
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.origin_countries
import biblioteka.core.presentation.generated.resources.production_companies
import biblioteka.core.presentation.generated.resources.season
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.RatingStars
import vadimerenkov.biblioteka.core.presentation.util.backHandler
import vadimerenkov.biblioteka.shows.presentation.util.existsLocally
import vadimerenkov.biblioteka.shows.presentation.util.toText
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

private val DIVIDER_WIDTH = 200.dp

@Composable
fun ShowDetailsScreen(
	viewModel: ShowDetailsViewModel = koinViewModel(),
	onBackClick: () -> Unit,
	onEpisodeClick: (String) -> Unit
) {
	ShowDetailsRoot(
		state = viewModel.state,
		onAction = { action ->
			when (action) {
				is ShowDetailsAction.OnEpisodeClick -> {
					onEpisodeClick(action.id)
				}
				else -> Unit
			}
			viewModel.onAction(action)
		},
		onBackClick = onBackClick
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ShowDetailsRoot(
	state: ShowDetailsState,
	onAction: (ShowDetailsAction) -> Unit,
	onBackClick: () -> Unit,
) {
	if (state.showFinishedDialog) {
		val datePickerState = rememberDatePickerState(
			initialSelectedDate = LocalDate.now()
		)
		Dialog(
			onDismissRequest = {
				onAction(ShowDetailsAction.DismissDialog)
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
						onAction(ShowDetailsAction.ConfirmFinishedDate(datePickerState.getSelectedDate()!!))
						onAction(ShowDetailsAction.DismissDialog)
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
			model = state.show?.backdropPath,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.fillMaxSize()
				.align(Alignment.BottomCenter)
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.fillMaxSize()
				.background(
					brush = Brush.verticalGradient(listOf(Color.Black, Color.Black.copy(alpha = 0.6f)))
				)
				.padding(16.dp)
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(16.dp),
				modifier = Modifier
					.fillMaxWidth()
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
				state.show?.let { show ->
					Column(
						verticalArrangement = Arrangement.spacedBy(8.dp),
						horizontalAlignment = Alignment.CenterHorizontally,
						modifier = Modifier
							.width(IntrinsicSize.Max)
					) {
						AsyncImage(
							model = show.posterPath,
							contentScale = ContentScale.Crop,
							contentDescription = null,
							modifier = Modifier
								.width(300.dp)
								.aspectRatio(2/3f)
						)
						show.episodeRuntime?.let { minutes ->
							Text(
								text = minutes.minutes.toString(),
								color = MaterialTheme.colorScheme.onBackground
							)
						}
						show.tagline?.let { tagline ->
							if (tagline.isNotBlank()) {
								Text(
									text = tagline,
									color = MaterialTheme.colorScheme.onBackground,
									modifier = Modifier
										.widthIn(max = 300.dp)
								)
							}
						}
						if (show.avgRating != null && show.ratingsCount != null) {
							Row {
								Icon(
									imageVector = Icons.Default.Star,
									contentDescription = null,
									tint = MaterialTheme.colorScheme.primary
								)
								Text(
									text = "${show.avgRating} (${show.ratingsCount})",
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
								value = show.status.toText(),
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
											onAction(ShowDetailsAction.CompletionStatusChange(status))
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
							text = show.title,
							fontSize = 36.sp,
							color = MaterialTheme.colorScheme.onBackground
						)
						show.originalTitle?.let { title ->
							if (title != show.title) {
								Text(
									text = "($title)",
									color = MaterialTheme.colorScheme.onBackground
								)
							}
						}
						show.firstAirDate?.let { date ->
							Text(
								text = date.toString(),
								color = MaterialTheme.colorScheme.onBackground
							)
						}
						if (show.genres.isNotEmpty()) {
							Text(
								text = show.genres.joinToString(),
								color = MaterialTheme.colorScheme.onBackground
							)
						}
						RatingStars(
							rating = show.rating,
							onClick = { rating ->
								onAction(ShowDetailsAction.OnRatingClick(rating))
							}
						)
						show.description?.let { description ->
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

						if (show.originCountries.isNotEmpty()) {
							Text(
								text = stringResource(Res.string.origin_countries),
								color = MaterialTheme.colorScheme.secondary
							)
							HorizontalDivider(modifier = Modifier.widthIn(max = DIVIDER_WIDTH))
							Column {
								show.originCountries.forEach { country ->
									Text(
										text = country,
										color = MaterialTheme.colorScheme.onBackground
									)
								}
							}
						}
						if (show.networks.isNotEmpty()) {
							Text(
								text = stringResource(Res.string.production_companies),
								color = MaterialTheme.colorScheme.secondary
							)
							HorizontalDivider(modifier = Modifier.widthIn(max = DIVIDER_WIDTH))
							Column {
								show.networks.forEach { company ->
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
			state.show?.let { show ->
				LazyColumn(
					verticalArrangement = Arrangement.spacedBy(8.dp),
					modifier = Modifier
						.padding(start = 32.dp)
				) {
					show.seasons.forEach { season ->
						stickyHeader {
							val seasonString = stringResource(Res.string.season)
							Text(
								text = "$seasonString ${season.seasonNumber}",
								color = MaterialTheme.colorScheme.onBackground,
								fontSize = 24.sp
							)
						}
						items(
							items = season.episodes,
							key = { it.id }
						) { episode ->
							Row(
								horizontalArrangement = Arrangement.spacedBy(8.dp),
								verticalAlignment = Alignment.CenterVertically,
								modifier = Modifier
									.fillMaxWidth()
									.clickable {
										onAction(ShowDetailsAction.OnEpisodeClick(episode.id))
									}
							) {
								IconButton(
									enabled = episode.existsLocally(),
									onClick = {
										onAction(ShowDetailsAction.OnPlayClick(episode.localPath))
									}
								) {
									Icon(
										imageVector = Icons.Default.PlayArrow,
										contentDescription = null,
										tint = MaterialTheme.colorScheme.primary
									)
								}
								Column {
									Text(
										text = "${episode.episodeNumber}. ${episode.title}",
										fontSize = 18.sp,
										color = MaterialTheme.colorScheme.onBackground
									)
									episode.localPath?.let {
										Text(
											text = it,
											fontSize = 12.sp,
											color = MaterialTheme.colorScheme.secondary
										)
									}
								}
								if (episode.status == CompletionStatus.FINISHED) {
									TooltipArea(
										tooltip = {
											Surface(
												modifier = Modifier.shadow(4.dp),
												color = Color(255, 255, 210),
												shape = RoundedCornerShape(4.dp)
											) {
												Text(
													text = stringResource(Res.string.finished),
													modifier = Modifier.padding(10.dp)
												)
											}
										}
									) {
										Icon(
											imageVector = Icons.Default.CheckCircle,
											contentDescription = null,
											tint = MaterialTheme.colorScheme.primary
										)
									}
								}
							}
						}
					}
				}
			}
		}
	}
}