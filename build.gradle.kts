// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.daggerHiltAndroid) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp) apply false
}

detekt {
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    source.setFrom(files(project.projectDir))
    autoCorrect = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    exclude("**/build/**")
    exclude("**/generated/**")
    exclude("**/resources/**")
    exclude("**/.gradle/**")
    exclude("**/.*")
}

ktlint { }

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}
