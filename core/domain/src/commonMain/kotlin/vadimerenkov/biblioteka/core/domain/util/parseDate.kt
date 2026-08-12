package vadimerenkov.biblioteka.core.domain.util

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

fun parseDate(date: String?): LocalDate? {
	return when {
		date == null -> null
		date.length == 4 -> {
			val year = date.toIntOrNull()
			if (year == null) null else LocalDate.ofYearDay(year, 1)
		}
		date.length == 7 -> YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1)
		date.length == 10 -> LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
		else -> null
	}
}