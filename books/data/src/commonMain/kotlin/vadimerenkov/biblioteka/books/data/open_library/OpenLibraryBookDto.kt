package vadimerenkov.biblioteka.books.data.open_library

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenLibraryBookDto(
	@SerialName("key") val id: String,
	@SerialName("title") val title: String,
	@SerialName("description") val description: String? = null,
	@SerialName("language") val languages: List<String>? = null,
	@SerialName("cover_i") val coverAlternativeKey: Int? = null,
	@SerialName("author_key") val authorKeys: List<String>? = null,
	@SerialName("author_name") val authorNames: List<String>? = null,
	@SerialName("cover_edition_key") val coverKey: String? = null,
	@SerialName("first_publish_year") val firstPublishYear: Int? = null,
	@SerialName("ratings_average") val ratingsAverage: Double? = null,
	@SerialName("ratings_count") val ratingsCount: Int? = null,
	@SerialName("number_of_pages_median") val numPagesMedian: Int? = null,
	@SerialName("edition_count") val numEditions: Int? = null,
)
