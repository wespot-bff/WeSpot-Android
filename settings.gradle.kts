pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")
        }
    }
}

rootProject.name = "WeSpot"
include(":app")
include(":feature:auth")
include(":feature:message")
include(":domain")
include(":data")
include(":data-remote")
include(":designsystem")
include(":core:model")
include(":core:common")
include(":core:ui")
include(":core:network")
include(":core:navigation")
include(":feature:vote")
