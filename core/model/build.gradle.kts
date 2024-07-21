plugins {
    alias(libs.plugins.wespot.jvm.library)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.serialization.json)
}
