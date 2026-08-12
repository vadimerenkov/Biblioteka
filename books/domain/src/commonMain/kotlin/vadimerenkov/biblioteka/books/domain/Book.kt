package vadimerenkov.biblioteka.books.domain

import vadimerenkov.biblioteka.core.domain.CompletionStatus
import java.time.LocalDate

data class Book(
	val id: String,
	val title: String,
	val coverUrl: String? = null,
	val description: String? = null,
	val firstPublishYear: Int? = null,
	val numPages: Int? = null,
	val avgOLRating: Double? = null,
	val numberOLRatings: Int? = null,
	val authors: List<String> = emptyList(),
	val rating: Int? = null,
	val status: CompletionStatus = CompletionStatus.NOT_STARTED,
	val startedOn: LocalDate? = null,
	val finishedOn: LocalDate? = null
)
