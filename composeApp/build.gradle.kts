// The app itself, shared by Android and iOS: its screens, their state, and what each platform does its own way
// (src/androidMain, src/iosMain) behind the Platform interface. androidApp and iosApp only start it.
plugins {
    id("machinery.compose-library")
    id("machinery.serialization")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core"))
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.aboutlibraries.compose.m3)
            implementation(libs.kotlinx.io.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.play.app.update.ktx)
        }
        getByName("androidHostTest").dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
