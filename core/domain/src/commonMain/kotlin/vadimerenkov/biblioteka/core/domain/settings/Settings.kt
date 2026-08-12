package vadimerenkov.biblioteka.core.domain.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first


class Settings(
	private val dataStore: DataStore<Preferences>,
	private val applicationScope: CoroutineScope
) {

	private val _state = MutableStateFlow(SettingsState())
	val state = _state.asStateFlow()

	suspend fun <T> getSetting(key: Preferences.Key<T>): T? {
		return dataStore
			.data
			.first()
			.get(key)
	}

	suspend fun <T> saveSetting(key: Preferences.Key<T>, data: T) {
		dataStore.edit { prefs ->
			prefs[key] = data
		}
	}
}