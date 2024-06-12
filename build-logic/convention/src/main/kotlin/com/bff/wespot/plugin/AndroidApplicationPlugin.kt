package com.bff.wespot.plugin

import com.bff.wespot.plugin.configure.configureApplicationVersion
import com.bff.wespot.plugin.configure.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            configureKotlinAndroid()
            configureApplicationVersion()
        }
    }
}