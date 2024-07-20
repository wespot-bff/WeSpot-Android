plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.bff.wespot.network"
}

dependencies {
    implementation(libs.junit)
    implementation(libs.timber)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlin.serialization.json)
    implementation(project(":core:model"))
}
