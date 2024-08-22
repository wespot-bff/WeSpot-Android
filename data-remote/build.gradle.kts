plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.bff.wespot.data.remote"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.junit)
    implementation(libs.timber)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlin.serialization.json)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config)
}
