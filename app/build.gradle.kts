import org.jetbrains.kotlin.konan.properties.Properties


val properties = Properties()
properties.load(rootProject.file("local.properties").inputStream())

plugins {
    alias(libs.plugins.wespot.android.application)
    alias(libs.plugins.wespot.android.hilt)
    alias(libs.plugins.wespot.android.compose)
    alias(libs.plugins.wespot.android.firebase)
}

android {
    namespace = "com.bff.wespot"

    defaultConfig {
        buildConfigField("String", "KAKAO_APP_KEY", properties.getProperty("KAKAO_APP_KEY"))

        buildConfigField("String", "FACEBOOK_APP_ID", properties.getProperty("FACEBOOK_APP_ID"))

        manifestPlaceholders["SCHEME_KAKAO_APP_KEY"] =
            properties.getProperty("SCHEME_KAKAO_APP_KEY")
    }

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions.add("wespot")
    productFlavors {
        create("dev") {
            dimension = "wespot"
            applicationIdSuffix = ".dev"
        }

        create("prod") {
            dimension = "wespot"
            applicationIdSuffix = ".prod"
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":data-remote"))
    implementation(project(":data-local"))
    implementation(project(":designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":core:analytics"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:vote"))
    implementation(project(":feature:message"))
    implementation(project(":feature:entire"))
    implementation(project(":feature:notification"))

    implementation(libs.kakao.sdk)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.junit)
    implementation(libs.timber)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kakao.link)
}
