package vadimerenkov.biblioteka.books.data.google

import kotlinx.serialization.Serializable

@Serializable
data class ImageLinks(
	val smallThumbnail: String? = null,
	val thumbnail: String? = null,
	val small: String? = null,
	val medium: String? = null,
	val large: String? = null,
	val extraLarge: String? = null
)
