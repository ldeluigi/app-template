// The Android app: an activity that shows the shared app (composeApp), its manifest, launcher icon and signing.
plugins {
    id("machinery.android-application")
}

dependencies {
    implementation(project(":composeApp"))
    implementation(libs.androidx.activity.compose)
    debugImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
}
