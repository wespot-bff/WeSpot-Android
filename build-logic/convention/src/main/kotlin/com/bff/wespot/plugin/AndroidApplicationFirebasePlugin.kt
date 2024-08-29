package com.bff.wespot.plugin

import com.bff.wespot.plugin.configure.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationFirebasePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
                apply("com.google.firebase.firebase-perf")
            }

            dependencies {
                "implementation"(platform(libs.findLibrary("firebase-bom").get()))
                "implementation"(libs.findLibrary("firebase-analytics").get())
                "implementation"(libs.findLibrary("firebase-crashlytics").get())
                "implementation"(libs.findLibrary("firebase-performance").get())
                "implementation"(libs.findLibrary("firebase-messaging").get())
            }
        }
    }
}