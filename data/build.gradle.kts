plugins {
    alias(libs.plugins.wespot.android.library)
    alias(libs.plugins.wespot.android.hilt)
}

android {
    namespace = "com.bff.wespot.data"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data-remote"))
    implementation(project(":core:model"))
    implementation(libs.junit)
    implementation(libs.kakao.sdk)
    implementation(libs.datastore)
}
