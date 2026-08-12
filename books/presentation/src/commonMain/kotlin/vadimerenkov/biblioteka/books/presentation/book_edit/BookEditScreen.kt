package vadimerenkov.biblioteka.books.presentation.book_edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.add_cover_image
import biblioteka.core.presentation.generated.resources.authors
import biblioteka.core.presentation.generated.resources.confirm
import biblioteka.core.presentation.generated.resources.description
import biblioteka.core.presentation.generated.resources.finished_on
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.published_year
import biblioteka.core.presentation.generated.resources.started_on
import biblioteka.core.presentation.generated.resources.title
import coil3.compose.AsyncImage
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.books.presentation.util.toText
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.FormDateField
import vadimerenkov.biblioteka.core.presentation.components.FormTextField
import vadimerenkov.biblioteka.core.presentation.util.backHandler

@Composable
fun BookEditScreen(
	viewModel: BookEditViewModel = koinViewModel(),
	onConfirmClick: () -> Unit,
	onBackClick: () -> Unit
) {

	LaunchedEffect(viewModel.events) {
		withContext(Dispatchers.Main.immediate) {
			viewModel.events.collect { event ->
				when (event) {
					BookEditEvent.WritingFinished -> onConfirmClick()
				}
			}
		}
	}

	BookEditRoot(
		state = viewModel.state,
		onAction = viewModel::onAction,
		onBackClick = onBackClick
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun BookEditRoot(
	state: BookEditState,
	onAction: (BookEditAction) -> Unit,
	onBackClick: () -> Unit
) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.backHandler {
				onBackClick()
			}
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
		val launcher = rememberFilePickerLauncher(
			type = FileKitType.Image
		) { image ->
			if (image != null) {
				onAction(BookEditAction.ChangeImageFile(image))
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
					.aspectRatio(2/3f)
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
				if (state.coverUrl == null) {
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
						model = state.coverUrl,
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
					value = state.status.toText(),
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
								onAction(BookEditAction.StatusChange(status))
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
							tint = if (state.rating != null && state.rating >= star + 1) MaterialTheme.colorScheme.primary else Color.Gray,
							modifier = Modifier
								.clickable {
									onAction(BookEditAction.RatingChange(star + 1))
								}
						)
					}
				}
			}
		}
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			FormTextField(
				text = stringResource(Res.string.title),
				state = state.titleTextState
			)
			FormTextField(
				text = stringResource(Res.string.authors),
				state = state.authorTextState
			)
			FormTextField(
				text = stringResource(Res.string.published_year),
				state = state.publishedYearTextState
			)
			FormTextField(
				text = stringResource(Res.string.description),
				state = state.descriptionTextState
			)
			FormDateField(
				text = stringResource(Res.string.started_on),
				date = state.startedDate,
				onDateChange = { date ->
					onAction(BookEditAction.StartedDateChange(date))
				}
			)
			FormDateField(
				text = stringResource(Res.string.finished_on),
				date = state.finishedDate,
				onDateChange = { date ->
					onAction(BookEditAction.FinishedDateChange(date))
				}
			)

			Button(
				onClick = {
					onAction(BookEditAction.OnConfirmClick)
				},
				enabled = state.isValid
			) {
				Text(
					text = stringResource(Res.string.confirm)
				)
			}
		}
	}
}

@Composable
@Preview
private fun BookEditPreview() {
	BookEditRoot(
		state = BookEditState(),
		onAction = {},
		onBackClick = {}
	)
}