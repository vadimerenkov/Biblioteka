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
				implementation(libs.bundles.coil)

				implementation(libs.compose.runtime)
				implementation(libs.compose.foundation)
				implementation(libs.compose.ui)
				implementation(libs.compose.material3)
				implementation(libs.compose.components.resources)
				implementation(libs.material.icons)
			}
		}
	}
}

compose.resources {
	publicResClass = true
}