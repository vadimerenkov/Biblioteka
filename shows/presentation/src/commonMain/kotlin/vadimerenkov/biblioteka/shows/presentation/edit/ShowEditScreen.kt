package vadimerenkov.biblioteka.shows.presentation.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.add_cover_image
import biblioteka.core.presentation.generated.resources.choose_local_path_for_season
import biblioteka.core.presentation.generated.resources.confirm
import biblioteka.core.presentation.generated.resources.delete_season
import biblioteka.core.presentation.generated.resources.folder
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.search_tmdb
import biblioteka.core.presentation.generated.resources.title
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.FormTextField
import vadimerenkov.biblioteka.core.presentation.util.backHandler
import vadimerenkov.biblioteka.shows.presentation.search.ShowSearchScreen
import vadimerenkov.biblioteka.shows.presentation.search.ShowSearchViewModel
import vadimerenkov.biblioteka.shows.presentation.util.toText

@Composable
fun ShowEditScreen(
	viewModel: ShowEditViewModel = koinViewModel(),
	onBackClick: () -> Unit
) {
	LaunchedEffect(viewModel.events) {
		withContext(Dispatchers.Main.immediate) {
			viewModel.events.collect { event ->
				when (event) {
					ShowEditEvent.FinishedWriting -> onBackClick()
				}
			}
		}
	}

	ShowEditRoot(
		state = viewModel.state,
		onAction = viewModel::onAction,
		onBackClick = onBackClick
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ShowEditRoot(
	state: ShowEditState,
	onAction: (ShowEditAction) -> Unit,
	onBackClick: () -> Unit
) {
	if (state.showSearchScreen) {
		Dialog(
			onDismissRequest = {
				onAction(ShowEditAction.CloseSearchWindow)
			}
		) {
			Window(
				onCloseRequest = {
					onAction(ShowEditAction.CloseSearchWindow)
				}
			) {
				val viewModel: ShowSearchViewModel = koinViewModel { parametersOf(state.titleState.text.toString()) }
				ShowSearchScreen(
					viewModel = viewModel,
					showConfirmButton = true,
					onShowSelected = { tmdbId ->
						onAction(ShowEditAction.LoadSelectedShow(tmdbId))
					},
					onBackClick = {
						onAction(ShowEditAction.CloseSearchWindow)
					}
				)
			}
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.backHandler {
				onBackClick()
			}
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
		) {
			IconButton(
				onClick = onBackClick
			) {
				Icon(
					imageVector = Icons.Default.KeyboardArrowLeft,
					contentDescription = stringResource(Res.string.go_back),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(modifier = Modifier.weight(1f))
			IconButton(
				onClick = {
					onAction(ShowEditAction.OnSearchClick)
				}
			) {
				Icon(
					imageVector = Icons.Default.Search,
					contentDescription = stringResource(Res.string.search_tmdb),
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(modifier = Modifier.weight(1f))
		}
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			val launcher = rememberFilePickerLauncher(
				type = FileKitType.Image
			) { image ->
				if (image != null) {
					onAction(ShowEditAction.ChangeImageFile(image))
				}
			}
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.height(350.dp)
						.aspectRatio(2 / 3f)
						.border(
							width = 2.dp,
							color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
							shape = RoundedCornerShape(16.dp)
						)
						.clip(RoundedCornerShape(16.dp))
						.clickable {
							launcher.launch()
						}

				) {
					if (state.show?.posterPath == null) {
						Column(
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							Icon(
								imageVector = Icons.Default.Add,
								contentDescription = null,
								tint = MaterialTheme.colorScheme.onBackground
							)
							Text(
								text = stringResource(Res.string.add_cover_image),
								color = MaterialTheme.colorScheme.onBackground
							)
						}
					} else {
						AsyncImage(
							model = state.show.posterPath,
							contentDescription = null,
							contentScale = ContentScale.Crop
						)
					}
				}
				var expanded by remember { mutableStateOf(false) }
				ExposedDropdownMenuBox(
					expanded = expanded,
					onExpandedChange = { expanded = it }
				) {
					TextField(
						value = state.show?.status!!.toText(),
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
									onAction(ShowEditAction.StatusChange(status))
									expanded = false
								}
							)
						}
					}
				}
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
								tint = if (state.show?.rating != null && state.show.rating!! >= star + 1) MaterialTheme.colorScheme.primary else Color.Gray,
								modifier = Modifier
									.clickable {
										onAction(ShowEditAction.RatingChange(star + 1))
									}
							)
						}
					}
				}
			}
			Column {
				FormTextField(
					text = stringResource(Res.string.title),
					state = state.titleState
				)
				Button(
					onClick = {
						onAction(ShowEditAction.OnConfirmClick)
					}
				) {
					Text(
						text = stringResource(Res.string.confirm)
					)
				}
			}
		}
		state.show?.let { show ->
			Row(
				horizontalArrangement = Arrangement.spacedBy(16.dp),
				modifier = Modifier
					.horizontalScroll(rememberScrollState())
			) {
				show.seasons
					.sortedBy { it.seasonNumber }
					.forEach { season ->
					Column(
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						val launcher = rememberDirectoryPickerLauncher { directory ->
							if (directory != null) {
								onAction(ShowEditAction.SelectFolderForSeason(season.id, directory))
							}
						}
						Row(
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier
								.fillMaxWidth()
						) {
							Text(
								text = season.seasonNumber.toString(),
								fontSize = 24.sp,
								color = MaterialTheme.colorScheme.primary
							)
							Spacer(modifier = Modifier.weight(1f))
							IconButton(
								onClick = {
									launcher.launch()
								}
							) {
								Icon(
									painter = painterResource(Res.drawable.folder),
									contentDescription = stringResource(Res.string.choose_local_path_for_season),
									tint = MaterialTheme.colorScheme.secondary
								)
							}
							IconButton(
								onClick = {
									onAction(ShowEditAction.OnDeleteSeasonClick(season))
								}
							) {
								Icon(
									imageVector = Icons.Default.Delete,
									contentDescription = stringResource(Res.string.delete_season),
									tint = MaterialTheme.colorScheme.secondary
								)
							}
						}
						LazyColumn(
							modifier = Modifier
								.border(
									width = 2.dp,
									color = MaterialTheme.colorScheme.secondary,
									shape = RoundedCornerShape(12.dp)
								)
								.padding(16.dp)
						) {
							items(
								items = season.episodes,
								key = { it.id }
							) { episode ->
								Column(
									modifier = Modifier
										.widthIn(max = 350.dp)
								) {
									Row(
										verticalAlignment = Alignment.CenterVertically
									) {
										if (episode.episodeNumber != null) {
											Text(
												text = "${episode.episodeNumber}. ",
												color = MaterialTheme.colorScheme.onBackground,
												fontSize = 18.sp,
											)
										}
										Text(
											text = episode.title,
											color = MaterialTheme.colorScheme.onBackground,
											fontSize = 18.sp,
											maxLines = 1,
											overflow = TextOverflow.Ellipsis
										)
									}
									if (episode.localPath != null) {
										TooltipArea(
											tooltip = {
												Surface(
													modifier = Modifier.shadow(4.dp),
													color = Color(255, 255, 210),
													shape = RoundedCornerShape(4.dp)
												) {
													Text(
														text = "${episode.localPath}",
														modifier = Modifier.padding(10.dp)
													)
												}
											},
											delayMillis = 500
										) {
											Text(
												text = episode.localPath!!,
												color = MaterialTheme.colorScheme.secondary,
												fontSize = 12.sp,
												maxLines = 1,
												overflow = TextOverflow.Ellipsis
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
}