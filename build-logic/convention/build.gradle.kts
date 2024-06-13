plugins{
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.build)
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.compose.compiler.extension)
    compileOnly(libs.ktlint.plugin)
    compileOnly(libs.detekt.plugin)
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "com.bff.wespot.application"
            implementationClass = "com.bff.wespot.plugin.AndroidApplicationPlugin"
        }
        create("androidLibrary") {
            id = "com.bff.wespot.library"
            implementationClass = "com.bff.wespot.plugin.AndroidLibraryPlugin"
        }
        create("androidFirebase") {
            id = "com.bff.wespot.firebase"
            implementationClass = "com.bff.wespot.plugin.AndroidApplicationFirebasePlugin"
        }
        create("androidFeature") {
            id = "com.bff.wespot.feature"
            implementationClass = "com.bff.wespot.plugin.AndroidFeaturePlugin"
        }
        create("androidCompose") {
            id = "com.bff.wespot.compose"
            implementationClass = "com.bff.wespot.plugin.AndroidComposePlugin"
        }
        create("androidHilt") {
            id = "com.bff.wespot.hilt"
            implementationClass = "com.bff.wespot.plugin.AndroidHiltPlugin"
        }
        create("androidLint") {
            id = "com.bff.wespot.lint"
            implementationClass = "com.bff.wespot.plugin.AndroidLintPlugin"
        }
    }
}