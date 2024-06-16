package com.bff.wespot.plugin

import com.bff.wespot.plugin.configure.configureDetekt
import com.bff.wespot.plugin.configure.configureKtLint
import com.bff.wespot.plugin.configure.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jlleitschuh.gradle.ktlint")
                apply("io.gitlab.arturbosch.detekt")
            }

            configureKtLint()
            configureDetekt()

            dependencies {
                "detektPlugins"(libs.findLibrary("detekt-formatting").get())
            }
        }
    }
}