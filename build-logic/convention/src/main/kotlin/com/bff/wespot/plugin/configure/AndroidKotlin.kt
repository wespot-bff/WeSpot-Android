package com.bff.wespot.plugin.configure

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(){
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    extensions.getByType<BaseExtension>().apply {
        setCompileSdkVersion(libs.findVersion("compileSdk").get().requiredVersion.toInt())

        defaultConfig {
            minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
            targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        extensions.getByType<KotlinAndroidProjectExtension>().apply {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        sourceSets {
            getByName("main") {
                java.srcDir("src/main/kotlin")
            }
        }

        packagingOptions {
            resources.excludes.apply {
                add("/META-INF/AL2.0")
                add("/META-INF/LGPL2.1")
            }
        }
    }
}