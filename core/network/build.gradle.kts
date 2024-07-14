import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}


android {
    namespace = "com.bff.wespot.network"

    defaultConfig {
        buildConfigField("String", "MOCK_BASE_URL", properties.getProperty("MOCK_BASE_URL"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.junit)
    implementation(libs.timber)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlin.serialization.json)
    implementation(project(":core:model"))
}
