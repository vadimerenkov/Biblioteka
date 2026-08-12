package vadimerenkov.biblioteka.books.presentation.book_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.confirm
import biblioteka.core.presentation.generated.resources.go_back
import biblioteka.core.presentation.generated.resources.no_description
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.books.presentation.util.toText
import vadimerenkov.biblioteka.core.domain.CompletionStatus
import vadimerenkov.biblioteka.core.presentation.components.RatingStars
import vadimerenkov.biblioteka.core.presentation.util.backHandler
import java.time.LocalDate
import kotlin.math.round

@Composable
fun BookDetailsScreen(
	viewModel: BookDetailsViewModel = koinViewModel(),
	onBackClick: () -> Unit
) {
	BookDetailsRoot(
		state = viewModel.state,
		book = viewModel.state.book,
		onAction = viewModel::onAction,
		onBackClick = onBackClick
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun BookDetailsRoot(
	state: BookDetailsState,
	book: Book,
	onAction: (BookDetailsAction) -> Unit,
	onBackClick: () -> Unit
) {
	if (state.showFinishedDialog) {
		val datePickerState = rememberDatePickerState(
			initialSelectedDate = LocalDate.now()
		)
		Dialog(
			onDismissRequest = {
				onAction(BookDetailsAction.DismissDialog)
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
						onAction(BookDetailsAction.ConfirmFinishedDate(datePickerState.getSelectedDate()!!))
						onAction(BookDetailsAction.DismissDialog)
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

	Row(
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.padding(16.dp)
			.backHandler {
				onBackClick()
			}
			.verticalScroll(rememberScrollState())
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
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			AsyncImage(
				model = book.coverUrl,
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.width(350.dp)
					.aspectRatio(2/3f)
			)
			book.avgOLRating?.let {
				val rating = round(it * 10) / 10.0 * 2
				Row {
					Icon(
						imageVector = Icons.Default.Star,
						tint = MaterialTheme.colorScheme.primary,
						contentDescription = null
					)
					Text(
						text = "$rating (${book.numberOLRatings})",
						fontSize = 18.sp,
						color = MaterialTheme.colorScheme.onBackground
					)
				}
			}
			var expanded by remember { mutableStateOf(false) }
			ExposedDropdownMenuBox(
				expanded = expanded,
				onExpandedChange = { expanded = it }
			) {
				TextField(
					value = book.status.toText(),
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
								onAction(BookDetailsAction.StatusChange(status))
								expanded = false
							}
						)
					}
				}
			}
		}
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			Text(
				text = if (book.firstPublishYear == null) book.title else "${book.title} (${book.firstPublishYear})",
				fontSize = 30.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			Text(
				text = book.authors.joinToString(),
				fontSize = 24.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			RatingStars(
				rating = state.book.rating,
				onClick = { rating ->
					onAction(BookDetailsAction.OnRatingClick(rating))
				}
			)
			Text(
				text = if (book.description == null) stringResource(Res.string.no_description) else book.description!!,
				color = MaterialTheme.colorScheme.onBackground
			)
		}
	}
}