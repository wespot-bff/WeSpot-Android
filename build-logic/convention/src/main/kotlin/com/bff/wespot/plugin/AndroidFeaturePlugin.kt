package com.bff.wespot.plugin

import com.android.build.gradle.LibraryExtension
import com.bff.wespot.plugin.configure.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidFeaturePlugin: Plugin<Project>{
    override fun apply(target: Project) {
        with(target){
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureKotlinAndroid(extension)
        }
    }
}