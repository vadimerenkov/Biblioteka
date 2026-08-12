package vadimerenkov.biblioteka.core.data.networking

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import vadimerenkov.biblioteka.core.domain.util.DataError
import vadimerenkov.biblioteka.core.domain.util.Result
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote> {
	return when (response.status.value) {
		in 200..299 -> {
			try {
				Result.Success(response.body<T>())
			} catch (e: NoTransformationFoundException) {
				Result.Failure(DataError.Remote.SERIALIZATION)
			}
		}
		400 -> Result.Failure(DataError.Remote.BAD_REQUEST)
		401 -> Result.Failure(DataError.Remote.UNAUTHORIZED)
		403 -> Result.Failure(DataError.Remote.FORBIDDEN)
		404 -> Result.Failure(DataError.Remote.NOT_FOUND)
		408 -> Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
		413 -> Result.Failure(DataError.Remote.PAYLOAD_TOO_LARGE)
		429 -> Result.Failure(DataError.Remote.TOO_MANY_REQUESTS)
		500 -> Result.Failure(DataError.Remote.SERVER_ERROR)
		503 -> Result.Failure(DataError.Remote.SERVICE_UNAVAILABLE)
		else -> Result.Failure(DataError.Remote.UNKNOWN)
	}
}

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Remote> {
	val response = try {
		execute()
	} catch (e: UnresolvedAddressException) {
		e.printStackTrace()
		return Result.Failure(DataError.Remote.NO_INTERNET)
	} catch (e: SerializationException) {
		e.printStackTrace()
		return Result.Failure(DataError.Remote.SERIALIZATION)
	} catch (e: Exception) {
		if (e is CancellationException) throw e
		e.printStackTrace()
		return Result.Failure(DataError.Remote.UNKNOWN)
	}
	return responseToResult(response)
}