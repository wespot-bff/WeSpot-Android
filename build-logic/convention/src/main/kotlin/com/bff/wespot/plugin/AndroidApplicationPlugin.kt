package com.bff.wespot.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.bff.wespot.plugin.configure.configureKotlinAndroid
import com.bff.wespot.plugin.configure.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)

                defaultConfig {
                    versionCode = libs.findVersion("versionCode").get().requiredVersion.toInt()
                    versionName = libs.findVersion("versionName").get().requiredVersion

                    targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                    applicationId = "com.bff.wespot"
                }
            }
        }
    }
}