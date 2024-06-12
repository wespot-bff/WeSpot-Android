package com.bff.wespot.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationFirebasePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                "implementation"(platform(libs.findLibrary("firebase-bom").get()))
                "implementation"(libs.findLibrary("firebase-analytics").get())
                "implementation"(libs.findLibrary("firebase-crashlytics").get())
                "implementation"(libs.findLibrary("firebase-messaging").get())
            }
        }
    }
}