package com.bff.wespot.plugin

import com.bff.wespot.plugin.configure.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            configureKotlinAndroid()
        }
    }
}