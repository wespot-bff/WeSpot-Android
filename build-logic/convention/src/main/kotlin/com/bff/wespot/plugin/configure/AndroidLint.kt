package com.bff.wespot.plugin.configure

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension

internal fun Project.configureKtLint() {
    extensions.configure<KtlintExtension> {
        android.set(true)
        verbose.set(true)
        outputToConsole.set(true)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        }
    }
}

internal fun Project.configureDetekt() {
    extensions.configure<DetektExtension> {
        parallel = true
        buildUponDefaultConfig = true
        toolVersion = libs.findVersion("detekt").get().toString()
        config.setFrom(files("$rootDir/config/detekt.yml"))
    }
}