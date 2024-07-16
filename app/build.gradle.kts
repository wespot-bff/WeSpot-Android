

plugins {
    alias(libs.plugins.wespot.android.application)
    alias(libs.plugins.wespot.android.hilt)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.firebase)
}

android {
    namespace = "com.bff.wespot"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:message"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.junit)
    implementation(libs.timber)
}
