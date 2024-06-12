package com.bff.wespot.plugin.configure

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureApplicationVersion() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    extensions.getByType<BaseExtension>().apply {
        defaultConfig {
            versionCode = libs.findVersion("versionCode").get().requiredVersion.toInt()
            versionName = libs.findVersion("versionName").get().requiredVersion
        }
    }
}