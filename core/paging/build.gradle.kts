plugins {
    alias(libs.plugins.wespot.android.library)
}

android {
    namespace = "com.bff.wespot.paging"
}

dependencies {
    implementation(libs.junit)
    implementation(libs.timber)
    implementation(project(":core:model"))
    implementation(libs.paging3)
}
