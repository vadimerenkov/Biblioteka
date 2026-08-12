package vadimerenkov.biblioteka.books.data

import kotlinx.serialization.json.Json
import vadimerenkov.biblioteka.books.data.google.GoogleBookDto
import vadimerenkov.biblioteka.books.data.open_library.OpenLibraryBookDto
import vadimerenkov.biblioteka.books.domain.Book
import vadimerenkov.biblioteka.core.data.database.books.BookEntity
import vadimerenkov.biblioteka.core.domain.util.parseDate
import java.time.LocalDate

fun OpenLibraryBookDto.toBook(): Book {
	return Book(
		id = id.removePrefix("/works/"),
		title = title,
		coverUrl = if(coverKey != null) {
			"https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
		} else {
			"https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
		},
		firstPublishYear = firstPublishYear,
		numPages = numPagesMedian,
		avgOLRating = ratingsAverage,
		numberOLRatings = ratingsCount,
		authors = authorNames ?: emptyList(),
		description = description
	)
}

fun Book.toEntity(): BookEntity {
	return BookEntity(
		id = id,
		title = title,
		coverUrl = coverUrl,
		description = description,
		firstPublishYear = firstPublishYear,
		numPages = numPages,
		avgOLRating = avgOLRating,
		numberOLRatings = numberOLRatings,
		authors = Json.encodeToString(authors),
		rating = rating,
		status = status,
		startedOn = startedOn?.toEpochDay(),
		finishedOn = finishedOn?.toEpochDay()
	)
}

fun BookEntity.toBook(): Book {
	return Book(
		id = id,
		title = title,
		coverUrl = coverUrl,
		description = description,
		firstPublishYear = firstPublishYear,
		numPages = numPages,
		avgOLRating = avgOLRating,
		numberOLRatings = numberOLRatings,
		authors = if (authors == null) emptyList() else Json.decodeFromString(authors!!),
		rating = rating,
		status = status,
		startedOn = if (startedOn == null) null else LocalDate.ofEpochDay(startedOn!!),
		finishedOn = if (finishedOn == null) null else LocalDate.ofEpochDay(finishedOn!!),
	)
}

fun GoogleBookDto.toBook(id: String): Book {
	val year = parseDate(publishedDate)?.year
	return Book(
		id = id,
		title = title,
		coverUrl = imageLinks?.medium ?: imageLinks?.small ?: imageLinks?.thumbnail ?: imageLinks?.smallThumbnail,
		description = description,
		firstPublishYear = year,
		numPages = pageCount,
		avgOLRating = averageRating,
		numberOLRatings = ratingsCount,
		authors = authors ?: emptyList()
	)
}