package vadimerenkov.biblioteka.books.data.google

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBookDto(
	val title: String,
	val authors: List<String>? = null,
	val publishedDate: String? = null,
	val description: String? = null,
	val pageCount: Int? = null,
	val averageRating: Double? = null,
	val ratingsCount: Int? = null,
	val imageLinks: ImageLinks? = null
)
