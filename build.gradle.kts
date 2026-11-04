// The machinery's convention plugins, at the version gradle/libs.versions.toml pins: each module applies the ones it
// needs (app-machinery's docs/06-how-it-works.md).
plugins {
    alias(libs.plugins.machinery.kmp.library) apply false
    alias(libs.plugins.machinery.compose.library) apply false
    alias(libs.plugins.machinery.android.application) apply false
    alias(libs.plugins.machinery.serialization) apply false
}
