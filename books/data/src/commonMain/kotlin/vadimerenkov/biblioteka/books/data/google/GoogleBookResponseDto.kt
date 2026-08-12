package vadimerenkov.biblioteka.books.data.google

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleBookResponseDto(
	@SerialName("items") val items: List<GoogleVolumeInfoDto>
)
