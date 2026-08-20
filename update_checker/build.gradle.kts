import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.buildkonfig)
	alias(libs.plugins.serialization)
}

kotlin {
	jvm()
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.bundles.ktor)

				implementation(projects.core.domain)
			}
		}
	}

}

buildkonfig {
	packageName = "vadimerenkov.biblioteka.updateChecker"

	defaultConfigs {
		buildConfigField(FieldSpec.Type.STRING, "versionName", libs.versions.version.name.get())
	}
}