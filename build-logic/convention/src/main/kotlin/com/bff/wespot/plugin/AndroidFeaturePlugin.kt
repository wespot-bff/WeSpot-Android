package com.bff.wespot.plugin

import com.android.build.gradle.LibraryExtension
import com.bff.wespot.plugin.configure.configureKotlinAndroid
import com.bff.wespot.plugin.configure.configureKtLint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class AndroidFeaturePlugin: Plugin<Project>{
    override fun apply(target: Project) {
        with(target){
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jlleitschuh.gradle.ktlint")
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureKotlinAndroid(extension)
            configureKtLint()

            extensions.configure<LibraryExtension> {
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
            }

            dependencies {
                "implementation"(project(":domain"))
                "implementation"(project(":core:model"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":designsystem"))
                "implementation"(project(":core:common"))
                "implementation"(project(":core:navigation"))
            }
        }
    }
}