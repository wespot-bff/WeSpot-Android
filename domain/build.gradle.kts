plugins {
    alias(libs.plugins.wespot.jvm.library)
}

dependencies {
    implementation(libs.junit)
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.paging3.domain)
    implementation(libs.java.inject)
    implementation(libs.kotlin.coroutines)
}
