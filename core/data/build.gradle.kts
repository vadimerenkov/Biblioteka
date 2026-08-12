import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.room)
	alias(libs.plugins.ksp)
	alias(libs.plugins.buildkonfig)
}

kotlin {
	jvm()
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.bundles.ktor)
				implementation(libs.koin.core)
				implementation(libs.androidx.room.runtime)
				implementation(libs.androidx.sqlite.bundled)
				implementation(libs.appdirs)

				implementation(projects.core.domain)
			}
		}
	}
}

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {
	ksp(libs.androidx.room.compiler)
}

buildkonfig {
	packageName = "vadimerenkov.biblioteka.core.data"

	defaultConfigs {
		buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "true")
	}

	defaultConfigs("release") {
		buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "false")
	}
}