import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.io.FileInputStream
import java.util.Properties

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
				implementation(libs.koin.core)
				implementation(libs.bundles.ktor)
				implementation(libs.filekit.dialogs)
				implementation(libs.kotlinx.serialization.core)
				implementation(libs.kotlinx.serialization.json)

				implementation(projects.core.data)
				implementation(projects.core.domain)
				implementation(projects.movies.domain)
			}
		}
	}
}

buildkonfig {
	packageName = "vadimerenkov.biblioteka.movies.data"

	defaultConfigs {
		val prop = Properties().apply {
			load(FileInputStream(File(rootProject.rootDir, "local.properties")))
		}
		val apiKey = prop.getProperty("TMDB_API_KEY")
		buildConfigField(FieldSpec.Type.STRING, "TMDB_API_KEY", apiKey)
	}
}