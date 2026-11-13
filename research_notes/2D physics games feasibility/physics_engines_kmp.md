# 2D physics engine options for a Kotlin Multiplatform app (Android + iOS), state of November 2026

Research date: 2026-11-13. Conventions used below:

- **[measured]** = I measured it myself on 2026-11-13 from a shallow clone of the linked repository, or from the
  artifact downloaded from Maven Central / npm (line counts are raw `wc`-style counts including comments and blanks;
  dates are the committer date of the tag or of HEAD).
- **[estimate]** = my own effort estimate for one experienced KMP engineer; not a sourced fact.
- **STALE** = last release older than 2025.
- "Template rule" = runtime dependencies must be Apache-2.0, MIT, BSD or CC0 (Licensee), `core` is common Kotlin only
  and is tested on the JVM (AGENTS.md of the template).
- GitHub star counts come from the repository pages as fetched on 2026-11-13.

## Objective: every realistic route to 2D rigid-body physics in this template, compared

### Takeaway
There is no maintained, permissively licensed, pure-Kotlin common-code rigid-body engine on Maven Central in
November 2026; the realistic routes are (a) the young MIT binding **Boks2D** to native Box2D v3.1.1 (works on
Android/iOS/JVM today, but its published Android `.so` files are 4 KB-aligned, so they fail Google Play's 16 KB rule
as shipped), (b) the stale pure-Kotlin **kbox2d 3.3.0** (JBox2D port, 2022, still resolvable for iOS/JVM/Android),
(c) vendoring a small MIT/BSD Kotlin/Java engine into `core` (KPhysics ≈3.1k lines, or the 28.8k-line JBox2D port),
or (d) writing game-specific physics. Full engines (KorGE, Godot, libGDX, Defold, LÖVE, Flame, Phaser) replace the
Compose Multiplatform app instead of joining it; the one Compose-native engine with a physics plugin (Kubriko) is
MPL-2.0 and therefore excluded by the template rule.

### Cited Findings

Route table (facts are sourced in the per-question sections below; effort figures are **[estimate]**):

| Route | Licence (verified where) | Latest release | Android + iOS? | JVM tests of `core`? | Native code? | Size | Determinism | Integration effort **[estimate]** |
|---|---|---|---|---|---|---|---|---|
| A. Boks2D → Box2D v3.1.1 | MIT (LICENSE + POM) | 0.1.1, 2026-03-17 | Yes (androidTarget, iosArm64, iosSimulatorArm64, iosX64) | Yes: `jvm` target ships natives for Linux x64, macOS arm64/x64, Windows x64 | Yes (JNI `.so`, static `libbox2d.a` via cinterop) | arm64 `.so` 3.0 MB unstripped; x86_64 `.so` 0.6 MB stripped **[measured]** | Box2D's own cross-platform determinism on 64-bit (if compiler flags are kept) | 1–3 days to integrate; plus 2–5 days to fork/rebuild with 16 KB alignment unless upstream fixes it |
| B. Own cinterop + JNI binding to Box2D v3.2 | MIT (Box2D) | Box2D v3.2.0, 2026-10-18 | Yes, if built for both | Yes, if a host native lib is built | Yes | Same order as A | Same as A | 2–3 weeks for a game-sized API subset incl. CMake builds and CI; ongoing upkeep |
| C. kbox2d 3.3.0 from Maven Central (pure Kotlin JBox2D port) | MIT in POM; underlying JBox2D is BSD-2 | 3.3.0, 2022-10-22 (**STALE**) | Yes (klibs for android, jvm, iosArm64, iosSimulatorArm64, iosX64) | Yes | No | jvm jar 597 KB; iosArm64 klib 1.06 MB **[measured]** | Not claimed; same-binary replay only | 1–2 days; risk: abandoned binary, transitive `korma:3.3.0` |
| D. Vendor the JBox2D Kotlin port (korge-box2d sources) into `core` | BSD-2 (JBox2D licence file in repo) | repo tag v0.2.0, 2025-05-16; not on Maven Central | Yes (common code) | Yes | No | 28.8k lines Kotlin **[measured]** | As C | 1–2 weeks to detach from `korlibs.math`/KorGE views and make it pass detekt/ktlint |
| E. Vendor KPhysics into `core` | MIT (LICENSE) | none on Maven Central; last commit 2022-03-17 (**STALE**) | Yes (plain Kotlin, no `java.*` imports found) | Yes | No | 3.1k lines Kotlin **[measured]** | Not claimed | 2–4 days; limited: discrete collision only, two primitive joint types |
| F. Write a minimal general engine in `core` | own | – | Yes | Yes | No | ~1.3k lines (Box2D-Lite scale) to ~3k lines (KPhysics scale) | Under own control | 1–2 weeks to "boxes and circles stack"; several more weeks for CCD, robust joints, sleeping |
| G. Game-specific physics (verlet ropes/ragdolls, circle-only, analytic pinball) | own | – | Yes | Yes | No | hundreds of lines | Under own control | days per game |
| H. JS engine in a WebView (Planck.js, Matter.js, Rapier WASM, Phaser) | MIT / Apache-2.0 | see JS section | Yes (WebView) | No (not Kotlin) | No (WASM for Rapier/box2d3-wasm) | 26 KB–906 KB gzipped **[measured]** | engine-specific | Replaces the Kotlin game layer |
| I. Full game engine (KorGE, Godot, libGDX, Defold, LÖVE, Flame) | mostly MIT/Apache/zlib; Defold custom | see engines section | Yes | n/a | varies | varies | varies | Replaces the template |
| X. Kubriko `plugin-physics` (Compose-based engine) | **MPL-2.0 — excluded** | 0.8.0, 2026-10-28 | Yes | Yes | No | 2.7k lines | – | excluded by licence |

- The template's own rules that shape the ranking: `core` is "pure Kotlin logic … No UI, no platform APIs: common code only, never a `jvmMain` or `iosMain` of its own", tested on the JVM; runtime dependencies "must be Apache-2.0, MIT, BSD or CC0 (Licensee checks)" — [AGENTS.md of the template](https://github.com/) (file read locally at `/home/user/app-template/AGENTS.md`).

### Inferences
- Ranking input: routes C–G keep `core` pure and JVM-testable with zero native build machinery; routes A/B give the
  best engine (Box2D v3: soft-step solver, CCD, cross-platform determinism) but add `.so`/static-lib supply-chain and
  the 16 KB page-size obligation; routes H/I abandon "one Kotlin codebase, Compose for every screen".
- Boks2D is a KMP dependency usable from `commonMain`, so `core` could formally stay "common code only", but it would
  no longer be "pure Kotlin": whether that is acceptable is an owner decision, not a technical one.
- For the game classes listed (stacking, merge/drop, ragdoll, rope, pinball, marble, bridge, slingshot) a Box2D-class
  engine covers all of them; a KPhysics-class engine (no CCD, no revolute joints/motors/sensors) covers stacking,
  merge/drop and simple slingshot but not ragdolls, pinball flippers or bridge-building without extension.

### Gaps
- No phone-class (Android ARM) benchmark of any of these engines was found; all performance figures are desktop/M2.
- I did not build or run any of the libraries; "works on iOS" for Boks2D and kbox2d rests on published target
  artifacts and on Boks2D's own benchmark configuration, not on my own test.

## Pure-Kotlin / common-code engines that could live in `core`

### Takeaway
The only pure-Kotlin Box2D on Maven Central with iOS klibs is `com.soywiz.korlibs.kbox2d:kbox2d:3.3.0` (a JBox2D port,
released 2022-10-22 with Kotlin 1.7.20, unmaintained as a published artifact); its source successor `korlibs/korge-box2d`
is a KorGE source-dependency, not a Maven artifact. Everything else is either JVM-only (JBox2D, dyn4j, PhysicsBox),
tiny and stale (KPhysics), alpha (Kanvas), or MPL-2.0 (Kubriko).

### Cited Findings

**kbox2d (korlibs) — Maven artifact**
- `com.soywiz.korlibs.kbox2d:kbox2d` latest version 3.3.0, published 2022-10-22, description "Port of JBox2d", built with Kotlin 1.7.20; targets Android, JVM, iOS (ios_arm32, ios_arm64, ios_simulator_arm64, ios_x64), macOS, Linux x64, mingw x64, JS; licence shown as MIT — [klibs.io package page](https://klibs.io/package/com.soywiz.korlibs.kbox2d/kbox2d). **STALE (2022).**
- Maven metadata lists versions 3.1.0, 3.2.0, 3.3.0, last updated 2022-10-22; the POM declares "MIT License" and a runtime dependency on `com.soywiz.korlibs.korma:korma:3.3.0` **[measured]** — [Maven Central directory](https://repo1.maven.org/maven2/com/soywiz/korlibs/kbox2d/kbox2d/).
- Artifact sizes: `kbox2d-jvm-3.3.0.jar` 596,959 bytes; `kbox2d-iosarm64-3.3.0.klib` 1,064,218 bytes **[measured]** — [Maven Central directory](https://repo1.maven.org/maven2/com/soywiz/korlibs/kbox2d/).
- The `korlibs/kbox2d` GitHub repository now contains only a README saying "Moved to https://github.com/korlibs/korge/tree/main/kbox2d"; last commit 2022-07-08 **[measured]** — [korlibs/kbox2d](https://github.com/korlibs/kbox2d). The current `korlibs/korge` main branch has no `kbox2d` directory at top level (top-level modules: korge, korge-core, korge-gradle-plugin, korge-ipc, korge-reload-agent, korge-sandbox, korge-benchmarks) **[measured]** — [korlibs/korge](https://github.com/korlibs/korge).
- klibs.io lists the parent project under "soywiz-archive/kbox2d" with 17 stars — [klibs.io package page](https://klibs.io/package/com.soywiz.korlibs.kbox2d/kbox2d).
- Evidence that the 2022 klib still resolves with a current compiler: Boks2D's benchmark module (Kotlin 2.2.20, targets `jvm`, `iosX64`, `iosArm64`, `iosSimulatorArm64`) declares `implementation("com.soywiz.korlibs.kbox2d:kbox2d:3.3.0")` in common code and publishes an iOS benchmark plot comparing against it **[measured from `benchmark/build.gradle.kts` and `benchmark/gradle/libs.versions.toml`]** — [joaomcl/Boks2d](https://github.com/joaomcl/Boks2d).
- The current korlibs monorepo publishes under `org.korge` (e.g. `org.korge:korlibs-math:6.1.0`, 2026-04-20) and its module list does not mention kbox2d or a Box2D port — [korlibs/korlibs](https://github.com/korlibs/korlibs); [Maven Central metadata](https://repo1.maven.org/maven2/org/korge/korlibs-math/maven-metadata.xml) **[measured]**.

**korge-box2d (korlibs) — source successor**
- "Box2D Physics implementation for KorGE Game Engine"; 3 stars, 23 commits; used as a kproject source dependency (`https://github.com/korlibs/korge-box2d/tree/0.0.1/korge-box2d`), no Maven coordinates given — [korlibs/korge-box2d](https://github.com/korlibs/korge-box2d).
- Last commit 2025-05-16, latest tag v0.2.0 (same date); the only licence file is `korge-box2d/LICENSE`, which is the JBox2D licence ("Copyright (c) 2014, Daniel Murphy", BSD 2-clause text); 144 files / 28,788 lines of Kotlin in `commonMain`, packages `org.jbox2d.*` plus two KorGE glue files (`korlibs/korge/box2d/WorldView.kt`, `KtreeSerializer.kt`); imports of non-stdlib packages: `korlibs.math.geom` (24 files), `korlibs.korge.view` (4), `korlibs.time`, `korlibs.io`, `korlibs.datastructure`; `kproject.yml` depends on `com.soywiz.korlibs.korge2:korge`; the sample build enables `targetJvm()`, `targetJs()`, `targetAndroid()` and has `//targetIos()` commented out; plugin version `com.soywiz.korge 6.0.0-beta4` **[measured]** — [korlibs/korge-box2d](https://github.com/korlibs/korge-box2d).
- Includes JBox2D's particle callbacks (`ParticleDestructionListener`, `ParticleQueryCallback`, `ParticleRaycastCallback`), i.e. it derives from the JBox2D 2.3 line that carries LiquidFun-style particles, plus `TimeOfImpact.kt` (continuous collision), ray-cast and contact-listener callbacks **[measured: file list]** — [korlibs/korge-box2d](https://github.com/korlibs/korge-box2d).
- KorGE's docs still say "KorGE provides a set of hit-testing and collision detection utilities and a port of Box2D out of the box", enabled with `supportBox2d()` in the `korge {}` block, API `worldView { createBody { }.fixture { shape = BoxShape(...) } }`; the page targets 6.x and carries the warning "Document not reviewed yet, might be outdated" — [docs.korge.org/physics](https://docs.korge.org/physics/).

**JBox2D (Java)**
- Licence: BSD 2-clause ("Copyright (c) 2014, Daniel Murphy … Redistribution and use in source and binary forms…"); tags end at `jbox2d-2.2.1.1` (2013-04-09) and `jbox2d-2.3.0-BETA`; last commit 2024-09-19; library module 139 files / 29,327 lines of Java **[measured]** — [jbox2d/jbox2d](https://github.com/jbox2d/jbox2d). **STALE.**
- Maven Central `org.jbox2d:jbox2d-library` latest 2.2.1.1, last updated 2013-04-09 **[measured]** — [Maven metadata](https://repo1.maven.org/maven2/org/jbox2d/jbox2d-library/maven-metadata.xml). Java-only: unusable from Kotlin/Native without porting (the korlibs port above is that port).

**dyn4j (Java)**
- "100% Java 2D collision detection and physics engine", New BSD licence — [dyn4j/dyn4j](https://github.com/dyn4j/dyn4j). LICENSE.md: "Copyright (c) 2010-2026, William Bittle … Redistribution and use in source and binary forms" (BSD-3 text); tags 5.0.2 (2024-03-16) and 6.0.0 (2026-07-17); last commit 2026-07-18; main sources 268 files / 64,977 lines of Java **[measured]** — [dyn4j/dyn4j](https://github.com/dyn4j/dyn4j).
- Maven Central `org.dyn4j:dyn4j` latest 6.0.0, last updated 2026-07-18 **[measured]** — [Maven metadata](https://repo1.maven.org/maven2/org/dyn4j/dyn4j/maven-metadata.xml). Maintained, but JVM-only: it would need a ~65k-line port to Kotlin common code to run on iOS.

**KPhysics / JPhysics**
- KPhysics: "an improved and extended version of Hayden Marshall's JPhysics … a 2D physics engine with zero third-party dependencies", distributed through JitPack (`com.github.Chafficui:KPhysics`); feature list: rigid body dynamics, primitive joint constraints, friction, restitution, sequential-impulses solver, "stable object stacking", explosions, object slicing, AABB broadphase, one-shot contact manifolds, **discrete collision detection**, convex polygon and circle collisions, ray casting — [KPhysics README/site](https://kphysics.github.io/KPhysics); [KPhysics/KPhysics](https://github.com/KPhysics/KPhysics).
- KPhysics LICENSE: MIT ("Copyright (c) 2022 Felix Beinßen. This Project is based on JPhysics by HaydenMarshalla, MIT license"); last commit 2022-03-17; no tags; 31 Kotlin files / 3,098 lines (29 non-test files) under `src/main/java/de/chaffic/…` (a JVM Gradle project, not KMP); joints are `JointToBody` and `JointToPoint` only; a grep for `import java.`/`javax.` in the Kotlin sources returned nothing **[measured]** — [KPhysics/KPhysics](https://github.com/KPhysics/KPhysics). 3 stars, per a third-party index — [ecosyste.ms](https://repos.ecosyste.ms/hosts/GitHub/repositories/KPhysics%2FKPhysics). **STALE (2022).**
- JPhysics: MIT ("Copyright (c) 2020 HaydenMarshalla"), Java, 70 files / 11,493 lines incl. demos, last commit 2026-01-05, no tags **[measured]** — [HaydenMarshalla/JPhysics](https://github.com/HaydenMarshalla/JPhysics).

**Kubriko (Compose Multiplatform game engine with a physics plugin) — excluded by licence**
- "A game engine based on Compose Multiplatform for 2D Android, iOS, Desktop, and Web projects", works by "exposing a Composable function that can easily be embedded into Kotlin Multiplatform projects"; published on Maven Central under `io.github.pandulapeter.kubriko`; 282 stars; licence "Mozilla Public License Version 2.0" — [pandulapeter/kubriko](https://github.com/pandulapeter/kubriko).
- LICENSE file is MPL 2.0; latest tag 0.8.0 (2026-10-28); last commit 2026-11-13; `plugins/physics` is 27 Kotlin files / 2,749 lines in `commonMain` (Arbiter, SweepAndPrune, JointToBody, JointToPoint, rays, explosions) **[measured]** — [pandulapeter/kubriko](https://github.com/pandulapeter/kubriko).
- The physics plugin credits KPhysics and JPhysics; features: rigid body dynamics, circular and polygonal bodies, "distance joints (to other bodies or fixed points)", ray casting, explosions, configurable gravity — [plugins/physics README](https://github.com/pandulapeter/kubriko/tree/main/plugins/physics).

**Kanvas**
- "Kotlin Multiplatform 2D runtime for Compose applications", version 0.3.0-alpha, targets Android, JVM desktop, Wasm JS, iOS arm64, iOS simulator arm64; module `io.github.maximdvinov:enginePhysics` = "Optional 2D physics primitives, broadphase, collision, and physics DSL"; MIT ("Copyright (c) 2026 Maxim Dvinov"); last commit 2026-11-01; the physics module has 3 source files (`Physics2D.kt`, `PhysicsDsl.kt`, `PhysicsUnits.kt`) plus one test **[measured]** — [MaximDvinov/Kanvas](https://github.com/MaximDvinov/Kanvas); [klibs.io](https://klibs.io/project/MaximDvinov/Kanvas).

**PhysicsBox**
- Compose physics layout container; Apache-2.0; 52 stars; Android (minSdk 23) and Desktop JVM only: "The runtime uses JBox2D on JVM platforms", "iOS/JS are not supported", iOS target on the roadmap — [GoetzDeBouville/PhysicsBox](https://github.com/GoetzDeBouville/PhysicsBox). Latest tag v2.0.2 (2026-05-24); Maven `io.github.zinchenko-dev:physicsbox` 2.0.2 **[measured]** — [Maven metadata](https://repo1.maven.org/maven2/io/github/zinchenko-dev/physicsbox/maven-metadata.xml).

### Inferences
- Answer to "is there any maintained pure-Kotlin common-code 2D physics library on Maven Central or klibs.io in 2026?":
  not one that is both maintained and permissively licensed. Kubriko's plugin is maintained but MPL-2.0 and tied to
  the Kubriko engine; Kanvas' physics module is alpha and (judging by three source files) not a general rigid-body
  engine; kbox2d is complete (Box2D 2.x feature set: joints, TOI-based CCD, sensors, ray casts, particles) but frozen
  since 2022.
- kbox2d 3.3.0 as a binary dependency is a supply-chain risk rather than a technical impossibility: it compiled
  against Kotlin 2.2.20 in another project in 2026, but nobody will rebuild it if a future Kotlin release drops
  compatibility with Kotlin 1.7-era klibs; vendoring the BSD-2 source (route D) removes that risk at the cost of
  owning 28.8k lines.
- KPhysics (MIT, 3.1k lines, no JVM imports) is small enough to copy into `core` and adapt; Kubriko doing exactly
  that (2.7k lines in `commonMain`) is evidence it works as common code on iOS — but Kubriko's adapted files are
  MPL-2.0, so only the original MIT KPhysics/JPhysics sources may be reused.
- Licensee would see `kbox2d` as MIT plus whatever `korma:3.3.0` and its transitive korlibs declare (not checked).

### Gaps
- Known bugs of kbox2d/korge-box2d: no issue list reviewed; unknown.
- The licence of `com.soywiz.korlibs.korma:korma:3.3.0` and its transitive dependencies was not verified.
- No LiquidFun port for Kotlin Multiplatform was found (searches for KMP Box2D/physics on klibs.io surfaced only
  Boks2D, kbox2d, Kubriko, Kanvas, PhysicsBox); absence is not proof.
- "physics-kt", "box2d-kmp", "box2d-kotlin" as named projects: nothing found under those names.
- Whether `supportBox2d()` still exists in KorGE 7.0.0-SNAPSHOT was not verified (docs flagged as possibly outdated).

## Native Box2D v3 and its Kotlin bindings

### Takeaway
Box2D v3 (MIT, C17) is current at v3.2.0 (tagged 2026-10-18) and states "Box2D has cross-platform determinism as of
version 3.1"; one real KMP binding exists — **Boks2D 0.1.1** (MIT, March 2026, cinterop on iOS + JNI on
Android/JVM, binds v3.1.1) — but it is a 35-star, single-author 0.1.x project, has not moved to v3.2, and its
published Android libraries are 4 KB-aligned (measured), which Google Play's 16 KB rule rejects for apps targeting
Android 15+.

### Cited Findings

**Box2D v3 itself**
- "Box2D is developed by Erin Catto and uses the MIT license"; "Written in portable C17"; 10.5k stars; SIMD (SSE2, AVX2, Neon) used by default, can be disabled with `BOX2D_DISABLE_SIMD` — [erincatto/box2d](https://github.com/erincatto/box2d). LICENSE: "MIT License, Copyright (c) 2022 Erin Catto" **[measured]**.
- Tag dates **[measured]**: v3.0.0 2024-08-11, v3.1.0 2025-04-19, v3.1.1 2025-06-03, v3.2.0 2026-10-18; HEAD 2026-10-29; `src` + `include` = 81 files / 47,335 lines of C — [erincatto/box2d](https://github.com/erincatto/box2d).
- README feature list (verbatim items): "Continuous collision detection", "Contact events", "Convex polygons, capsules, circles, rounded polygons, segments, and chains", "Multiple shapes per body", "Collision filtering", "Ray casts, shape casts, and overlap queries", "Sensor system", "Robust *Soft Step* rigid body solver", "Continuous physics for fast translations and rotations", "Island based sleep", "Revolute, prismatic, distance, motor, weld, and wheel joints", "Joint limits, motors, springs, and friction", "Extensive multithreading and SIMD", "Optimized for large piles of bodies", "Low cost double-precision support for large worlds", "Recording and replay", "Cross platform determinism", "Snapshot and restore" — [erincatto/box2d README](https://github.com/erincatto/box2d).
- v3.1 (released 2025-04-19): new sensor system ("This system is multithreaded and deterministic"), rolling resistance and tangent speed, experimental character mover, "Continuous collision performance is roughly doubled", "GJK was optimized to be 20% faster", "Neon has also been optimized and provides big gains on Apple Silicon" — [box2d.org "Box2D v3.1"](https://box2d.org/posts/2025/04/box2d-3.1/). The v3.1 release notes list "Cross-platform determinism" — [release notes v3.1](https://box2d.org/documentation/md_release__notes__v310.html).
- v3.2 release notes (in-repo `docs/release_notes_v320.md`): API changes — "Joint definitions share a common `b2JointDef base` and use local frames instead of anchors, axes and reference angles", "Mouse joint removed, the motor joint now has spring and velocity targets", "Chain shapes are segment based", "Native task system. Box2D runs its own threads when no task callbacks are hooked up"; new — "Large worlds with optional double precision", "Recording and replay, plus world snapshots (experimental)", "Contact recycling", "Joint events for breakable joints", "Tunable continuous collision per body", "Continuous sensor events", "Runtime AVX2 dispatch with SSE2 fallback"; infrastructure — "Swift and Zig builds in CI", "JSON descriptions of the public API in `api/` for binding generators"; plus a list of silent behaviour changes (e.g. "`b2RelativeAngle(a, b)` swapped its arguments") **[measured]** — [erincatto/box2d docs](https://github.com/erincatto/box2d/blob/main/docs/release_notes_v320.md); summary mirrored at [newreleases.io](https://newreleases.io/project/github/erincatto/box2d/release/v3.2.0).
- Determinism, FAQ (verbatim): "For the same input Box2D will reproduce any simulation. … Box2D is also deterministic under multithreading. A simulation using two threads will give the same result as eight threads. Box2D has cross-platform determinism as of version 3.1. This is heavily tested and a first class feature of Box2D. This assumes precision is not mixed (single versus double precision). Some developers want rollback determinism. You can achieve this using the snapshot system." **[measured from `docs/questions.md` at HEAD]** — [Box2D FAQ source](https://github.com/erincatto/box2d/blob/main/docs/questions.md).
- Determinism, manual (verbatim): "Cross-platform determinism is achieved on 64-bit platforms by using compiler flags and by avoiding non-deterministic library functions. – precise math is used on MSVC – floating point contraction is disabled on clang and GCC – Box2D has custom implementations of atan2, cosine, and sine. … you can break determinism by choosing different compiler flags. … I maintain a unit test for determinism that is run for every pull request." and the note "Box2D determinism does not mean your application will be deterministic." **[measured from `docs/simulation.md`]** — [Box2D simulation docs](https://box2d.org/documentation/md_simulation.html). The root `CMakeLists.txt` adds `-ffp-contract=off` **[measured]**.
- Catto's 2024 blog: "By avoiding fast-math, FMA, and implementing `atan2f` I was able to make Box2D cross-platform deterministic."; "I test Box2D on x64 and ARM CPUs. I also test on MSVC, GCC, and Clang compilers."; "Determinism is a brittle feature. It is easy to break."; at that time "Box2D does not provide roll-back determinism" — [box2d.org "Determinism" (2024-08)](https://box2d.org/posts/2024/08/determinism/).
- Performance: large pyramid benchmark (5,050 bodies, 4 workers): "AVX2 : 1117 fps = 0.90 ms; Neon : 1058 fps = 0.95 ms; SSE2 : 982 fps = 1.02 ms; scalar (AMD): 524 fps = 1.91 ms; scalar (M2): 679 fps = 1.47 ms", run on an AMD 7950X and an Apple M2 — [box2d.org "SIMD Matters" (2024-08)](https://box2d.org/posts/2024/08/simd-matters/). The repo's `benchmark/` folder holds result sets for `amd7950x_*`, `m2air_*` and `n100_sse2` only **[measured]** — no phone results.
- Third-party comparison (author of the Godot Rapier plugin, 2026-08-08, so not neutral): Rapier2D vs Box2D v3.1 vs Godot Physics 4.7 — Pyramid 20.0 / 21.3 (unstable) / 147 ms; Smash 11.9 / 11.6 / 55.1 ms; Mixed pile 9.3 / 6.7 / 71.1 ms; Query storm 6.2 / 4.6 / 57.2 ms; Joint grid 10.2 / 4.9 (unstable) / did not finish — [godot.rapier.rs blog v0.35](https://godot.rapier.rs/blog/v0-35-0).

**Boks2D — the KMP binding**
- "Kotlin Multiplatform bindings for the Box2D physics engine (v3). Supported platforms: Android, iOS and Desktop."; MIT; 35 stars, 2 forks, 13 commits, 3 open issues; Maven `io.github.joaomcl:boks2d` — [joaomcl/Boks2d](https://github.com/joaomcl/Boks2d). klibs.io: version 0.1.1, platforms Android, JVM, iOS, 1 dependent — [klibs.io](https://klibs.io/project/joaomcl/Boks2d).
- **[measured from the repo]**: LICENSE "MIT License, Copyright (c) 2026 João (@joaomcl)"; tags v0.1.0, v0.1.1 (2026-03-17); last commit 2026-03-23; built with Kotlin 2.1.21, AGP 8.12.3; targets `androidTarget`, `jvm`, `iosArm64`, `iosSimulatorArm64`, `iosX64`; Android ABIs arm64-v8a, armeabi-v7a, x86_64, x86 with `ANDROID_PLATFORM=android-21`, minSdk 21; iOS: Box2D built by CMake as static `libbox2d.a` (deployment target 13.0) and bound with a 9-line cinterop `.def` (`headers = box2d/box2d.h`, `staticLibraries = libbox2d.a`); Android/desktop JVM: hand-written JNI in C++ (5 files, 4,922 lines) with Box2D linked statically into `libboks2d`; Kotlin sources 13,519 lines; Box2D submodule pinned to commit `8c661469c9507d3ad6fbd2fea3f1aa71669c2fe3` — [joaomcl/Boks2d](https://github.com/joaomcl/Boks2d). That commit is the one libGDX names as "the v3.1.1 release of Box2D" — [libgdx/gdx-box2d](https://github.com/libgdx/gdx-box2d).
- API surface **[measured: file list]**: `World`, `Body`, shapes (Circle, Polygon, Capsule, Segment, Chain), joint defs (Distance, Filter, Motor, Mouse, Prismatic, Revolute, Weld, Wheel), `RayCastInput`/`RayResult`, `ShapeCastInput`, `SensorEvents`, `ContactEvents` (begin/end/hit), `BodyMoveEvent`, `CharacterMover`, `ExplosionDef`, 64-bit filter categories; common tests for bodies, chains, joints, world, character mover; a Compose Multiplatform sample app (`samples/CMPApp`) — [joaomcl/Boks2d](https://github.com/joaomcl/Boks2d).
- Maven Central: versions 0.1.0 and 0.1.1 only (last updated 2026-03-18), with `boks2d-android`, `boks2d-jvm`, `boks2d-iosarm64` etc.; POM licence "MIT License" **[measured]** — [Maven Central](https://repo1.maven.org/maven2/io/github/joaomcl/); [central.sonatype.com](https://central.sonatype.com/artifact/io.github.joaomcl/boks2d).
- **16 KB finding [measured]**: in `boks2d-android-0.1.1.aar`, `jni/arm64-v8a/libboks2d.so` (3,037,632 bytes) and `jni/x86_64/libboks2d.so` (2,812,264 bytes) have all `LOAD` segments with `Align 0x1000` (4 KB), are "with debug_info, not stripped", and were built with "Android … clang version 18.0.4"; stripping the x86_64 library with `strip --strip-unneeded` gives 606,120 bytes; armeabi-v7a 2,463,376 and x86 2,268,336 bytes unstripped; `classes.jar` 228,821 bytes — [boks2d-android 0.1.1 on Maven Central](https://repo1.maven.org/maven2/io/github/joaomcl/boks2d-android/0.1.1/).
- Desktop natives inside `boks2d-jvm-0.1.1.jar` **[measured]**: `linux-x64/libboks2d.so` 503,800 bytes, `macos-arm64` 354,464, `macos-x64` 355,848, `windows-x64/boks2d.dll` 355,840 (no Linux arm64); iOS: `boks2d-iosarm64-0.1.1.klib` 314,407 bytes plus a separate `-cinterop-box2d.klib` — [Maven Central](https://repo1.maven.org/maven2/io/github/joaomcl/boks2d-jvm/0.1.1/).
- Benchmark: "classic tumbler scenario … Body count doubles from 100 to 3200 … 300 warmup steps followed by 1000 measured steps", compared with KBox2D (JVM and iOS) and dyn4j 5.0.2 (JVM only); results are published only as SVG plots — [joaomcl/Boks2d README](https://github.com/joaomcl/Boks2d). The author's Kotlin Slack announcement says Boks2D "came out significantly ahead" of KBox2D and dyn4j and that Kotlin/Wasm is planned (author's own claim; page returned HTTP 429 when fetched, wording taken from the search-result summary) — [Kotlin Slack archive](https://slack-chats.kotlinlang.org/t/33175444/hello-everyone-i-ve-just-released-a-new-kmp-library-called-b).

**Other Box2D v3 bindings (not KMP-complete)**
- `fabmax/box2d-jni`: "Java / JNI bindings for Box2D", "covers the full API of `box2d` in version `3.2.0`"; desktop JVM (Windows, Linux, macOS; x86_64 and arm64) and Android (arm-v8, arm-v7, x86_64, x86) as one AAR; generated from WebIDL interface files; no iOS; MIT; 3 stars; Maven `de.fabmax.box2d-jni:box2d-jni:2.0.0` and `…:box2d-jni-android:2.0.0`; a separate `kool-box2d-wasm` package targets Kotlin/JS — [fabmax/box2d-jni](https://github.com/fabmax/box2d-jni). Tags v1.0.0 (2025-12-07), v2.0.0 (2026-10-24, six days after Box2D v3.2.0); LICENSE "MIT License, Copyright (c) 2025 Max Thiele" **[measured]**.
- `libgdx/gdx-box2d`: "java bindings for the v3.1.1 release of Box2D", Apache-2.0, 11 stars, 83 commits; platforms desktop, Android (arm64-v8a, armeabi-v7a, x86, x86_64) and iOS (`natives-ios`, arm64 device and arm64/x86_64 simulator) — the iOS natives are for the Java-on-iOS path ("still safe to use with mobiVM"), not Kotlin/Native; coordinates `com.badlogicgames.gdx:gdx-box2d:$version` — [libgdx/gdx-box2d](https://github.com/libgdx/gdx-box2d). Tag v3.1.1-0 dated 2025-06-22; last commit 2025-10-23; no v3.2 release **[measured]**.
- Other language bindings seen in passing (evidence that v3's C API is easy to bind): Box2D.NET, box2d-rust, Box2D3.js, Solar2D port — [search results for Box2D v3.1](https://box2d.org/posts/2025/04/box2d-3.1/).

**Google Play 16 KB page-size rule**
- "all apps targeting Android 15 (API level 35) and higher must support 16 KB memory page sizes on 64-bit devices on Google Play. Starting February 1, 2027, if your app updates don't support 16 KB memory page sizes, you won't be able to release these updates." — [Android developers: Support 16 KB page sizes](https://developer.android.com/guide/practices/page-sizes) (text as served on 2026-11-13).
- "If your app only uses code written in the Java programming language or in Kotlin, including all libraries or SDKs, then your app already supports 16 KB devices." / "If your app uses any NDK libraries, either directly or indirectly through an SDK, then you will need to rebuild your app for it to work on these 16 KB devices." — [same page](https://developer.android.com/guide/practices/page-sizes).
- "NDK version r28 and higher compile 16 KB-aligned by default"; for r27 and lower add `-Wl,-z,max-page-size=16384` and `-Wl,-z,common-page-size=16384`; packaging needs AGP 8.5.1+ with uncompressed shared libraries; check that `LOAD` segments show `align 2**14`; "If your app uses any prebuilt shared libraries, you must also recompile them in the same way" — [same page](https://developer.android.com/guide/practices/page-sizes).

### Inferences
- Boks2D 0.1.1 cannot be shipped to Google Play unchanged in an app targeting API 35+: its 64-bit `.so` files are
  4 KB-aligned (measured), and prebuilt libraries "must" be recompiled. Options: wait for/ask for an upstream release
  built with NDK r28+, or fork and rebuild (its Gradle build already drives CMake per ABI; adding the two linker flags
  or moving to NDK r28 is a small change, but then the template must consume a self-published or vendored artifact).
- Boks2D lags Box2D: it binds v3.1.1 (June 2025) and has had no commit since 2026-03-23, while v3.2.0 (2026-10-18)
  changed joint definitions and removed the mouse joint — an upgrade means reworking its joint layer. fabmax's binding
  moved to v3.2.0 within a week, but has no iOS half.
- Because Boks2D's `jvm` artifact bundles a Linux x64 native, `./gradlew check` on an x64 Linux CI runner could run
  physics tests of `core` on the JVM; an arm64 Linux runner could not (no linux-arm64 native in the jar).
- Box2D's cross-platform determinism is stated for 64-bit platforms and depends on compiler flags; Boks2D builds Box2D
  through Box2D's own CMakeLists (which sets `-ffp-contract=off`), so the property plausibly carries over to arm64
  Android, arm64 iOS and x64/arm64 desktop, but nobody has published a test of that through Boks2D, and the 32-bit
  Android ABIs it also ships are outside the stated guarantee.
- Effort to write an own binding **[estimate]**: the iOS half is nearly free (cinterop generates Kotlin from
  `box2d.h`; Boks2D's `.def` is 9 lines) and Box2D v3.2 now ships JSON API descriptions "for binding generators";
  the JNI half is the work (Boks2D needed ~4.9k lines of C++ for the whole API). For a game-sized subset (world, body,
  circle/box/polygon/capsule shapes, revolute/distance/weld/wheel joints, contact + sensor events, ray cast):
  about 2–3 weeks including CMake builds for 3 iOS slices, 2–4 Android ABIs, a desktop host library for JVM tests,
  and CI wiring; plus recurring work at each Box2D release.
- Size: a stripped Box2D v3.1.1 + JNI wrapper is roughly 0.4–0.6 MB per ABI (measured 606 KB for Android x86_64
  stripped, 354–504 KB for desktop builds); negligible against a Compose Multiplatform app.
- On iOS a statically linked C library raises no page-size or store-policy issue; the 16 KB rule is Android-only.

### Gaps
- Numeric results of Boks2D's tumbler benchmark (only plots are published) and any benchmark on a real phone.
- Whether Boks2D's published Android libraries pass on a 16 KB device in "backcompat mode" was not tested; the
  alignment values were read with `readelf`, not validated with Play Console.
- Whether `fabmax/box2d-jni-android:2.0.0` is 16 KB-aligned (Maven Central rate-limited further downloads).
- No evidence found of any shipped app using Boks2D (klibs.io shows one dependent).
- Bodies-per-frame on mid-range Android: not found; the only data are 5,050 bodies at 0.95–1.47 ms/step on an Apple M2.

## Other native engines: Chipmunk2D and Rapier

### Takeaway
Neither has Kotlin Multiplatform bindings that I could find. Chipmunk2D (MIT, C) has had no release since 7.0.3 in
June 2019 and its maintainer is leaving GitHub for Codeberg; Rapier (Apache-2.0, Rust) is active and offers
cross-platform determinism via the `enhanced-determinism` feature, but reaching it from Kotlin means writing a C FFI
layer and Rust cross-compilation for Android and iOS on top of the JNI/cinterop work Box2D already needs.

### Cited Findings
- Chipmunk2D: MIT-style licence ("Copyright (c) 2007-2015 Scott Lembcke and Howling Moon Software. Permission is hereby granted, free of charge…"); latest tag `Chipmunk-7.0.3` dated 2019-06-07; last GitHub commit 2026-05-05 (a merged pull request); 62 files / 14,629 lines of C in `src` + `include` **[measured]** — [slembcke/Chipmunk2D](https://github.com/slembcke/Chipmunk2D). **STALE (release 2019).**
- The repository carries `PROJECT_HAS_MOVED.md`: "You can find the new project page on Codeberg … At least for now, I will be supporting the project on both sites, but eventually I plan to eventually drop GitHub entirely." **[measured]** — [slembcke/Chipmunk2D](https://github.com/slembcke/Chipmunk2D); new home [codeberg.org/slembcke/Chipmunk2D](https://codeberg.org/slembcke/Chipmunk2D).
- Chipmunk2D "is a 2D rigid body physics library distributed under the MIT license" — [chipmunk-physics.net](https://chipmunk-physics.net/aboutChipmunk.php); stable release 7.0.3 of June 7, 2019, Pro features folded into the free version — [Wikipedia: Chipmunk (software)](https://en.wikipedia.org/wiki/Chipmunk_(software)).
- Rapier: Apache-2.0 (LICENSE file); tag v0.36.1 dated 2026-10-06; last commit 2026-10-10; `rapier2d` has the feature `enhanced-determinism = ["simba/libm_force", "parry2d/enhanced-determinism"]` **[measured]** — [dimforge/rapier](https://github.com/dimforge/rapier).
- Rapier determinism docs: "By default, Rapier is locally deterministic."; "It is possible to make Rapier cross-platform deterministic." when "The `enhanced-determinism` feature of Rapier is enabled" and "The target platforms must strictly comply to the IEEE 754-2008 floating-points standard", which "include[s] most modern mainstream processors as well as WASM targets"; it cannot be combined with `simd8`, while "The `parallel` feature … can be combined with it" — [rapier.rs determinism guide](https://rapier.rs/docs/user_guides/rust/determinism/).
- Official non-Rust bindings are JavaScript/WASM (`@dimforge/rapier2d` 0.21.0, published 2026-09-25, Apache-2.0) **[measured from the npm registry]** — [npm @dimforge/rapier2d](https://www.npmjs.com/package/@dimforge/rapier2d).
- A third-party repository `rapier-ffi` "provides bindings for the Rapier physics engine to various languages" (language list not confirmed to include Kotlin) — [aecsocket/rapier-ffi (via mirror)](https://github-redirect.dependabot.com/aecsocket/rapier-ffi). A Flutter binding exists (`rapier_physics`) — [pub.dev](https://pub.dev/documentation/rapier_physics/0.4.0/).
- Godot Rapier plugin: MIT ("Copyright (c) 2023 Fabrice Cipolla, Sp3ctralCat and Dragos Daian"), tag v0.36.1 on 2026-11-08 **[measured]** — [appsinacup/godot-rapier-physics](https://github.com/appsinacup/godot-rapier-physics); "Both 2D and 3D builds now always have parallel SIMD solver and cross platform determinism enabled." — [godot.rapier.rs blog](https://godot.rapier.rs/blog/v0-35-0).

### Inferences
- With Box2D v3 available under MIT, in C, with an existing KMP binding and its own cross-platform determinism,
  neither Chipmunk2D nor Rapier offers this template anything that justifies the extra binding work; Rapier would
  only become interesting if a maintained KMP wrapper appeared.
- Effort to bind Rapier from Kotlin **[estimate]**: Box2D-binding effort plus a hand-written `extern "C"` layer in
  Rust and cargo cross-compilation (aarch64-linux-android, aarch64-apple-ios, simulator, host) — roughly 4–6 weeks.

### Gaps
- No search hit for Kotlin/JNI/KMP bindings of Rapier 2D or Chipmunk2D; `rapier-ffi`'s contents were not inspected.
- Activity on Chipmunk2D's Codeberg repository after May 2026 was not checked.

## JavaScript engines in a WebView (comparison route)

### Takeaway
All mainstream JS options are MIT or Apache-2.0 and small (26 KB gzipped for Matter.js up to ~0.9 MB gzipped for
Rapier's WASM), with Planck.js, Rapier and box2d3-wasm released in autumn 2026 and Matter.js unreleased since June
2024; as a route they move both simulation and rendering out of Kotlin, so they compete with the template rather
than plug into it.

### Cited Findings
Versions, dates and licences from the npm registry; sizes measured on the published tarballs (**[measured]**, gzip -9):

| Package | Latest (npm publish date) | Licence | Shipped file | Size | Gzipped |
|---|---|---|---|---|---|
| `planck` (Planck.js, Box2D 2.x rewrite in TS) | 1.5.1 (2026-10-12) | MIT | `dist/planck.min.js` | 297 KB | 55 KB |
| `matter-js` | 0.20.0 (2024-06-23) **STALE** | MIT | `build/matter.min.js` | 83 KB | 26 KB |
| `@dimforge/rapier2d` | 0.21.0 (2026-09-25) | Apache-2.0 | `rapier_wasm2d_bg.wasm` + JS glue | 2.40 MB + 298 KB | 906 KB + 30 KB |
| `phaser-box2d` (JS port of Box2D v3.0) | 1.1.0 (2025-01-01) | MIT | `dist/PhaserBox2D.min.js` | 276 KB | 69 KB |
| `box2d3-wasm` (Box2D v3 compiled to WASM) | 6.0.0 (2026-10-26) | MIT | `Box2D.compat.wasm` + `.mjs` | 612 KB + 102 KB | 211 KB + 27 KB |
| `box2d-wasm` (Box2D 2.4 WASM) | 7.0.0 (2021-11-28) **STALE** | Zlib | – | – | – |
| `phaser` (framework; Arcade + Matter physics built in) | 4.2.1 (2026-07-09) | MIT | – | 112 MB unpacked package | – |

- Sources: [npm planck](https://www.npmjs.com/package/planck), [npm matter-js](https://www.npmjs.com/package/matter-js), [npm @dimforge/rapier2d](https://www.npmjs.com/package/@dimforge/rapier2d), [npm phaser-box2d](https://www.npmjs.com/package/phaser-box2d), [npm box2d3-wasm](https://www.npmjs.com/package/box2d3-wasm), [npm box2d-wasm](https://www.npmjs.com/package/box2d-wasm), [npm phaser](https://www.npmjs.com/package/phaser).
- Planck.js: LICENSE.txt "The MIT License, Copyright (c) Erin Catto, Ali Shakiba"; last commit 2026-10-23; repository `package.json` already says 1.6.0 while npm `latest` is 1.5.1; `src` is ~32k lines of TypeScript **[measured]** — [piqnt/planck.js](https://github.com/piqnt/planck.js).
- Matter.js: "The MIT License (MIT), Copyright (c) Liam Brummitt and contributors"; newest tag 0.20.0 (2024-06-23) although the repository's last commit is 2026-09-30; `src` 32 files / 11,097 lines **[measured]** — [liabru/matter-js](https://github.com/liabru/matter-js).
- Phaser Box2D: MIT ("Copyright (c) 2024 Phaser"), tag v1.1.0 on 2025-01-01, last commit 2025-04-03 **[measured]** — [phaserjs/phaser-box2d](https://github.com/phaserjs/phaser-box2d); announced December 2024 — [phaser.io](https://phaser.io/news/2024/12/announcing-phaser-box2d).
- Phaser: "The MIT License (MIT), Copyright (c) 2026 Richard Davey, Phaser Studio Inc."; tag v4.2.1; last commit 2026-08-21 **[measured]** — [phaserjs/phaser](https://github.com/phaserjs/phaser).
- box2d-wasm (Birch-san): Zlib licence, tag v7.0.0 dated 2021-11-29, last commit 2024-12-29 **[measured]** — [Birch-san/box2d-wasm](https://github.com/Birch-san/box2d-wasm).
- Rapier's cross-platform determinism explicitly covers "WASM targets" — [rapier.rs determinism guide](https://rapier.rs/docs/user_guides/rust/determinism/).

### Inferences
- In a WebView route the natural design is physics **and** canvas rendering in JS (stepping physics in JS while
  drawing in Compose would need a per-frame bridge message per body). That means the game is a web app inside a
  Compose shell: it cannot live in `core`, cannot be unit-tested on the JVM by `./gradlew check`, and breaks
  "Compose Multiplatform for every screen". It is a comparison baseline, not a way to add physics to this template.
- If the route were chosen anyway, Planck.js (maintained, 55 KB gz, Box2D 2.x semantics incl. joints and CCD) or
  box2d3-wasm (Box2D v3 semantics) are the closest functional equivalents of the native routes; Matter.js is the
  smallest but has no release since mid-2024 and no CCD (the latter from general knowledge, not verified here).

### Gaps
- Third-party "comparison" pages gave conflicting sizes (82–206 KB) and versions; I used only registry data and my
  own measurements. WebView policy/monetisation aspects are covered by another researcher's notes.
- Whether `box2d3-wasm` 6.0.0 tracks Box2D v3.2 was not checked.

## Full 2D game engines as alternatives

### Takeaway
Every full engine replaces the Compose Multiplatform app (own renderer, own build plugin or own language) rather
than joining it; KorGE is the only Kotlin one and is alive (Kotlin 2.3.21, AGP 9, namespace move to `org.korge` in
2026) but still has 6.0.0 (May 2025) as its latest stable release, and Godot's new LibGodot embedding is
desktop-only in 4.6. The Compose-native engines that could join the app are Kubriko (MPL-2.0, excluded) and Kanvas
(MIT, alpha).

### Cited Findings
- **KorGE**: LICENSE opens "Each library has its own licenses typically MIT or Public Domain, dual licensed with Apache 2.0", followed by an MIT licence text "Copyright (c) 2017-2026 The KorGE Team and contributors"; newest stable tag v6.0.0 dated 2025-05-15; HEAD 2026-10-25; version catalogue at HEAD: `kotlin = "2.3.21"`, `korlibs = "7.0.0-SNAPSHOT"` **[measured]** — [korlibs/korge](https://github.com/korlibs/korge). About 3.1k stars — [korlibs/korge](https://github.com/korlibs/korge).
- KorGE README: "The latest official release on Maven Central is `6.0.0`. The current snapshot release on Maven Central is `7.0.0-SNAPSHOT`"; news 02-May-2026: "We are moving Korge to a new namespace on maven central, from `com.soywiz.korge` to `org.korge`. This a breaking change."; news 08-Sep-2026: "Korge was updated to Gradle 9 and Android Gradle Plugin 9"; targets: "JVM for Android, JS & WASM for the Web, native code for iOS, and JVM/JS for Desktop"; applied through its own Gradle plugin (`id = "org.korge.engine"`) — [korlibs/korge README](https://github.com/korlibs/korge).
- KorGE ↔ Compose: the existing bridge goes the other way — `korlibs/korge-compose` adds Compose-style composables *inside* KorGE (last commit 2024-06-13 **[measured]**, **STALE**) — [korge-compose](https://github.com/korlibs/korge-compose); a Kotlin Slack thread asks "is korge something I can nest inside of compose similar to canvas" (no documented supported way found) — [Kotlin Slack archive](https://slack-chats.kotlinlang.org/t/23439182/is-korge-something-i-can-nest-inside-of-compose-similar-to-c).
- **Godot**: LICENSE.txt is the MIT/Expat text ("Copyright (c) 2014-present Godot Engine contributors … Permission is hereby granted, free of charge"); newest tags 4.7.1-stable, 4.7.2-stable **[measured]** — [godotengine/godot](https://github.com/godotengine/godot). Godot 4.6 (January 2026): "With the new LibGodot, you can now embed the engine directly into your own applications" — [godotengine.org 4.6 release page](https://godotengine.org/releases/4.6/); initial LibGodot support covers Linux, Windows and macOS — [AlternativeTo news](https://alternativeto.net/news/2026/1/godot-4-6-debuts-modern-theme-jolt-physics-node-ids-and-libgodot-embed); a proposal describes embedding in an iOS/Android host as lacking "a standardized way for a host application to provide an existing OS-level rendering surface" — [godot-proposals #14435](https://github.com/godotengine/godot-proposals/issues/14435). Godot 4.7 shipped June 2026; coverage found mentions HDR and an Android build environment, not mobile LibGodot — [godotengine.org 4.7](https://godotengine.org/releases/4.7/).
- Godot size (forum anecdotes, not benchmarks): blank Godot 4.2 Android project "about 19MB" standard, ~13 MB with modules cut — [Godot forum](https://forum.godotengine.org/t/godot-4-android-build-size-difference/58593); a Godot 4.5.1 user reported a 244 KB project exporting to a 114 MB APK — [Godot forum](https://forum.godotengine.org/t/absurd-file-size-increase-when-my-project-is-exported-to-apk/130000).
- Godot's built-in 2D physics is far slower than Box2D/Rapier in the Rapier-plugin author's 2026 benchmark (4.76×–12.56× slower than the fastest engine; table in the Box2D section) — [godot.rapier.rs blog](https://godot.rapier.rs/blog/v0-35-0).
- **libGDX**: Apache License 2.0 (LICENSE); newest tag 1.14.2; HEAD 2026-11-13 **[measured]** — [libgdx/libgdx](https://github.com/libgdx/libgdx). Its iOS path is Java bytecode compiled by MobiVM (RoboVM fork), as its Box2D binding's README states ("safe to use with mobiVM") — [libgdx/gdx-box2d](https://github.com/libgdx/gdx-box2d); Kotlin use is JVM Kotlin via KTX (`io.github.libktx:ktx-box2d`) — [Maven Central](https://central.sonatype.com/artifact/io.github.libktx/ktx-box2d/).
- **Defold**: "Defold License, Version 1.0, May 2020", "derived from Apache 2.0"; "You can not commercialise original or modified (derivative) versions of the Defold editor and/or engine"; games may be commercialised and there are no royalties or fees — [defold.com/license](https://defold.com/license/). Newest tag 1.14.1, HEAD 2026-11-13 **[measured]** — [defold/defold](https://github.com/defold/defold). Lua-scripted; a forum thread discusses its Box2D 3.0 integration in 1.10.0 — [Defold forum](https://forum.defold.com/t/updating-release-notes-for-1-10-0-box2d-3-0-ui/81101).
- **LÖVE**: licence file lists "LOVE … License: zlib … Copyright (c) 2006-2026 LOVE Development Team"; newest tag 11.5; HEAD 2026-11-04 **[measured]** — [love2d/love](https://github.com/love2d/love). Lua.
- **Flame** (Flutter/Dart): "MIT License, Copyright (c) 2021 Blue Fire"; newest tag v1.38.2; HEAD 2026-11-13 **[measured]** — [flame-engine/flame](https://github.com/flame-engine/flame).
- **Phaser**: MIT, v4.2.1 (see JS section) — [phaserjs/phaser](https://github.com/phaserjs/phaser).
- **Kubriko** (joins Compose, but MPL-2.0) and **Kanvas** (joins Compose, MIT, 0.3.0-alpha): see the pure-Kotlin section — [pandulapeter/kubriko](https://github.com/pandulapeter/kubriko); [MaximDvinov/Kanvas](https://github.com/MaximDvinov/Kanvas).

### Inferences
- "Join or replace" summary: KorGE — replaces (own Gradle plugin, own GL/Metal renderer, own app entry points; no
  supported nesting in Compose found). Godot — replaces (GDScript/C#, own export pipeline; LibGodot not yet a mobile
  embedding path). libGDX — replaces and is not Kotlin/Native on iOS. Defold, LÖVE — replace, Lua. Flame — replaces,
  Flutter. Phaser — WebView. Kubriko, Kanvas — join (they are Composables), but one is MPL-2.0 and the other alpha.
- Defold's licence is not in the template's allow-list and is not OSI-approved in form (field-of-use restriction);
  it only matters if engine code were redistributed as a dependency, which a Defold game would not do through
  Gradle anyway — it is simply a different toolchain.
- KorGE's physics is the same JBox2D port discussed above, so KorGE offers no better physics than route C/D while
  costing the whole template (machinery plugins, Compose UI, store pipeline).

### Gaps
- KorGE iOS build health in 2026 (Kotlin/Native target, App Store submissions) was not verified beyond the README's
  target list; no date for a 7.0.0 stable release was found.
- Whether LibGodot mobile embedding merged after 4.7 (4.8 development) is unknown; GodotCon 2026 talk abstracts say
  mobile enhancements were "in the process of being merged" — [GodotCon talk listing](https://talks.godotengine.org/godotcon-ams-2026/talk/DQXZZC.ics) (abstract only, status unconfirmed).
- No reliable minimum binary size for Godot 4.7 on iOS/Android; forum figures range 13–114 MB.

## "Write your own" feasibility

### Takeaway
A teaching-grade sequential-impulse engine is ~1.25k lines (Box2D-Lite, boxes only) and a usable Kotlin one with
circles, convex polygons, a broadphase, two joint types and ray casts is ~3k lines (KPhysics; Kubriko's common-code
adaptation is 2.7k), so a minimal engine in `core` is feasible; the hard parts that separate it from Box2D (47k
lines) are stable stacking, tunnelling/CCD, joint behaviour under mass ratios, and sleeping — and several of the
target game types can avoid a general engine entirely.

### Cited Findings
- Box2D-Lite: "a small 2D physics engine … developed for the 2006 GDC Physics Tutorial. This is the original version of the larger Box2D library. The Lite version is more suitable for learning about game physics."; MIT; 1.1k stars — [erincatto/box2d-lite](https://github.com/erincatto/box2d-lite). **[measured]**: 1,252 lines in total — `Arbiter.cpp` 197, `Body.cpp` 56, `Collide.cpp` 328, `Joint.cpp` 109, `World.cpp` 136, headers 426 (of which `MathUtils.h` 196); `Body` has a single `Vec2 width` (boxes only); one `Joint` type with `biasFactor`/`softness`; `World` has static `accumulateImpulses` and `warmStarting` flags and a fixed `iterations` loop; tags 1.0.0, 1.0.1.
- Size ladder **[measured line counts]**: Box2D-Lite 1.25k (C++) → Kubriko physics plugin 2.7k (Kotlin, common) → KPhysics 3.1k (Kotlin) → Matter.js `src` 11.1k (JS) → Chipmunk2D 14.6k (C) → Solver2D 16.1k (C) → JBox2D library 29.3k (Java) / its Kotlin port 28.8k → Box2D v3 47.3k (C) → dyn4j 65.0k (Java) — repositories linked in the sections above; [erincatto/solver2d](https://github.com/erincatto/solver2d) (MIT, last commit 2026-09-01).
- Solver2D is "a cut of Box2D version v3.0 with many features removed" (no sleeping, two joint types, no continuous collision, no threading) used to compare eight Gauss-Seidel solver variants (PGS with Baumgarte, PGS_NGS, PGS_NGS_Block, PGS_Soft, TGS_Sticky, TGS_Soft, TGS_NGS, XPBD) under "large mass ratios, large coordinates, significant overlap"; "It has long been known that smaller time steps are more effective than more iterations."; "Relaxation is expensive, but it improves the simulation quality dramatically."; TGS_Soft (sub-stepping + warm starting + soft constraints + relaxation) became Box2D v3's "Soft Step" solver — [box2d.org "Solver2D" (2024-02)](https://box2d.org/posts/2024/02/solver2d/).
- Even Box2D v3 documents limits **[measured from `docs/loose_ends.md`, `docs/simulation.md`, `docs/questions.md`]**: "Extreme mass ratios may cause joint stretching and collision overlap."; fast bodies can "incorrectly pass through each other. This effect is called *tunneling*."; "By default, Box2D uses continuous collision detection (CCD) to prevent dynamic bodies from tunneling through static bodies"; bullets "perform CCD with all body types, but **not** other bullets … I recommend using bullets sparingly"; "Common mistakes: Using pixels units without calling `b2SetLengthUnitsPerMeter()`; Expecting Box2D to give pixel perfect results" — [Box2D simulation docs](https://box2d.org/documentation/md_simulation.html); [Box2D repo docs](https://github.com/erincatto/box2d/tree/main/docs).
- Sequential impulses are order-dependent ("smear velocities across the system depending on the order in which constraints are solved"); resting contact needs a penetration "slop" allowance; fixing one contact can break another on the same body, worsening with more bodies — discussion threads and tutorials: [gamedev.net "Modeling restitution"](https://gamedev.net/forums/topic/696878-modeling-restitution/5379685/); [Tuts+ "How to Create a Custom 2D Physics Engine: The Basics and Impulse Resolution"](https://code.tutsplus.com/how-to-create-a-custom-2d-physics-engine-the-basics-and-impulse-resolution--gamedev-6331t); [nkarve "How to Make a 2D Physics Engine – Constraints"](https://nkarve.github.io/programming/2021/07/01/physeng3.html).
- Small reference implementations exist beyond Box2D-Lite, e.g. SmallSI, "inspired by Box2D-Lite, but tried to make it more understandable" — [XMAMan/SmallSI](https://github.com/XMAMan/SmallSI).
- Verlet/position-based route: Jakobsen's "Advanced Character Physics" presents a simple, stable method for cloth, soft and rigid bodies and articulated bodies; velocity is implicit in current vs previous position and constraints are satisfied by repeated projection (relaxation); it was the basis of Hitman: Codename 47's ragdolls — [Game Developer reprint](https://gamedeveloper.com/programming/advanced-character-physics); [PDF copy](https://edelkas.github.io/n/files/bib/jakobsen.pdf); [Wikipedia: Ragdoll physics](https://en.wikipedia.org/wiki/Ragdoll_physics). Tutorial-level application to tearable cloth and ragdolls — [Tuts+](https://code.tutsplus.com/simulate-tearable-cloth-and-ragdolls-with-simple-verlet-integration--gamedev-519t). Its limitation: only particle positions are integrated, so rigid bodies need many particles tied by distance constraints — [Baltman & Radeztsky follow-up paper](https://edelkas.github.io/n/files/bib/baltman.pdf).
- Existence proof for a common-code Kotlin engine with tests: Kubriko's physics plugin has common tests for bodies, ray intersection, explosions and sweep-and-prune, and desktop tests for the fixed-step accumulator **[measured file list]** — [pandulapeter/kubriko](https://github.com/pandulapeter/kubriko/tree/main/plugins/physics).

### Inferences
- Classes of game that can skip a general engine (my assessment, built on the sources above):
  - **Ropes, chains, cloth, simple ragdolls, cut-the-rope-like**: verlet particles + distance constraints
    (Jakobsen); a few hundred lines; unconditionally stable; weak at rigid stacking and exact joint limits.
  - **Circle-only merge/drop ("suika"-type), marble, peg/plinko**: circle–circle and circle–segment tests are a
    few lines each; a sequential-impulse or position-based solver over circles avoids polygon manifolds, SAT/GJK and
    most of the stacking difficulty (circle contacts are single points). Still needs iterations/sub-steps for piles.
  - **Pinball, breakout, slingshot with ballistic flight**: one or few fast balls against static geometry — swept
    circle-vs-segment/arc tests give exact, tunnelling-free collisions analytically; flippers are kinematic.
  - **Tile/kinematic platformers**: AABB sweeps, no rigid-body dynamics.
  - **Needs a real engine**: box/polygon stacking and toppling towers, bridge-building (many joints, mass ratios,
    breakable joints), vehicle/wheel joints, ragdolls with joint limits colliding with piles.
- Effort **[estimate]**: Box2D-Lite-equivalent in Kotlin (boxes + circles, SI with warm starting, O(n²) or grid
  broadphase): about 1 week including tests. Adding convex polygons, revolute/distance joints, sleeping and a simple
  CCD for fast circles: 3–5 more weeks, with stacking stability and joint drift as the recurring time sinks.
  Per-game special-purpose physics: 2–5 days each.
- An own engine in `core` is the route most compatible with agent-built games in this template: no native binaries,
  no licence or page-size questions, fully testable with golden tests on the JVM — at the price of physical fidelity
  that reviewers and players will compare against Box2D-based competitors.

### Gaps
- I did not read Erin Catto's GDC slide decks themselves (2006 "Fast and Simple Physics using Sequential Impulses",
  later soft-constraint/CCD talks); the 2006 tutorial PDF is in the Box2D-Lite repo (`docs/GDC2006_Catto_Erin_PhysicsTutorial.pdf`) — [erincatto/box2d-lite](https://github.com/erincatto/box2d-lite).
- No sourced line count or post-mortem for a shipped merge/drop game with hand-written physics was found; the
  "circle-only is easy" claim is my inference.
- Box2D-Lite's broadphase being O(n²) is from memory, not re-read.

## Determinism for a `core` simulation (JVM vs Kotlin/Native, ARM64 vs x86-64)

### Takeaway
Bit-exact floating-point results across JVM and Kotlin/Native are plausible for `+ − × ÷ sqrt` on 64-bit targets
(Java has been always-strict since 17; IEEE 754 fixes those operations) but are not guaranteed by any Kotlin
documentation I could find, and `kotlin.math.sin/cos/atan2` demonstrably differ between platforms — the same three
hazards (FMA contraction, fast-math, libm trigonometry) that Box2D v3 and Rapier had to neutralise. For single-player
ad-funded games only same-build replay determinism is needed; cross-platform determinism is a nice-to-have for
golden tests and optional features.

### Cited Findings
- Box2D v3: three levels — "algorithmic, multithreaded, and cross-platform"; recipe: never use fast-math; "FMA must be disabled for cross-platform determinism … using `-ffp-contract=off`"; "`atan2f` gives different answers on different platforms, so it is necessary to implement"; custom sin/cos kept although tests matched, because "I don't trust this result will hold"; "Determinism is a brittle feature. It is easy to break." — [box2d.org "Determinism" (2024-08)](https://box2d.org/posts/2024/08/determinism/). The old belief that fixed-point is required belonged to the 32-bit/x87 era, per the same post (as summarised in search results) — [same](https://box2d.org/posts/2024/08/determinism/).
- Box2D manual: "Cross-platform determinism is achieved on 64-bit platforms by using compiler flags and by avoiding non-deterministic library functions"; "Box2D determinism does not mean your application will be deterministic." — [Box2D simulation docs](https://box2d.org/documentation/md_simulation.html).
- Rapier: "By default, Rapier is locally deterministic" (same machine, same Rapier and compiler version); cross-platform needs `enhanced-determinism` (forces a software libm: `simba/libm_force` **[measured in Cargo.toml]**) and platforms that "strictly comply to the IEEE 754-2008 floating-points standard" — [rapier.rs determinism guide](https://rapier.rs/docs/user_guides/rust/determinism/); [dimforge/rapier](https://github.com/dimforge/rapier).
- Gaffer On Games (Glenn Fiedler, 2010-02-24; **STALE but canonical**): the answer to "is floating point deterministic across machines?" is "yes, if…" — same compiler/architecture/settings; it is "incredibly naive" to expect arbitrary float code to match across compilers and architectures; collected opinions: transcendental functions differed between AMD and Intel (Battlezone 2 wrapped sin/cos), replays stored as inputs "cannot be played back on machines with different CPU architectures, compilers, or optimization settings" (Shawn Hargreaves), physics APIs with variable iteration counts broke determinism (Gas Powered Games) — [gafferongames.com "Floating Point Determinism"](https://gafferongames.com/post/floating_point_determinism/).
- Java: JEP 306 "Restore Always-Strict Floating-Point Semantics", delivered in JDK 17, makes floating-point operations consistently strict; the separate default mode existed because of x87 and "the technical motivation … is no longer present" with SSE2; HotSpot already evaluated strictly — [JEP 306](https://openjdk.org/jeps/306); [JDK-8175916](https://bugs.openjdk.org/browse/JDK-8175916).
- Kotlin stdlib: the `kotlin.math.cos` reference tells users results may be inexact (compare with an epsilon) — [kotlinlang.org kotlin.math.cos](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.math/cos.html). Reports from Kotlin Slack: a test "passes on JVM and JS but fails on iOS" with `cos` off by one in the last digit — [Kotlin Slack archive](https://slack-chats.kotlinlang.org/t/528124/hi-everyone-i-get-different-results-using-cos-function-in-k-); JVM `sin` giving a different bit pattern on macOS, with `java.lang.Math` "may be implemented differently on different JVMs" — [Kotlin Slack archive](https://slack-chats.kotlinlang.org/t/29127581/i-filed-a-https-youtrack-jetbrains-com-issue-kt-78497-floati?t=v2).
- FMA contraction is toolchain-dependent in C/C++ ("clang defaults to -ffp-contract=off while GCC defaults to -ffp-contract=fast" in one GCC report; Apple clang 14 changed its default to on) and "extremely hard to predict", causing "inconsistencies between executables compiled with different toolchains or for different OSes" — [GCC bug discussion](https://gcc.gnu.org/pipermail/gcc-bugs/2022-June/789814.html); [LLVM discourse "FP contraction (FMA) on by default"](https://discourse.llvm.org/t/fp-contraction-fma-on-by-default/64975/4); [KDAB "FMA woes"](https://www.kdab.com/fma-woes/). In LLVM, contraction of separate multiply and add is only allowed when the front end emits `llvm.fmuladd` or the `contract` fast-math flag — [LLVM discourse RFC](https://discourse.llvm.org/t/rfc-c-a-b-vs-a-b-c-for-strict-mode/83745).
- Box2D v3.2 adds "Recording and replay, plus world snapshots (experimental)" with a state hash whose failure to reproduce is "a real determinism break" **[measured: `docs/recording.md`, release notes]** — [erincatto/box2d docs](https://github.com/erincatto/box2d/tree/main/docs); blog [box2d.org "Replay" (2026-06)](https://box2d.org/posts/2026/06/replay/).

### Inferences
- For a pure-Kotlin simulation in `core`: basic arithmetic and `sqrt` on `Float`/`Double` should be bit-identical on
  HotSpot (always strict, no implicit FMA under Java semantics) and Kotlin/Native on arm64/x86-64 **provided**
  Kotlin/Native's LLVM back end does not contract `a*b+c` into FMA. I found no JetBrains statement either way, so this
  must be tested, not assumed: a cheap safeguard is a golden-hash test (N steps of a fixed scene → hash of all body
  states) run both in the JVM tests and in the iOS simulator tests the template already runs on a Mac/CI.
- Trigonometry is the known divergence point (`kotlin.math.sin/cos/atan2` map to `java.lang.Math` on JVM/ART and to
  the platform libm on Native). A deterministic `core` engine should do what Box2D does: store rotations as
  (cos, sin) pairs, renormalise them, and use its own polynomial sin/cos/atan2 — a few dozen lines.
- Fixed-point (integer) arithmetic gives guaranteed cross-platform determinism in Kotlin (Int/Long semantics are
  fully specified) at the cost of range/precision management and slower, more awkward code; Box2D's and Rapier's
  experience says it is no longer necessary on 64-bit IEEE hardware.
- Does it matter for single-player ad-funded games? Mostly no. What is needed is **same-build determinism**
  (fixed timestep, no wall-clock or unordered-collection iteration in the step) so that tests, level-solution checks
  and "watch replay"/hint features reproduce on the device that recorded them — every option here provides that.
  **Cross-platform** determinism only matters for: (1) golden tests written on the JVM that should predict iOS/ART
  behaviour exactly; (2) shipping level solutions or "ghost" replays as input recordings that must replay identically
  on every device; (3) a daily challenge whose physics outcome must be identical for all players; (4) any
  server-side score verification. Without it, such features must store state snapshots instead of inputs, and
  physics tests must assert tolerances, not exact values.
- Native Box2D v3 gives cross-platform determinism "as a first class feature" on 64-bit, but only inside the engine:
  game logic around it in Kotlin still has to avoid platform-dependent math.
- Physics puzzle games where a level's solvability depends on chaotic stacking outcomes are the one single-player
  case where cross-device divergence becomes visible to players (a level solvable on one phone, not another); for
  those, either route A/B (Box2D determinism) or an own engine with custom trig is preferable to a JVM-libm-dependent
  port.

### Gaps
- No authoritative source on whether Kotlin/Native (LLVM) ever emits FMA for separate multiply and add, nor on ART's
  (Android Runtime's) floating-point strictness; JEP 306 formally covers Java SE/HotSpot. Marked unverified.
- JEP 306 page itself returned HTTP 403 to my fetch; statements are from the JEP's summary as shown in search results
  and the linked JBS issue.
- No test was run comparing kbox2d (pure Kotlin) outputs between JVM and iOS; whether the JBox2D port is
  cross-platform deterministic in practice is unknown (it uses `kotlin.math`/korlibs math for trig — not inspected).
- No source found that quantifies how often cross-device physics divergence causes user-visible problems in shipped
  single-player mobile games.
