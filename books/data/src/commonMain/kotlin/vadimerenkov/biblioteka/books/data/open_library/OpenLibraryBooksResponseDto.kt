package vadimerenkov.biblioteka.books.data.open_library

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenLibraryBooksResponseDto(
	@SerialName("docs") val books: List<OpenLibraryBookDto>
)
