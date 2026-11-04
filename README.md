# App Template

A Kotlin Multiplatform app for Android and iOS, ready to grow into yours: one Kotlin codebase, Compose Multiplatform
screens, and everything else (CI, releases to the stores, AI agents that take issues and fix builds, dependency
updates, repository settings) provided by app-machinery, so this repository holds the app and little else.

It starts as a tiny counter with an About screen, the stores' update prompts, translations, and the seams for
purchases and ads (unused until you want them).

## Make it yours

1. Create your repository from this template (GitHub's **Use this template**, or
   `gh repo create <owner>/<repo> --template <owner>/app-template --private --clone`).
2. Give it its name and id:

   ```sh
   scripts/make-it-yours.sh "<Name>" <app.id>
   ```

   The id (like `com.example.myapp`) is the Android application id and the iOS bundle id: it can never change once
   the app is on a store, so name what the app does, not its brand.
3. Describe the app in this README and in AGENTS.md ("The app"), draw its icon
   (`composeApp/src/androidMain/res/drawable/ic_launcher_*.xml`), and pick its colours (`ui/theme/AppTheme.kt`).
4. `./gradlew check` (the first run redraws the iPhone icon from the launcher icon and asks you to commit it), then
   commit and push.
5. Follow app-machinery's `docs/02-new-app.md` from step 3: settings, secrets, signing, the stores.

## Build and run

- **Requirements**: a JDK 21 or newer (Android Studio brings one) and the Android SDK (`local.properties` points to
  it, as Android Studio writes it). iOS builds need a Mac with Xcode; without one, CI builds and tests them.
- `./gradlew check`: every test and check.
- `./gradlew :androidApp:installDebug`: the app on a connected phone or emulator.
- iOS on a Mac: `cd iosApp && xcodegen generate && open App.xcodeproj`.
- `./gradlew compileKotlinIosSimulatorArm64`: compiles the iOS code on any computer.

## How it is organized

AGENTS.md describes the layout and where code goes, for people and AI agents alike. In short: logic in `core`, the app
in `composeApp` (shared screens, platform code behind the `Platform` interface), the two thin entry points in
`androidApp` and `iosApp`, and the app's identity in `gradle.properties`.

The machinery's version in `gradle/libs.versions.toml` brings Kotlin, the Android Gradle plugin, Compose
Multiplatform, the SDK levels, the test libraries and the static analysis; Dependabot moves it.

## Licence

MIT: see [LICENSE](LICENSE). Change it for your app if you like.
