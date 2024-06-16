package com.bff.wespot.plugin

import com.android.build.gradle.BaseExtension
import com.bff.wespot.plugin.configure.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class AndroidComposePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            dependencies {
                "implementation"(platform(libs.findLibrary("androidx-compose-bom").get()))
                "implementation"(libs.findBundle("androidx-compose").get())
            }

            extensions.configure<BaseExtension> {
                buildFeatures.compose = true
            }

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                enableStrongSkippingMode.set(true)
                includeSourceInformation.set(true)
            }
        }
    }
}