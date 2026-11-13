# 2D physics games in Compose Multiplatform: rendering, driving, testing, verification and fit with this repository

State of things on 13 November 2026. Every finding carries one of these labels:

- **[repo]** read from this repository's files (path given).
- **[doc]** official documentation or release notes, fetched in this session.
- **[vendor]** a vendor's own claim, not independently measured.
- **[search]** seen only in a search-result summary, page not opened (403/429 or budget); treat as lower confidence.
- **[community]** forum, Slack, blog.
- **[recalled]** from my own prior knowledge of an API or document, URL given but not re-fetched in this session.
- **[estimate]** my own estimate, no source.

Access limits of this session: the machinery's plugins (which pin Kotlin, AGP, Compose Multiplatform, detekt, the
copy-paste check and Robolectric's settings) are not in this repository and the machinery's repository was not
readable from this session, so those versions and settings are inferred, not read. No Gradle build was run.

---

## 1. Game loop in Compose: frame clock, fixed timestep, recomposition, high refresh rates, lifecycle

### Takeaway
A frame-driven loop (`withFrameNanos` in a `LaunchedEffect`, fixed-timestep accumulator, state read only in the draw
phase) is the documented and commonly used way and needs nothing the template lacks. The loop must be refresh-rate
independent because this repository already opts iOS into 120 Hz, and Android phones run 60/90/120 Hz.

### Cited Findings
- **[doc]** `withFrameNanos` gives the lambda a frame time in nanoseconds from the coroutine context's
  `MonotonicFrameClock`; the time base is implementation-defined and values are strictly monotonically increasing; it
  throws if no frame clock is in the context. — [withFrameNanos reference](https://composables.com/jetpack-compose/androidx.compose.runtime/runtime/functions/withFrameNanos.md)
- **[community, 2021, possibly stale]** JetBrains' own Asteroids write-up for Compose Desktop uses exactly
  `LaunchedEffect { while (true) { withFrameNanos { game.update(it) } } }` as the game loop. —
  [dev.to/kotlin Asteroids](https://dev.to/kotlin/how-i-built-an-asteroids-game-using-jetpack-compose-for-desktop-309l)
- **[community, date unknown]** A Kotlin Slack thread reports that `withFrameNanos` returns inconsistent values when a
  loop is restarted, so deltas must be computed defensively (drop or clamp the first delta). —
  [Slack: withFrameNanos inconsistent values](https://slack-chats.kotlinlang.org/t/16035651/why-does-withframenanos-return-inconsistent-values-details-i)
- **[recalled]** The standard fixed-timestep accumulator with render interpolation and a clamp on the accumulated
  time ("spiral of death") is described in "Fix Your Timestep!" (2004, timeless). —
  [gafferongames.com](https://gafferongames.com/post/fix_your_timestep/)
- **[doc]** Official Compose guidance: "You should be suspicious if you are causing recomposition just to re-layout or
  redraw a Composable"; with a lambda-based modifier such as `drawBehind`, "the color state is only read during the
  draw phase. As a result, Compose can skip the composition and layout phases entirely — when the color changes,
  Compose goes straight to the draw phase"; "When you are passing frequently changing State variables into modifiers,
  you should use the lambda versions of the modifiers whenever possible." —
  [Compose performance best practices](https://developer.android.com/develop/ui/compose/performance/bestpractices)
- **[doc]** The `Canvas` composable is a wrapper around `Modifier.drawBehind`; `drawWithCache` keeps objects (paths,
  brushes) across draws until size or read state changes; `graphicsLayer` isolates content into its own layer. —
  [Compose graphics modifiers](https://developer.android.com/develop/ui/compose/graphics/draw/modifiers)
- **[doc]** Frame budgets: "render frames in under 16ms to achieve 60 fps ... 90 fps ... 11ms, and for 120 fps it's
  8ms"; a frame that overruns is dropped entirely by `Choreographer`. —
  [Slow rendering](https://developer.android.com/topic/performance/vitals/render)
- **[repo]** iOS is already opted into ProMotion: `CADisableMinimumFrameDurationOnPhone` is `true`, with the comment
  "Lets Compose draw at 120 Hz on ProMotion screens; Compose stops the app at launch without it." —
  [iosApp/App/Info.template.plist](../../iosApp/App/Info.template.plist)
- **[search]** Compose Multiplatform's changelog records the key being added to the template's Info.plist "to support
  120Hz", a warning when it is not configured, and "improved performance on 120 Hz devices"; Apple's forums confirm
  the key is required for a `CADisplayLink` above 60 Hz on iPhone. —
  [Apple forum thread](https://developer.apple.com/forums/thread/761616);
  [Compose changelog mirror](https://code.fanruan.com/github/compose-jb/blame/commit/393cfdd6638eee465b3c974fe9e6b3b0a1db57c1/CHANGELOG.md)
- **[doc]** Since Compose Multiplatform 1.11.0 (May 2026) iOS rendering is offloaded to a dedicated render thread by
  default ("concurrent rendering is enabled by default"; it was opt-in since 1.8.0). —
  [What's new in Compose Multiplatform 1.11](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)
- **[repo]** The template already depends on JetBrains' multiplatform `lifecycle-runtime-compose` 2.11.0 and uses
  `collectAsStateWithLifecycle` and a common `ViewModel` (`viewModel { HomeViewModel() }`), so lifecycle-aware pausing
  is available in `commonMain` without a new dependency. — [gradle/libs.versions.toml](../../gradle/libs.versions.toml);
  [HomeScreen.kt](../../composeApp/src/commonMain/kotlin/app/template/ui/home/HomeScreen.kt)
- **[doc]** Tests can drive frames deterministically: `TestMonotonicFrameClock` / the compose test rule's main clock
  supply the frame time. — [TestMonotonicFrameClock](https://developer.android.com/reference/kotlin/androidx/compose/ui/test/TestMonotonicFrameClock)

### Inferences
- Recommended shape for this repository (my design, not a cited pattern): the world is a plain mutable `core` object
  owned by the screen's view model; a `LaunchedEffect` loop calls `withFrameNanos`, adds the clamped delta to an
  accumulator, steps `core` at a fixed 1/60 s (or 1/120 s) zero or more times, then writes one small snapshot state
  (a frame counter or interpolation alpha) that only the `Canvas` draw lambda reads. Nothing recomposes per frame.
- Pushing an immutable world through `StateFlow` + `collectAsStateWithLifecycle` every frame (the template's pattern
  for the counter) would recompose and allocate at 60–120 Hz; the view model should expose draw-phase state instead.
  The rule "state and logic in the view model, looks in the composable" still holds.
- With a 60 Hz fixed step on a 120 Hz display, interpolation between the last two physics states is needed for smooth
  motion; without it objects move every second frame.
- Pause: stop stepping when the lifecycle leaves RESUMED (`LifecycleResumeEffect` or `repeatOnLifecycle`), and discard
  the first delta after resume.

### Gaps
- No official JetBrains or Google page describes a game loop as such; the pattern is assembled from the frame-clock
  API, the performance guidance and community write-ups.
- Whether the frame clock keeps ticking on iOS when the app is inactive but visible (e.g. Control Centre pulled down)
  was not found; a spike should log it.
- I found no current official kotlinlang page on iOS frame-rate configuration; the 120 Hz evidence is the repository's
  own plist, changelog entries and Apple's forum.

---

## 2. Performance of Compose Canvas for games

### Takeaway
There are no published sprites-per-frame numbers for Compose `Canvas` games on phones — not from Google, not from
JetBrains, not from shipped games. Everything points to "a few hundred simple shapes per frame is comfortable,
thousands need batching", but that sentence is my estimate and must be replaced by a spike.

### Cited Findings
- **[doc]** JetBrains maintains a multiplatform benchmark suite (Android, iOS, macOS, desktop, Kotlin/Wasm, JS) with
  `CanvasDrawing` ("massive amount of graphic shapes"), `VisualEffects` (animated snowflakes, stars, rocket
  particles), `HeavyShader`, `LazyGrid`, `TextLayout`, etc.; modes SIMPLE, VSYNC_EMULATION, and REAL (real VSync: FPS
  and actual missed frames); it publishes **no result numbers** on the page. —
  [compose-multiplatform/benchmarks/multiplatform](https://github.com/JetBrains/compose-multiplatform/tree/master/benchmarks/multiplatform)
- **[search, vendor, 2024, possibly stale]** For Compose Multiplatform 1.7.0 JetBrains reported the iOS `VisualEffects`
  benchmark "3.6 times as fast", average CPU time per 1000 frames from 8.8 s to 2.4 s (i.e. about 2.4 ms CPU per
  frame for that particle scene; device not stated in the summary). —
  [Kotlin Slack, CMP 1.7.0](https://slack-chats.kotlinlang.org/t/23079248/compose-multiplatform-as-compose-multiplatform-1-7-0-approac)
- **[vendor, May 2025]** JetBrains on 1.8.0 (iOS Stable): scrolling "on par with SwiftUI, even on
  high-refresh-rate devices". This is a UI claim, not a game-load claim. —
  [Compose Multiplatform 1.8.0 released](https://blog.jetbrains.com/kotlin/2025/05/compose-multiplatform-1-8-0-released)
- **[doc]** The common `androidx.compose.ui.graphics.Canvas` interface (marked Common) has `drawVertices`,
  `drawPoints`, `drawRawPoints`, `drawImage`, `drawImageRect`, `drawPath`, `drawCircle`, `drawRect`, `drawArc`,
  `drawLine`, `saveLayer`, `concat`, etc. There is **no `drawAtlas`** and no `drawBitmapMesh` in it; `nativeCanvas`
  returns "the native primitive that implements the Canvas interface". —
  [Canvas API reference](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/Canvas)
- **[doc]** Android's own rendering advice (applies to the hardware canvas Compose draws into): `drawPath` rasterises
  paths on the CPU and uploads them, so "if you have large paths, avoid editing them from frame to frame";
  "`drawPoints()`, `drawLines()`, and `drawRect/Circle/Oval/RoundRect()` are more efficient and better to use even if
  you use more draw calls"; `clipPath` "triggers expensive clipping behavior, and must generally be avoided"; the first
  display of a bitmap uploads it as a texture, which "can take several milliseconds"; allocation "in a tight loop
  that's called frequently" loads the GC. — [Slow rendering](https://developer.android.com/topic/performance/vitals/render)
- **[doc]** Shaders: on non-Android targets Compose's `Shader` was an alias of `org.jetbrains.skia.Shader` and since
  1.11 is a Compose wrapper (`SkShader.asComposeShader()`, `Shader.skiaShader`) — the common API is being decoupled
  from Skia, but no common runtime-shader API is announced. —
  [What's new in Compose Multiplatform 1.11](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)
- **[community]** Runtime shaders in Compose Multiplatform are done with `expect`/`actual`: AGSL `RuntimeShader` on
  Android (Android 13+), Skia `RuntimeEffect` (SkSL) on iOS/desktop/web; libraries exist that wrap this (ShaderX and
  others). — [ProAndroidDev: shaders in CMP](https://proandroiddev.com/how-to-implement-shaders-in-compose-multiplatform-android-ios-desktop-web-c86a36dd9666);
  [AGSL](https://developer.android.com/develop/ui/views/graphics/agsl)
- **[doc]** Skia was updated to Milestone 144 in 1.11 and Milestone 150 in 1.12. —
  [What's new 1.12](https://kotlinlang.org/docs/multiplatform/whats-new-compose-112.html)
- **[doc]** Kotlin/Native's default GC is "a concurrent mark and sweep (CMS) collector that does not separate the heap
  into generations"; marking runs concurrently, but "when allocation outpaces collection, the GC forces a
  stop-the-world phase"; the page gives no pause durations; pauses can be seen in Xcode Instruments with
  `kotlin.native.binary.enableSafepointSignposts=true`, and logged with `-Xruntime-logs=gc=info`. —
  [Kotlin/Native memory management](https://kotlinlang.org/docs/native-memory-manager.html)
- **[search, community, 2026]** A Compose-iOS Slack thread reports "a severe frame pacing issue on newer iPhones (e.g.
  iPhone 15 Pro, iOS 26.3)". The thread could not be opened (HTTP 429), so cause, version and fix are unknown. —
  [Kotlin Slack thread](https://slack-chats.kotlinlang.org/t/33111503/wave-hi-everyone-at-our-company-we-are-facing-a-severe-perfo)
- **[doc]** 1.12 moved lazy-list item deactivation out of the drawing phase on iOS for smoother scrolling — evidence
  that iOS frame smoothness is still being tuned release by release. —
  [What's new 1.12](https://kotlinlang.org/docs/multiplatform/whats-new-compose-112.html)
- **[community]** In a Kotlin Slack thread about drawing roughly a million circles per frame, a commenter doubted
  Compose would handle that many draw calls; structure-of-arrays storage was suggested for particles. No numbers. —
  [Kotlin Slack: particle sim](https://slack-chats.kotlinlang.org/t/32839008/i-m-trying-to-do-a-particle-sim-when-looks-like-particle-val)

### Inferences
- A non-generational collector on iOS means short-lived per-frame garbage (`Offset`, `Path`, lists, lambdas, boxed
  floats) is not cheap the way it is on ART; the simulation in `core` should use preallocated primitive arrays and
  reuse objects, and drawing should reuse `Path` objects (`rewind`) and cache `Brush`/`Paint` in `drawWithCache`.
  `Offset`, `Size` and `Color` are value classes and do not allocate when not boxed [recalled].
- Batching without `drawAtlas`, in common code: `drawPoints` for particles and `Canvas.drawVertices` (via
  `drawIntoCanvas`) with an image shader for many textured quads. Using `nativeCanvas` would split the code:
  `android.graphics.Canvas` on Android, `org.jetbrains.skia.Canvas` on iOS [recalled], which breaks the repository's
  "write it once in commonMain" rule unless hidden behind `expect`/`actual`.
- Runtime shaders are the one feature that is (a) platform-split, (b) Android 13+ only, and (c) unverified in
  Robolectric — a physics game does not need them and should avoid them.
- Text in the canvas (`drawText` with a `TextMeasurer`) is possible in common code, but scores and labels are better as
  ordinary `Text` composables layered over the canvas (see sections 6 and 10).
- [estimate] A physics puzzler with tens to low hundreds of bodies, drawn as circles, rounded rects, short polygons
  and a few images, is far below anything that should stress Skia on Metal or Android's hardware renderer. The risk is
  frame pacing and GC hitches on iOS, not raw throughput.

### Gaps
- **No measured numbers exist** (that I could find) for shapes or sprites per frame at 60/120 fps on a mid-range
  Android phone or an iPhone with Compose `Canvas`. A spike should measure, on one mid-range Android phone and one
  ProMotion iPhone, release builds: p50/p95/p99 frame time and missed frames for N = 100 / 500 / 2,000 / 10,000 of
  (filled circles, stroked polygons as `Path`, `drawImageRect` sprites, `drawPoints`, one `drawVertices` batch);
  allocation per frame; Kotlin/Native GC pauses via signposts; 60 Hz vs 120 Hz.
- Whether `drawVertices` with a texture is reliable on both Android's hardware canvas and iOS was not verified.
- No shipped commercial game built with Compose `Canvas` was found; the examples are samples, tutorials and hobby
  projects (Asteroids for desktop 2021; an Autodesk engineering article on Android+iOS games in Compose, page
  returned 403; a "3D game in Compose Multiplatform" article on ProAndroidDev, not opened).
- No KotlinConf talk with game frame-time data was found.

---

## 3. Compose Multiplatform in late 2026, and what this template uses

### Takeaway
Compose Multiplatform 1.12.2 (15 October 2026) is the current stable; Android, iOS and desktop are Stable, web
(Kotlin/Wasm) is still Beta. The template's visible library versions match the 1.12 line; its exact Kotlin, AGP and
Compose Multiplatform versions live in the machinery's plugins and could not be read.

### Cited Findings
- **[doc]** Latest stable: Compose Multiplatform 1.12.2, based on Jetpack Compose 1.12.1; the What's-new page is dated
  15 October 2026 and lists Lifecycle 2.11.0, Navigation 2.10.0-rc01, Skia Milestone 150. —
  [Compatibility and versioning](https://kotlinlang.org/docs/multiplatform/compose-compatibility-and-versioning.html);
  [What's new 1.12](https://kotlinlang.org/docs/multiplatform/whats-new-compose-112.html)
- **[search]** 1.12.0 was released on 26 August 2026; it adds an experimental MCP server in Compose Hot Reload that
  lets AI coding agents "trigger reloads, take screenshots, inspect the semantic tree, simulate clicks and input, and
  read application logs" (desktop target). — [InfoWorld](https://www.infoworld.com/article/4216661/compose-multiplatform-1-12-0-welcomes-coding-agents-with-mcp-server.html);
  [What's new 1.12](https://kotlinlang.org/docs/multiplatform/whats-new-compose-112.html)
- **[doc]** 1.11.0 (May 2026): concurrent rendering default on iOS, minimum iOS raised to 14, `iosX64` removed, v2
  `ComposeUiTest` APIs (StandardTestDispatcher) on non-Android targets, web scrolling/touch reworked, "latest features
  require an upgrade to Kotlin 2.3.10" for native/web. —
  [What's new 1.11](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)
- **[search, doc]** Stability levels: Android, iOS, desktop (JVM) Stable; web on Kotlin/Wasm **Beta** (Beta since
  1.9.0, September 2025). I found no announcement of a Stable web target. —
  [Stability of supported platforms](https://kotlinlang.org/docs/multiplatform/supported-platforms.html);
  [1.9.0: Compose for Web Beta](https://blog.jetbrains.com/kotlin/2025/09/compose-multiplatform-1-9-0-compose-for-web-beta/)
- **[vendor]** iOS became Stable with 1.8.0 (May 2025). —
  [1.8.0 released](https://blog.jetbrains.com/kotlin/2025/05/compose-multiplatform-1-8-0-released)
- **[repo]** Visible versions: machinery plugins 1.4.1 (CI workflow pinned at v1.5.5); JetBrains lifecycle 2.11.0 and
  navigation-compose 2.10.0; kotlinx-coroutines 1.11.0; kotlinx-io 0.9.2; Jetpack `compose-bom` 2026.09.00 (used for
  host tests and tooling); activity-compose 1.13.0; Gradle 9.8.2; JDK 25 (`.java-version`); iOS deployment target
  16.0; host tests run at `sdk = [35]`. "Kotlin, the Android Gradle plugin, Compose Multiplatform, the SDK levels, the
  test libraries and the static analysis come with the machinery's plugins". —
  [gradle/libs.versions.toml](../../gradle/libs.versions.toml);
  [gradle-wrapper.properties](../../gradle/wrapper/gradle-wrapper.properties);
  [iosApp/project.yml](../../iosApp/project.yml);
  [StoreArtTest.kt](../../composeApp/src/androidHostTest/kotlin/app/template/ui/StoreArtTest.kt);
  [.github/workflows/ci.yml](../../.github/workflows/ci.yml)
- **[repo]** Test stack visible from sources: Kotest `FunSpec` with `kotest-property` in `core/src/commonTest`;
  Robolectric + `androidx.compose.ui.test.junit4.v2.createComposeRule` in `composeApp/src/androidHostTest`; an
  emulator smoke test in `androidApp/src/androidTest`; an XCUITest launch test that attaches a screenshot. —
  [CounterTest.kt](../../core/src/commonTest/kotlin/app/template/core/CounterTest.kt);
  [SmokeTest.kt](../../androidApp/src/androidTest/kotlin/app/template/SmokeTest.kt);
  [LaunchTest.swift](../../iosApp/AppUITests/LaunchTest.swift)

### Inferences
- Lifecycle 2.11.0 + Navigation 2.10.0 are the versions listed for Compose Multiplatform 1.12.x, the `v2` compose test
  rule exists since the Compose 1.11 line, and the `androidHostTest` source-set name is the one used by AGP's
  Kotlin Multiplatform library plugin — so the template is very likely on Compose Multiplatform 1.12.x, Kotlin
  2.3.x or newer and a recent AGP. This is inference; the exact numbers are in the machinery.
- A web (Wasm) build of the same game is technically reachable later because `core` is common-only and the game would
  be drawn with common Compose, but the template shows no web target, the platform layer would need a third
  implementation, and the target is Beta.

### Gaps
- Exact Kotlin, AGP, Compose Multiplatform, Robolectric, detekt and ktlint versions: not visible here.
- Whether the machinery's plugins declare any target besides Android and iOS (a JVM target exists at least for `core`,
  since its tests run "on the JVM"): not visible.

---

## 4. Input: touch, sensors, haptics

### Takeaway
Touch (multi-touch, drag, tap) is fully covered by common `pointerInput`; tilt sensors and anything beyond a handful of
predefined haptic effects need a small platform interface, which is exactly what the template's `Platform` seam is for.

### Cited Findings
- **[repo]** "What only a platform can do sits behind an interface that `Platform` provides ... A platform that can't
  do something gets a null object (like `NoUpdates`), never an `if` on the platform in common code"; "sensors belong
  to `composeApp`'s platform layer". — [AGENTS.md](../../AGENTS.md);
  [Platform.kt](../../composeApp/src/commonMain/kotlin/app/template/Platform.kt)
- **[doc]** `HapticFeedbackType` and `HapticFeedback.performHapticFeedback` are in Compose's common API ("Constants to
  be used to perform haptic feedback effects"). —
  [androidx.compose.ui.hapticfeedback](https://developer.android.com/reference/kotlin/androidx/compose/ui/hapticfeedback/package-summary)
- **[doc]** 1.11 reworked touch processing on the web target; nothing in the 1.11/1.12 notes changes iOS or Android
  pointer input. — [What's new 1.11](https://kotlinlang.org/docs/multiplatform/whats-new-compose-111.html)
- **[community]** Third-party KMP haptics libraries exist (multihaptic, Jindong) for richer patterns. —
  [klibs.io: multihaptic](https://klibs.io/project/xfqwdsj/multihaptic);
  [klibs.io: jindong](https://klibs.io/project/compose-jindong/jindong)
- **[repo]** The iOS app is locked to portrait and full screen (`UISupportedInterfaceOrientations` = portrait only,
  `UIRequiresFullScreen`). — [Info.template.plist](../../iosApp/App/Info.template.plist)

### Inferences
- `Modifier.pointerInput` with `awaitEachGesture` / `detectDragGestures` gives per-pointer positions each frame in
  common code [recalled]; a slingshot/drag/aim mechanic needs nothing else. Input events should be queued and applied
  at the next fixed step so that recorded inputs replay exactly (section 7).
- `LocalHapticFeedback` offers only predefined effect types, with no control of amplitude, duration or pattern
  [recalled]; collision-strength haptics would need `interface Haptics` on `Platform` (Android `Vibrator` +
  `VIBRATE` permission declared in `composeApp`'s manifest; iOS `UIImpactFeedbackGenerator`), with a null object.
- Accelerometer: `interface Tilt { val gravity: StateFlow<...> }` on `Platform`, Android `SensorManager`, iOS
  `CMMotionManager`, null object for tests. A tilt game cannot be exercised by Robolectric or the simulator beyond the
  null object, so tilt should be an input to `core` like any other and be replaced by recorded values in tests.
- A landscape game needs the portrait-only plist (and the Android activity) changed.

### Gaps
- Which `HapticFeedbackType` values Compose Multiplatform implements on iOS, and with which UIKit generators: not
  found.
- No touch-to-photon latency measurement for Compose Multiplatform on iOS or Android was found; a spike could film a
  drag at 240 fps or compare against a native view.

---

## 5. Audio

### Takeaway
Compose Multiplatform has no audio API. The KMP audio libraries that exist in late 2026 are permissively licensed but
young (tens of GitHub stars, 0.x versions), and the low-latency one ships native code; a small own interface over
`SoundPool` and `AVAudioPlayer`/`AVAudioEngine` fits the template's rules best.

### Cited Findings
- **[doc]** basic-sound (LexiLabs): MIT; 0.2.6, about three months old at fetch; Android, JVM, iOS, macOS, watchOS,
  tvOS, Wasm, JS; URLs, local paths and Compose Resources (Android needs a `Context`); a `SoundBoard` preloads sounds
  for repeated playback; MP3 everywhere, other formats platform-dependent; 22 stars; underlying platform APIs not
  stated on the page. — [klibs.io: basic-sound](https://klibs.io/project/LexiLabs-App/basic-sound)
- **[doc]** Klarinet: Apache-2.0; 0.6.0 (days old at fetch), created about seven months ago, 14 stars; low-latency
  playback, recording, effects; Android backend is Google Oboe in C++17 with JNI, a shared Rust DSP core, iOS uses
  AVAudioEngine — i.e. it **ships native libraries**. — [klibs.io: Klarinet](https://klibs.io/project/vectencia/Klarinet)
- **[search]** Others: audio-stream-player-kmp (raw PCM streaming, AVAudioEngine on iOS), kmp-audio-recorder-player
  (recording/playback, last release about nine months old), KMedia, soundscape. None is positioned as a game
  sound-effects pool. — [klibs.io: audio-stream-player-kmp](https://klibs.io/project/adrianczuczka/audio-stream-player-kmp);
  [klibs.io: kmp-audio-recorder-player](https://klibs.io/project/hyochan/kmp-audio-recorder-player);
  [klibs.io: KMedia](https://klibs.io/project/moonggae/KMedia)
- **[repo]** Runtime dependencies must be Apache-2.0, MIT, BSD or CC0 (Licensee); "Each capability is its own small
  interface, used only by the code that needs it." — [AGENTS.md](../../AGENTS.md)

### Inferences
- [estimate] Own implementation: `fun interface Sounds { fun play(sound: Sound, volume: Float) }` (plus
  `music(track)`/`stop`) on `Platform`; Android with `android.media.SoundPool` (in the SDK, no dependency, built for
  short low-latency clips) and `MediaPlayer` for music; iOS with a small pool of `AVAudioPlayer` (or
  `AVAudioEngine` + player nodes) and `AVAudioSession` category *ambient* so the mute switch is respected; a `Silent`
  null object for tests and previews. Roughly 100–200 lines over both platforms, no new dependency, no licence
  question, no native library. Sound files go in `composeResources/files`.
- MIT and Apache-2.0 pass the licence rule, so basic-sound is admissible; its maturity (0.2.x, 22 stars) is the risk.
  Klarinet would bring NDK libraries back into the app (section 8).
- Audio cannot be verified by Robolectric or screenshots. What can be tested: `core` emits sound *events* (collision
  with impulse above a threshold, level complete) and a fake `Sounds` records them; that the phone actually plays
  them is a manual or device check.

### Gaps
- korlibs' audio (korau) status and licence in 2026 was not checked.
- Real latency of `SoundPool` on current mid-range phones and of `AVAudioPlayer` pools was not found; a spike should
  measure tap-to-sound roughly (video with audio track).
- Whether basic-sound uses `SoundPool` or `MediaPlayer` internally: not stated on its page.

---

## 6. Verification: can a game frame be rendered to an image on the JVM, and how would an agent "see" the game

### Takeaway
Yes, and the repository already does it: `StoreArtTest` composes real screens under Robolectric and writes PNGs. A
Compose `Canvas` game frame goes through the same path (real Skia, CPU pipeline), so shapes, paths, gradients, images,
blend modes and text render faithfully; only GPU-only effects are out. This is the decisive difference from the 3D
route, where the pixels were produced by a GL/Metal surface or a WebView that Robolectric does not run.

### Cited Findings
- **[repo]** `StoreArtTest` runs with `AndroidJUnit4` under Robolectric (`@Config(sdk = [35], qualifiers =
  "w360dp-h640dp-xxhdpi")`), sets content with `createComposeRule()` (v2), calls
  `compose.onNodeWithTag(ART).captureToImage().asAndroidBitmap()` and writes PNGs to `composeApp/build/store-art`
  (1080 × 1920 home screenshot, 1024 × 500 feature graphic); the pictures "must come out the same, pixel for pixel,
  until the app itself changes"; for the committed iPhone icon it tolerates a 1 % difference because "anti-aliasing
  differs a little between machines". It draws Material 3 text, backgrounds, vector drawables and bitmaps. No
  `@GraphicsMode` annotation or `robolectric.properties` is in the repository, so graphics mode is configured by the
  machinery. — [StoreArtTest.kt](../../composeApp/src/androidHostTest/kotlin/app/template/ui/StoreArtTest.kt)
- **[doc, March 2026]** Robolectric's native runtime compiles Android's graphics code with `-DHWUI_NULL_GPU`, which
  "disables GPU acceleration, uses Skia CPU pipeline only"; it bundles Skia, Minikin, HarfBuzz, FreeType and the
  PNG/JPEG/WebP codecs and fonts; `Path`, `PathMeasure`, `Bitmap`, `ImageDecoder`, `RenderNode`, `HardwareRenderer`,
  `RenderEffect`, shaders and `BlendModeColorFilter` are bound; `Readback` is a stub, there is no GPU cache, and
  **WebView support is absent**; API 35+ uses `libandroid_runtime` built from AOSP; supported hosts: Linux x86_64,
  macOS x86_64/aarch64, Windows x86_64. —
  [Robolectric native runtime report](https://utzcoz.github.io/2026/03/01/robolectric-native-runtime-report.html)
- **[search]** Known GPU-only hole: a Robolectric issue reports that `RenderEffect` blur "is a GPU-only effect, and
  does not have a CPU implementation" in native graphics, and the tile mode is forced to DECAL. —
  [Robolectric issue tracker (as returned by search; issue number not re-verified)](https://github.com/robolectric/robolectric/issues/8955)
- **[doc]** Roborazzi (Apache-2.0) needs Robolectric ≥ 4.10 with `@GraphicsMode(NATIVE)`; recommends
  `robolectric.pixelCopyRenderMode=hardware` "to enhance the accuracy" and for "drawing issues like shadows or shape
  graphics"; says native graphics "doesn't work properly on older SDK versions" (use API 28+); on differences between
  machines: "there are no guarantees for identical rendering across all environments", record and verify in the same
  CI environment; tolerances via `SimpleImageComparator`. — [Roborazzi README](https://github.com/takahirom/roborazzi/)
- **[doc]** Roborazzi features relevant to agents: `captureRoboGif()` "captures visually distinct states as a GIF";
  experimental `recordRoboVideo()` (GIF/APNG); a JSON UI-tree dump with node bounds and a numbered "Set-of-Mark"
  image; experimental AI image assertion; it "ships its documentation as an installable agent skill"; it supports
  Compose Multiplatform iOS (`roborazzi-compose-ios`) and desktop (`roborazzi-compose-desktop`), where desktop tests
  are "roughly 4–6x faster" than Robolectric ones and "goldens are per-platform". —
  [Roborazzi README](https://github.com/takahirom/roborazzi/)
- **[doc]** Robolectric's `PixelCopy` shadow historically draws via `View.draw` because "the real PixelCopy performs a
  full hardware capture of the screen ... which is impossible in Robolectric". —
  [ShadowPixelCopy javadoc 4.15](https://robolectric.org/javadoc/4.15/org/robolectric/shadows/ShadowPixelCopy.html)
- **[community, old threads, possibly stale]** Desktop's `ImageComposeScene` renders Compose to an image without a
  window ("supports this mode", no `java.awt.headless` needed); reports of CI trouble: a JVM SIGSEGV on CI,
  `SKIKO_RENDER_API=SOFTWARE_COMPAT` not affecting `ImageComposeScene`, a test hanging on `close()`, and default fonts
  differing per machine. — [Slack: ImageComposeScene screenshots](https://slack-chats.kotlinlang.org/t/2472911/i-use-imagecomposescene-with-this-code-to-take-screenshots-o?t=v2);
  [Slack: platform-specific results](https://slack-chats.kotlinlang.org/t/2488957/maybe-it-s-just-platform-specific-if-i-run-the-tests-on-maco)
- **[doc]** Compose Hot Reload's experimental MCP server (1.12) lets an agent take screenshots and simulate input in a
  running desktop Compose app. — [What's new 1.12](https://kotlinlang.org/docs/multiplatform/whats-new-compose-112.html)
- **[repo]** The emulator skill lets an agent launch the real app and take `adb exec-out screencap` screenshots; the
  iOS launch test attaches a simulator screenshot to the CI run. — [run-app skill](../../.claude/skills/run-app/SKILL.md);
  [LaunchTest.swift](../../iosApp/AppUITests/LaunchTest.swift)

### Inferences
- How an agent would see a game with what the repository already has (no new dependency):
  1. In `core`, build a world, feed it a recorded input list, step it to tick N. Deterministic.
  2. In an `androidHostTest` test next to `StoreArtTest`, compose the game's drawing composable with that world
     (the composable takes a world snapshot; no loop running), `captureToImage()`, write a PNG to `build/`.
  3. For motion, render ticks N, N+10, N+20 … side by side in one `Row` (a filmstrip) or as numbered PNGs; the agent
     reads the image. Overlays (velocity vectors, contact points, body ids) can be drawn in a debug mode.
  4. The store screenshots of the game come from the same test class, as the template intends.
- Golden-image tests are possible but new to this repository: today only the iPhone icon is a committed image, with a
  1 % tolerance. Goldens should be few (one per level's initial state is plenty), compared with a tolerance, and
  recorded on the CI's Linux runners. State assertions in `core` should carry correctness; images are for eyes and
  for catching drawing regressions.
- Contrast with the 3D route: there the frame came from a WebView (absent in Robolectric's native runtime) or a
  GL/Metal surface (GPU compiled out, `Readback` stubbed), so host tests could only prove the screen existed. With
  `Canvas`, the host test runs the very draw code that ships, through the same Skia, minus the GPU backend.
- What Robolectric will *not* show: GPU-only effects (blur `RenderEffect`), probably AGSL `RuntimeShader` fidelity
  (unverified), exact device anti-aliasing, performance, and anything iOS-specific (Skia-on-Metal differences, fonts).
  iOS drawing is covered only by the simulator launch test's screenshot unless more is added.
- Roborazzi would add GIF capture and nicer diff reports but is a new test dependency that the machinery (which owns
  the test libraries) would have to bring; `config/build-only-deps.txt` — which agents may not edit — would need its
  group added so its updates do not release the app. The repository's own approach is enough to start.
- `ImageComposeScene` is not a fit today: it needs a desktop (JVM) target in `composeApp`, which the template does not
  show, and a third platform implementation.

### Gaps
- Whether Robolectric's native graphics renders AGSL `RuntimeShader` correctly: no source found (natives are
  registered; fidelity unknown).
- Which graphics mode and `pixelCopyRenderMode` the machinery sets, and the Robolectric version: not visible. The
  fact that `captureToImage()` produces the store screenshots today proves the pipeline works for ordinary drawing.
- Whether Compose `Canvas` output under Robolectric is bit-identical between two Linux CI runs for anti-aliased
  paths and gradients was not measured; the repository's "pixel for pixel" expectation for store art suggests it is on
  the same runner image.
- A first spike should render one frame containing a path, a linear and a radial gradient, an image with rotation and
  alpha, two blend modes, a `saveLayer`, and text, and compare it by eye with the emulator.

---

## 7. Testing a physics simulation headlessly on the JVM

### Takeaway
A deterministic `core` simulation (fixed timestep, own seeded RNG, inputs as data) is testable in the template's
existing Kotest setup with example, property and replay tests, and bots can check every level for solvability. Box2D
itself is the precedent for determinism tests in CI; simulation-based solvability checking has academic precedent.

### Cited Findings
- **[repo]** `core` tests are Kotest `FunSpec` with property tests (`checkAll(Arb.int(0..1_000))`) in
  `commonTest`, run on the JVM and on iOS on a Mac; `core` is "common code only, never a `jvmMain` or `iosMain`". —
  [CounterTest.kt](../../core/src/commonTest/kotlin/app/template/core/CounterTest.kt); [AGENTS.md](../../AGENTS.md)
- **[search, 2024]** Box2D runs a determinism test ("Falling Hinges") in its unit tests on every pull request, on x64
  and ARM with MSVC, GCC and Clang; cross-platform determinism required disabling FMA contraction and custom
  trigonometric functions; the test compares final positions and rotations exactly. —
  [box2d.org: Determinism](https://box2d.org/posts/2024/08/determinism/)
- **[doc, June 2026]** Box2D's replay records one world snapshot followed by API calls; each step entry stores a hash
  of body transforms and velocities "which validates determinism"; recordings "work across platforms and regardless
  of worker count"; built so users can send files that reproduce bugs. — [box2d.org: Replay](https://box2d.org/posts/2026/06/replay/)
- **[search, 2013–2016, old but foundational]** Simulation-based playability checking for a physics puzzler: Ropossum
  for Cut the Rope uses an agent that proposes only sensible moves at each state so that depth-first search over the
  game's own physics simulation is tractable, and is used to test playability of generated levels; a configurable
  automated playtester was built for the Gamika casual-game engine. —
  [AAAI AIIDE 2013](https://ojs.aaai.org/index.php/AIIDE/article/view/12690);
  [Gamika, CIG 2016](https://www.kmjn.org/publications/GamikaLevelDesign_CIG16.pdf)
- **[recalled]** Kotlin's seeded `Random(seed)` is repeatable for a given seed, but the documentation reserves the
  right to change the algorithm in future Kotlin versions. —
  [kotlin.random.Random](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.random/-random.html)

### Inferences
- Test kinds that fit, all in `core/src/commonTest`:
  - *Invariants as properties*: no NaN or infinity in any body after any step; bodies stay inside the world bounds;
    no tunnelling (a fast body never ends on the far side of a static wall — generate speeds up to the design
    maximum); energy never increases without input beyond a tolerance; resting stacks come to rest within T ticks.
  - *Determinism*: the same level + seed + inputs gives the same state hash at every tick, twice in a row.
  - *Replay/golden state*: a recorded input list ends in a known state hash; a failing property test prints
    seed + inputs so the case can be replayed and drawn (section 6).
  - *Solvability*: for each level, a stored solution (input list) must reach the goal; and/or a bot (random search,
    grid search over aim angle and power, or beam search) must find a solution within a budget. The same bot gives a
    difficulty estimate (fraction of random shots that win).
- Determinism only needs to hold per platform for this purpose. JVM and Kotlin/Native (arm64) may differ in the last
  bits (FMA contraction, `sin`/`cos` implementations) — the Box2D post shows what bit-exact cross-platform equality
  costs. Consequence: state hashes checked in tests must be per-platform or the hash tests JVM-only by tolerance;
  solvability should be asserted with a margin (the stored solution also wins when perturbed slightly).
- Use an own small PRNG in `core` (xorshift or PCG, a dozen lines) rather than `kotlin.random.Random(seed)` if seeds
  are stored in levels or replays, so a Kotlin update cannot change them.
- [estimate] Throughput: a pure-Kotlin step for tens of bodies should take microseconds on the JVM, so a 30-second
  run (1,800 steps at 60 Hz) should take a few milliseconds and hundreds to thousands of runs per second per core are
  plausible. Unmeasured; the spike should report steps per second on the CI runner, and the solvability bots need a
  time budget so `./gradlew check` stays "a few minutes".
- Kotest property tests default to many iterations; the iOS simulator run of the same `commonTest` on CI Macs is
  slower, so heavy bot searches may need a smaller budget there (without disabling the tests — rule 4).

### Gaps
- No public description of replay-based regression testing inside a specific commercial 2D mobile game was fetched in
  this session; the practice is widely described but I cite only Box2D's own replay.
- The Angry Birds AI competition and similar solver work were not checked.
- No measured JVM throughput for a pure-Kotlin 2D physics step was found.

---

## 8. CI implications

### Takeaway
A pure-Kotlin engine in `core` drawn with common Compose adds no native library, no NDK and no new CI job: the
existing `check`, emulator smoke test and labelled iOS build cover it functionally. What the template has no answer
for is frame-time measurement on real hardware; that needs either a device lab (Firebase Test Lab's game-loop mode
fits well) or a manual check.

### Cited Findings
- **[doc]** 16 KB pages: "If your app only uses code written in the Java programming language or in Kotlin, including
  all libraries or SDKs, then your app already supports 16 KB devices"; apps with NDK libraries must be rebuilt;
  "Starting February 1, 2027, if your app updates don't support 16 KB memory page sizes, you won't be able to release
  these updates" (apps targeting Android 15+). —
  [Support 16 KB page sizes](https://developer.android.com/guide/practices/page-sizes)
- **[repo]** CI is one reusable machinery workflow: checks and Android build on every pull request and push, the iOS
  build "where it is worth it (pushes to main that touch code, pull requests labelled `ios`, runs started by hand)";
  agents must not edit `.github/`. `./gradlew check` runs "tests, detekt, ktlint, copy-paste check, Android Lint,
  licences, translations" and "takes a few minutes". An emulator smoke test and an XCUITest launch test exist. —
  [ci.yml](../../.github/workflows/ci.yml); [AGENTS.md](../../AGENTS.md);
  [SmokeTest.kt](../../androidApp/src/androidTest/kotlin/app/template/SmokeTest.kt)
- **[doc]** Firebase Test Lab Game Loop: the app declares an intent filter for
  `com.google.intent.action.TEST_LOOP`, reads an integer `scenario` extra, runs its own scripted or AI play, writes a
  result log (JSON with `frame_stats` is shown in the console) and calls `finish()`; runs on physical devices
  (virtual devices "aren't recommended because their frame rates are lower"); no game engine is required. —
  [Test Lab: Game Loop](https://firebase.google.com/docs/test-lab/android/game-loop)
- **[doc, search]** Macrobenchmark's `FrameTimingMetric` reports `frameDurationCpuMs` and `frameOverrunMs`
  (API 31+; positive = missed deadline); JankStats is an in-app library reporting per-frame jank. Practitioners
  advise gating on high percentiles and treating emulator numbers as relative only. —
  [Macrobenchmark metrics](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-metrics);
  [FrameTimingMetric](https://developer.android.com/reference/kotlin/androidx/benchmark/macro/FrameTimingMetric)
- **[doc]** Android vitals for games: a slow session is one in which more than 25 % of frames are slow; a frame is
  slow if it is not presented within 50 ms of the previous one (20 fps); a second metric uses 34 ms (30 fps);
  monitoring starts after the game has run for one minute; Google says Play "will start steering users away from
  games that cannot achieve 20 FPS on their phones". —
  [Slow sessions](https://developer.android.com/games/optimize/vitals/slow-session)
- **[doc]** For games that bypass `Choreographer` Google points to the Frame Pacing library; a Compose game does not
  bypass it. — [Slow rendering](https://developer.android.com/topic/performance/vitals/render)
- **[doc]** Kotlin/Native GC pauses can be recorded as signposts for Instruments. —
  [Kotlin/Native memory management](https://kotlinlang.org/docs/native-memory-manager.html)

### Inferences
- A native Box2D binding (JNI on Android, cinterop on iOS) would bring `.so` files (16 KB alignment, NDK or prebuilt
  binaries per ABI), a second build path for iOS, a licence check of the binding, and — decisive here — a simulation
  that is not in `core` as common Kotlin and therefore cannot be tested the way the repository requires ("common code
  only"). The pure-Kotlin route keeps all of that out of scope.
- "No new native libraries" is the accurate wording: Compose itself may already bring small Google-built native
  libraries on Android [recalled, unverified here — a build would show `lib/` in the APK]; they are Google's to keep
  aligned.
- The thresholds in Android vitals (20/30 fps) are far below a 60 fps target; passing them says little. They matter
  only as the store's floor, and as a reminder that the game should be categorised as a game only if the owner wants
  the game metrics.
- Options for frame-time evidence, cheapest first: (1) an in-app debug overlay / log of frame deltas from the game
  loop itself, read by an agent on the emulator through the existing run-app skill (relative numbers only);
  (2) JankStats in debug builds (Apache-2.0 [recalled], runtime dependency: owner's call); (3) Firebase Test Lab game
  loop on two or three physical phones running a bot-played level and writing frame stats — needs a Firebase project,
  secrets and a workflow step, all owner/machinery work; (4) Macrobenchmark needs its own module and plugin, also
  machinery work; (5) iOS: Instruments or XCTest performance metrics on a real iPhone — not available on CI
  simulators in a meaningful way.
- Kotlin/Native: more code in `core` and `composeApp` lengthens the iOS compile and link, which CI runs only on
  labelled pull requests and on main, so day-to-day feedback stays on the JVM. `./gradlew
  compileKotlinIosSimulatorArm64` remains the local check for shared code.

### Gaps
- Kotlin/Native compile-time numbers for a project of this size in 2026 were not found; not measured here.
- iOS frame metrics (XCTest hitch metrics, MetricKit) were not researched in this session.
- Whether the machinery's emulator job could run a longer scripted game session, and its cost in minutes, is not
  visible.

---

## 9. App size and startup

### Takeaway
A pure-Compose game adds only Kotlin code and assets to an app that already pays for Compose (and, on iOS, for Skia);
there is no engine runtime to ship. Hard comparative numbers against WebView or native engines for 2026 were not
found.

### Cited Findings
- **[vendor, May 2025]** JetBrains: Compose Multiplatform "adds only ~9 MB to the size of an iOS app compared to a
  fully native SwiftUI app with the same UI logic and assets". —
  [1.8.0 released](https://blog.jetbrains.com/kotlin/2025/05/compose-multiplatform-1-8-0-released)
- **[measured by a third party, 2023, stale]** IPA sizes with App Thinning: native 1.7 MB, KMP + Compose 24.8 MB
  (CMP 1.4) and 32.2 MB (1.5 alpha), Flutter 17.9 MB; Skia accounts for most of the Compose figure. —
  [flutter-vs-native-vs-kmp](https://github.com/jacobras/flutter-vs-native-vs-kmp)
- **[doc]** The JetBrains benchmark suite has a STARTUP mode (time to main, to first frame, longest early frames) but
  publishes no numbers. — [benchmarks/multiplatform](https://github.com/JetBrains/compose-multiplatform/tree/master/benchmarks/multiplatform)

### Inferences
- In this template the Compose/Skia cost is already paid by the counter app; the game's marginal size is its
  compiled Kotlin (tens to a few hundred KB [estimate]) plus images and sounds. A WebView game adds its web bundle
  and a WebView start (process and page load) on top of the same app; a native engine adds megabytes of runtime and,
  on Android, `.so` files per ABI.
- Startup: no engine initialisation or WebView warm-up; the game screen is another composable. First-frame costs to
  watch are image decoding and first texture uploads (preload before the level starts).

### Gaps
- No 2026 measurement of Compose Multiplatform's iOS size overhead, and no measured startup comparison between a
  Compose screen, a WebView game and a native engine, was found. A spike can read the IPA/AAB size before and after.

---

## 10. Fit problems specific to this repository

### Takeaway
The architecture fits unusually well (pure logic in a tested common module, a platform seam with null objects,
screenshot-by-test already in place, natural-break ads already modelled). The friction is in the quality gates —
detekt's defaults against numeric, branchy code, the copy-paste check against look-alike levels — and in two things a
canvas game does not get for free: translated in-game text and accessibility.

### Cited Findings
- **[repo]** `core`: "pure Kotlin logic, the same on every platform ... No UI, no platform APIs: common code only";
  "Logic that isn't about a screen goes to `core`, with tests"; screens are "`ui/<screen>/`: a `…Screen` composable
  and its `…ViewModel`". — [AGENTS.md](../../AGENTS.md)
- **[repo]** "Never weaken quality gates: don't disable or relax tests, detekt, ktlint, Android Lint, Licensee or the
  translation check, and don't lower their thresholds"; agents must not edit `config/`, `.github/`, signing or the
  store listing's texts. — [AGENTS.md](../../AGENTS.md)
- **[repo]** How existing code lives with detekt: `StoreArtTest` moves every bare number into named constants in a
  `private companion object` (`ICON_PX`, `BYTE`, `ADAPTIVE_LAYER`, `MAX_BYTES` …), while `24.dp`-style literals stay
  inline in composables; the only suppressions in the code are two narrow, commented ones in `iosMain`
  (`@Suppress("FunctionName", "unused")`, `@Suppress("UNCHECKED_CAST")`). ktlint: `ktlint_official`, 120 columns,
  composables exempt from function naming. — [StoreArtTest.kt](../../composeApp/src/androidHostTest/kotlin/app/template/ui/StoreArtTest.kt);
  [MainViewController.kt](../../composeApp/src/iosMain/kotlin/app/template/MainViewController.kt);
  [.editorconfig](../../.editorconfig)
- **[doc]** detekt defaults (page for 2.0.0-alpha.6): `CyclomaticComplexMethod` 14, `LongMethod` 60 lines,
  `LongParameterList` 5 function / 6 constructor parameters, `LargeClass` 600 lines, `TooManyFunctions` 11 per
  file/class, `NestedBlockDepth` 4, `ComplexCondition` 3; `CognitiveComplexMethod` off by default. —
  [detekt complexity rules](https://detekt.dev/docs/rules/complexity/)
- **[repo]** Texts: "Every text people see goes in `strings.xml`, in every language the app has (`values-*`): the
  build fails on a missing text, plural form or argument"; the template has English and Italian; texts are read with
  `stringResource`/`pluralStringResource` from `composeResources`. — [AGENTS.md](../../AGENTS.md);
  [values/strings.xml](../../composeApp/src/commonMain/composeResources/values/strings.xml);
  [HomeScreen.kt](../../composeApp/src/commonMain/kotlin/app/template/ui/home/HomeScreen.kt)
- **[repo]** Both smoke tests find the UI through accessibility semantics: Android `onNodeWithText("Count")`, iOS
  "Compose names its texts and buttons by their text for the accessibility tree that UI tests read". —
  [SmokeTest.kt](../../androidApp/src/androidTest/kotlin/app/template/SmokeTest.kt);
  [LaunchTest.swift](../../iosApp/AppUITests/LaunchTest.swift)
- **[doc, community]** A `Canvas` is invisible to accessibility services unless semantics are added; the official
  guidance is a `semantics { contentDescription = … }` (and custom actions) on the canvas. —
  [Key steps to improve Compose accessibility](https://developer.android.com/develop/ui/compose/accessibility/key-steps);
  [ProAndroidDev: accessible graphs](https://proandroiddev.com/more-accessible-graphs-with-jetpack-compose-part-1-adding-content-description-29567d6e2724)
- **[repo]** Monetization seams a game can use as they are: `Ads.show(AdPlacement.NATURAL_BREAK)` ("At a natural
  break, never in the middle of what the person is doing"), `Unlocked(entitlements, feature) { … }`,
  `Feature.NO_ADS`; the app is `Free` until the owner decides. `AppContainer` is manual dependency injection built
  from `Platform`. — [Monetization.kt](../../composeApp/src/commonMain/kotlin/app/template/monetization/Monetization.kt);
  [AppContainer.kt](../../composeApp/src/commonMain/kotlin/app/template/AppContainer.kt)
- **[repo]** The serialization plugin is applied to `composeApp` only; `core` applies only `machinery.kmp-library`. —
  [composeApp/build.gradle.kts](../../composeApp/build.gradle.kts); [core/build.gradle.kts](../../core/build.gradle.kts)

### Inferences
- **detekt vs game code.** The pattern in `StoreArtTest` shows a magic-number rule is in force (numbers hoisted into
  constants, `.dp` literals tolerated). Physics and drawing code is full of constants (gravity, restitution, radii,
  colours, tuning) and of long, branchy functions (collision narrow phase, solver loop, a `draw` function with a
  `when` per shape). To pass without touching the gates: tuning values in named `const val`s or a `Tuning`/`Material`
  data class; one small function per shape pair and per draw kind; parameter objects instead of 6+ parameters; split
  files to stay under 11 functions. This is how good engine code looks anyway, but agents will hit these rules on
  nearly every first attempt, and the fix must never be a suppression or a config change.
- **Copy-paste check vs levels.** Levels written as Kotlin constructor calls look alike token-wise and may trip a
  token-based duplicate detector; levels as near-identical test cases will too. Mitigation without weakening the
  gate: a tiny level DSL or a compact text format (e.g. an ASCII map or one line per body) parsed by `core`, so each
  level is data rather than repeated code, and table-driven tests that loop over all levels. The check's tool and
  threshold are in the machinery and not visible, so a spike should add five levels and run `./gradlew check`.
- **Where levels live.** `core` cannot read `composeResources` (they belong to `composeApp`), and tests in `core`
  must be able to load every level to prove it solvable. So levels belong in `core` as Kotlin data (strings or DSL),
  not as JSON in `composeResources`. JSON would also need the serialization plugin in `core`, which is a machinery
  plugin choice per module (the plugin exists in the catalog, so it is possible, but unnecessary).
- **In-game text.** Score, level names, "Paused", "Try again", tutorials: all through `strings.xml` in English and
  Italian. `stringResource` is composable-only, so text should be resolved in composition and either shown as `Text`
  composables in a HUD layered over the canvas (preferred: free semantics, free translation check, free font
  scaling) or passed into the draw lambda for `drawText`. Level *names* cannot live in `core` as display strings;
  `core` should expose ids and `composeApp` map them to string resources. Text baked into images is ruled out.
- **Accessibility and lint.** A real-time physics game cannot be made fully playable with TalkBack, but menus, HUD,
  pause and results can and should be ordinary composables; the canvas gets a `contentDescription` from
  `strings.xml` describing the game state. Android Lint does not look inside `DrawScope`, so the gate is unlikely to
  object [estimate]; the risk is the opposite — nothing forces the semantics to exist. The smoke tests need at
  least one text or tagged node on the first screen, which a canvas-only first screen would not give them.
- **Screen and view model.** `ui/game/GameScreen` + `GameViewModel` fits; the view model owns the `core` world, the
  loop state and pause; `App.kt` gets a route. Sounds, haptics and tilt arrive through `AppContainer` from `Platform`,
  each as its own small interface with a null object, which also makes `StoreArtTest`-style rendering trivial (the
  test already passes `NoUpdates()`).
- **Ads.** "Level complete" and "game over" are exactly the `NATURAL_BREAK` placement; nothing to add.
- **Portrait lock** (section 4) and the **`config/` rule** (a new test-only library's updates would release the app
  unless the owner lists it in `config/build-only-deps.txt`) are the two places where an agent would have to open an
  issue for the owner instead of acting.

### Gaps
- The machinery's actual detekt configuration (whether defaults are tightened, whether `MagicNumber` ignores named
  arguments or property declarations), the copy-paste tool and its token threshold, and the Android Lint settings
  are not visible. The inferences above rest on detekt's defaults and on how the existing code is written.
- Whether detekt and the copy-paste check also run on test source sets (where long table-driven physics tests would
  live) is not visible; `StoreArtTest`'s constants suggest tests are checked too.
