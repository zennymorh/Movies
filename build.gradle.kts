// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    alias(libs.plugins.compose.compiler) apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}

detekt {
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    source.setFrom(files(project.projectDir))
    // Optional: Set the baseline file for ignoring existing issues (run ./gradlew detektBaseline first)
    // baseline = file("$rootDir/config/detekt/detekt-baseline.xml")
}
