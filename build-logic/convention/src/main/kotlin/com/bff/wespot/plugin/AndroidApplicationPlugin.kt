package com.bff.wespot.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.bff.wespot.plugin.configure.configureKotlinAndroid
import com.bff.wespot.plugin.configure.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.io.File

class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val file = File(target.rootDir, "local.properties")
        val property = loadProperties(file.absolutePath)

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

                signingConfigs {
                    create("release") {
                        storeFile = file("keystore/release.keystore")
                        storePassword = property.getProperty("SIGNED_STORE_PASSWORD")
                        keyAlias = property.getProperty("SIGNED_KEY_ALIAS")
                        keyPassword = property.getProperty("SIGNED_KEY_PASSWORD")
                    }
                }

                buildTypes {
                    debug {
                        isMinifyEnabled = false
                        isShrinkResources = false

                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }

                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true

                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                        signingConfig = signingConfigs.getByName("release")
                    }
                }
            }
        }
    }
}