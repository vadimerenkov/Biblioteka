import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.navigation3)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.bundles.coil)
    implementation(libs.filekit.dialogs)
    implementation(libs.filekit.dialogs.compose)
    implementation(libs.filekit.coil)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.compose.material3)
    implementation(libs.compose.components.resources)
    implementation(libs.material.icons)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.books.data)
    implementation(projects.books.domain)
    implementation(projects.books.presentation)
    implementation(projects.movies.presentation)
    implementation(projects.movies.domain)
    implementation(projects.movies.data)
    implementation(projects.shows.data)
    implementation(projects.shows.domain)
    implementation(projects.shows.presentation)
    implementation(projects.updateChecker)
}

compose.desktop {
    application {
        mainClass = "vadimerenkov.biblioteka.MainKt"

        nativeDistributions {
            includeAllModules = true
            targetFormats(TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = "Biblioteka"
            packageVersion = libs.versions.version.name.get()
            description = "Biblioteka - unified media library"
            copyright = "2026 © Vadim Erenkov"
            vendor = "Vadim Erenkov"

            windows {
                dirChooser = true
                menuGroup = "Biblioteka"
                iconFile.set(project.file("app_icon.ico"))
            }
        }
    }
}