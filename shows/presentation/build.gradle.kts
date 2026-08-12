plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
}

kotlin {
	jvm()
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.compose.runtime)
				implementation(libs.compose.foundation)
				implementation(libs.compose.ui)
				implementation(libs.compose.material3)
				implementation(libs.bundles.koin)
				implementation(libs.bundles.coil)
				implementation(libs.material.icons)
				implementation(libs.compose.uiToolingPreview)
				implementation(libs.bundles.navigation3)
				implementation(libs.filekit.dialogs)
				implementation(libs.filekit.dialogs.compose)
				implementation(libs.compose.components.resources)
				implementation(libs.androidx.datastore)
				implementation(libs.androidx.datastore.preferences)

				implementation(projects.core.presentation)
				implementation(projects.core.domain)
				implementation(projects.shows.domain)
			}
		}
	}

}