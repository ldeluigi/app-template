pluginManagement {
    // The machinery's plugins: from a local copy of app-machinery when machinery.local names one, to change them
    // together with the app; otherwise from the machinery's Maven repository, at the version gradle/libs.versions.toml
    // pins (Dependabot moves it).
    val local = settings.providers.gradleProperty("machinery.local").orNull
    if (local != null) includeBuild("$local/plugins")
    repositories {
        maven(settings.providers.gradleProperty("machinery.repository").get()) {
            content { includeGroupByRegex("""machinery(\..*)?|app\.machinery""") }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.1.27"
}

// Conventional Commits, checked before every commit (releases are made from them), and the formatting check.
gitHooks {
    commitMsg { conventionalCommits() }
    preCommit { tasks("ktlintCheck") }
    createHooks(true)
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "app"

include(":core")
include(":composeApp")
include(":androidApp")
