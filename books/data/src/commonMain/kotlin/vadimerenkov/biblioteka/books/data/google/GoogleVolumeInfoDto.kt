package vadimerenkov.biblioteka.books.data.google

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleVolumeInfoDto(
	val id: String,
	@SerialName("volumeInfo") val book: GoogleBookDto
)
