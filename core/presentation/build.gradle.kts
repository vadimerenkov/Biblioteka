import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.buildkonfig)
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

				implementation(projects.updateChecker)
			}
		}
	}
}

compose.resources {
	publicResClass = true
}

buildkonfig {
	packageName = "vadimerenkov.biblioteka.core.presentation"

	defaultConfigs {
		buildConfigField(FieldSpec.Type.STRING, "versionName", libs.versions.version.name.get())
	}
}