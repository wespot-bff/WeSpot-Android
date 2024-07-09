/**
 * https://github.com/JLLeitschuh/ktlint-gradle
 */

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
        additionalEditorconfig.set (
            mapOf(
                "ktlint_standard_no-wildcard-imports" to "disabled",
                "ktlint_standard_filename" to "disabled",
                "ktlint_standard_function-naming" to "disabled",
                "ktlint_standard_class-naming" to "disabled",
                "ktlint_standard_annotation" to "disabled",
                "ktlint_standard_blank-line-before-declaration" to "disabled",
            )
        )
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
