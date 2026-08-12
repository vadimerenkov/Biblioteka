package vadimerenkov.biblioteka.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biblioteka.core.presentation.generated.resources.Res
import biblioteka.core.presentation.generated.resources.choose_date
import biblioteka.core.presentation.generated.resources.confirm
import org.jetbrains.compose.resources.stringResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDateField(
	text: String,
	date: LocalDate?,
	onDateChange: (LocalDate?) -> Unit
) {
	val formattedDate = if (date == null) "" else DateTimeFormatter.ISO_DATE.format(date)
	var menuOpen by remember { mutableStateOf(false) }
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(16.dp),
	) {
		Text(
			text = text,
			fontSize = 18.sp,
			color = MaterialTheme.colorScheme.onBackground
		)
		OutlinedTextField(
			value = formattedDate,
			onValueChange = {},
			readOnly = true,
			trailingIcon = {
				Icon(
					imageVector = Icons.Default.DateRange,
					contentDescription = stringResource(Res.string.choose_date),
					modifier = Modifier
						.clickable {
							menuOpen = true
						}
				)
			}
		)
	}
	if (menuOpen) {
		val state = rememberDatePickerState()
		DatePickerDialog(
			onDismissRequest = { menuOpen = false },
			confirmButton = {
				Button(
					onClick = {
						onDateChange(state.getSelectedDate())
						menuOpen = false
					}
				) {
					Text(
						text = stringResource(Res.string.confirm)
					)
				}
			}
		) {
			DatePicker(
				state = state
			)
		}
	}
}