plugins {
	alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
	jvm()
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.androidx.datastore)
				implementation(libs.androidx.datastore.preferences)
				implementation(libs.koin.core)
				implementation(libs.filekit.dialogs)
				implementation(libs.filekit.coil)
			}
		}
	}
}