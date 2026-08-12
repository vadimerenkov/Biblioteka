plugins {
	alias(libs.plugins.kotlinMultiplatform)
}

kotlin {

	jvm()
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.kotlin.stdlib)
				implementation(libs.filekit.dialogs)
				implementation(libs.kotlinx.coroutinesSwing)

				implementation(projects.core.domain)
			}
		}
	}

}