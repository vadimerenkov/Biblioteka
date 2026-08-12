import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.io.FileInputStream
import java.util.Properties

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.serialization)
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

				implementation(projects.core.data)
				implementation(projects.core.domain)
				implementation(projects.books.domain)
			}
		}
	}
}

buildkonfig {
	packageName = "vadimerenkov.biblioteka.books.data"

	defaultConfigs {
		val prop = Properties().apply {
			load(FileInputStream(File(rootProject.rootDir, "local.properties")))
		}
		val apiKey = prop.getProperty("GOOGLE_API_KEY")
		buildConfigField(FieldSpec.Type.STRING, "GOOGLE_API_KEY", apiKey)
	}
}