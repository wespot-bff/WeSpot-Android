package com.bff.wespot.plugin

import com.bff.wespot.plugin.configure.configureKtLint
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKtLintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jlleitschuh.gradle.ktlint")
            }

            configureKtLint()
        }
    }
}