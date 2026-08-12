package vadimerenkov.biblioteka.core.domain.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import vadimerenkov.biblioteka.core.domain.settings.Settings
import java.io.File

val coreDomainModule = module {
	singleOf(::Settings)
	single {
		PreferenceDataStoreFactory.createWithPath {
			File(System.getProperty("java.io.tmpdir"), "track_everything.preferences_pb").absolutePath.toPath()
		}
	}
	single {
		CoroutineScope(SupervisorJob())
	}
}