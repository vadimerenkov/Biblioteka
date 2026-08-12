package vadimerenkov.biblioteka.core.data.database.books

import androidx.room.Entity
import androidx.room.PrimaryKey
import vadimerenkov.biblioteka.core.domain.CompletionStatus

@Entity
data class BookEntity(
	@PrimaryKey val id: String,
	val title: String,
	val coverUrl: String? = null,
	val description: String? = null,
	val firstPublishYear: Int? = null,
	val numPages: Int? = null,
	val avgOLRating: Double? = null,
	val numberOLRatings: Int? = null,
	val authors: String? = null,
	val rating: Int? = null,
	val status: CompletionStatus = CompletionStatus.NOT_STARTED,
	val startedOn: Long? = null,
	val finishedOn: Long? = null
)