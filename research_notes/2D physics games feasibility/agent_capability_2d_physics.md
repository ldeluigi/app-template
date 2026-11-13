# Agent capability for 2D physics-based games (evidence as of 2026-11-13)

Conventions used below: **[M]** measured result (benchmark or controlled run), **[A]** anecdote or self-reported case,
**[E]** my own estimate (basis given). "Stale" marks evidence older than 2025. The 3D baseline from the earlier study
(one-shot three.js prototype $30–200, shipped game ≈$9,000 in tokens over five months, GameDevBench best 53.8%,
GameCraft-Bench best 41.46%) is used only for comparison and was supplied by the coordinator, not re-derived here.

## 1. Benchmarks: 2D versus 3D, physics-specific and genre breakdowns

### Takeaway
No benchmark shows 2D game work to be easier for agents than 3D: in GameDevBench, 2D graphics/animation tasks score
*lower* (33.0%) than 3D ones (38.4%), and the all-2D GameCraft-Bench tops out at 41.46%. What agents do well in every
benchmark is mechanics/gameplay logic (51–55%), which is where motion and collision code sits; what they do badly is
content depth, art and presentation. No benchmark has a dedicated physics category, and the one benchmark on writing
physics simulations from text (SimuScene) reports a best score of only 21.5%.

### Cited Findings
- **[M] GameDevBench (333 Godot 4 tasks; v1 2026-02-11, v2 2026-06-30).** Task mix: 2D graphics and animation 33.3%,
  3D graphics and animation 26.7%, user interface 20.1%, gameplay logic 19.8%. Success by category (averaged over
  agents): gameplay logic 51.4%, 3D graphics/animation 38.4%, **2D graphics/animation 33.0%**, UI 32.0%. There is no
  physics category; motion and collisions fall under gameplay logic, and the tests check collider interactions
  deterministically. — [arXiv 2602.11103 abstract](https://arxiv.org/abs/2602.11103);
  [full text v2](https://arxiv.org/html/2602.11103v2)
- **[M] GameDevBench per-agent pass@1 (no feedback → screenshot+video feedback):** gemini-cli + gemini-3-pro-preview
  50.1 → 53.8; codex + gpt-5.4 41.1 → 52.0; claude-code + claude-sonnet-4-5 28.8 → 34.8; codex + gpt-5.4-mini 36.9 →
  39.0 (video only: 43.2); gemini-cli + gemini-3-flash 45.4 → 44.1; claude-code + claude-haiku-4-5 13.8 → 16.5. Note
  the Claude models tested are 4.5-generation, i.e. old by November 2026. —
  [arXiv 2602.11103v2](https://arxiv.org/html/2602.11103v2)
- **[M] GameDevBench failure modes:** wrong sprite/animation frames chosen (e.g. walking frames instead of attack
  frames), nodes at the wrong scene-tree level, dropped signals, resources on the wrong node; weaker models collapse on
  scene and contextual editors (tilemap, animation). Cost: feedback raises cost, up to 3.3× for GPT-5.4; Gemini 3
  Flash in its native harness was the most cost-efficient. Dollar-per-task figures are in an appendix I could not
  retrieve. — [arXiv 2602.11103v2](https://arxiv.org/html/2602.11103v2)
- **[M] GameCraft-Bench (140 tasks, 15 game families, whole games in Godot, 2D only; 2026-06-16).** Overall and
  category scores (Mechanics / Depth / Visuals / Art): Claude Code + Opus-4.7 high **41.46** (55.34 / 39.48 / 42.78 /
  36.86); Codex + GPT-5.5 high 39.49 (54.36 / 38.61 / 41.84 / 32.94); Kimi Code + Kimi-K2.6 30.65; Claude Code +
  MiMo-V2.5-Pro 24.10; Code Buddy + GLM-5.1 18.29; Code Buddy + MiniMax-M2.7 10.95; Codex + DeepSeek-V4-Pro 2.15.
  Weights: Mechanics 0.15, Depth 0.35, Visuals 0.15, Art 0.35. —
  [arXiv 2606.17861](https://arxiv.org/html/2606.17861)
- **[M] GameCraft-Bench per family (Opus-4.7 / GPT-5.5 / Kimi-K2.6):** Platformer (19 tasks) 36.57 / 37.26 / 28.94;
  Puzzle (8) 35.87 / 46.80 / 25.42; Racing (4) 45.26 / 37.15 / 19.81; Sports (4) 48.24 / 49.71 / 41.61; Idle (4) 56.99
  / 64.91 / 41.65; Horror (5) 57.30 / 49.05 / 28.94; Card game (5) 33.78 / 25.50 / 18.48. There is no pinball or
  physics-puzzle family; platformer and racing are described as involving "continuous control and collision". The
  benchmark explicitly **excludes 3D, multiplayer and large-scale physics**. —
  [arXiv 2606.17861](https://arxiv.org/html/2606.17861)
- **[M] GameCraft-Bench headline diagnosis:** agents "often implement recognizable mechanics but struggle to deliver
  games with sufficient content, functional visual feedback, and coherent presentation". —
  [arXiv 2606.17861 abstract](https://arxiv.org/abs/2606.17861)
- **[M] OpenGame-Bench (150 prompts, 2D web games on Phaser 3, headless browser + VLM judge; v1 2026-04-20, v2
  2026-09-15).** Scores (Build Health / Visual Usability / Intent Alignment): Cursor + Claude Sonnet 4.6 66.8 / 61.4 /
  58.9; qwen-code + Claude Sonnet 4.6 63.2 / 54.3 / 57.8; bare Claude Sonnet 4.6 58.5 / 50.8 / 50.3; GPT-5.1 57.4 /
  52.9 / 49.4; Gemini 3.1 Pro 53.6 / 60.2 / 42.1; OpenGame framework + Claude Sonnet 4.6 **72.4 / 67.2 / 65.1**.
  Intent alignment by genre: platformers 76.8, top-down shooters 71.4, arcade 66.5, strategy 58.2, **puzzle/UI 52.6**
  (lowest). — [arXiv 2604.18394](https://arxiv.org/html/2604.18394)
- **[M] OpenGame ablations relevant to physics and practice:** removing "physics-first classification" (choosing the
  template by the physics regime, e.g. "falling without ground support" → platformer) drops intent alignment from 65.1
  to 61.6; a single static skeleton instead of five genre templates gives 65.4 build health / 56.3 intent alignment
  versus 72.4 / 65.1 with the full template library plus a living debug protocol; zero-shot build health is 58.4 and
  gains from debug iterations plateau near five rounds. Strong platformer results are attributed to correct binding
  of "collision layers, physics bodies, and velocity vectors"; "logic desynchronization" in arcade and strategy games
  fails silently, with no compiler warning or crash. — [arXiv 2604.18394](https://arxiv.org/html/2604.18394)
- **[M] GameASG-Bench (47 browser-native game-generation tasks, 12 genres, 2D and 3D; 2026-09-18).** Tasks declare a
  behavioural test interface *before* generation (starting scenarios, player actions, snapshots, invariants). Across
  nine agent stacks the best mean runtime-check pass rate is 93.2%, but the best *strict task success* is 55.3% (26 of
  47). Two harnesses each reached 18 strict successes yet only ten tasks succeeded under both. —
  [arXiv 2609.21293](https://arxiv.org/abs/2609.21293)
- **[M] SimuScene (physics-scenario animation code; 7,659 scenarios, 334 human-verified test items, 52 physics
  concepts in five domains; v1 2026-02-11, v2 2026-08-13).** Ten LLMs evaluated; "even the strongest model" reaches
  only **21.5% Avg@8** for code that both runs and visually matches the described physical scenario. —
  [arXiv 2602.10840](https://arxiv.org/abs/2602.10840)
- **[A] Bouncing-ball-in-a-rotating-polygon informal test (January 2025, possibly stale).** Reports were mixed: Claude
  3.5 Sonnet and Gemini 1.5 Pro let the ball escape the shape; Gemini 2.0 Flash Thinking and GPT-4o were reported to
  pass in one go; a researcher said writing the rotating-heptagon version by hand took him about two hours;
  commentators noted that small prompt changes alter the outcome, so it is not an empirical benchmark. —
  [TechCrunch, 2025-01-24](https://techcrunch.com/2025/01/24/people-are-benchmarking-ai-by-having-it-make-balls-bounce-in-rotating-shapes)
- **[A] Same test, April 2025:** a Reddit thread reported DeepSeek R1 and Gemini 2.5 Pro producing a working 20-ball
  heptagon in one request while Llama 4 Maverick failed in eight. Single-user reports. —
  [r/LocalLLaMA mirror](https://axebps-redlib.hf.space/r/LocalLLaMA/comments/1jsdq4p/llama_4_maverick_python_hexagon_test_failed/mlmuxer/?context=3)

### Inferences
- "2D is easier than 3D" is not supported by measurement. The 2D penalty in GameDevBench comes from sprite, tilemap and
  animation-frame work (asset selection, editor-specific resources), not from logic. A physics game drawn with
  procedural shapes on a canvas avoids exactly those task types, so the relevant benchmark number for the owner's plan
  is the gameplay-logic/mechanics figure (51–55% on older models), not the 2D-graphics figure. This is an inference:
  no benchmark isolates "procedural-shape 2D game without sprite assets".
- The category where agents are weakest in whole-game benchmarks (content depth and art, each weighted 0.35 in
  GameCraft-Bench) maps onto level count, difficulty curve and visual polish, which is the bulk of a 50–100-level
  physics puzzler.
- Puzzle genres score lowest or near-lowest (OpenGame 52.6; GameCraft Opus 35.87), platformers (collision-centric)
  score highest in OpenGame but below average in GameCraft: results per genre are not stable across benchmarks, and
  the GameCraft families have as few as four tasks.
- By 2025 frontier models could one-shot the bouncing-ball toy; SimuScene shows that matching a *specified* physical
  scenario is still mostly failed in 2026. Writing a plausible solver is easy; writing one that behaves as specified
  is not.
- All benchmark models are one or two generations behind what is on sale in November 2026 (pricing pages list Opus
  5.5, Sonnet 5.5, Fable 5.2; benchmarks used Opus 4.7, GPT-5.5, Sonnet 4.5/4.6). Absolute scores are likely
  understated for current models; the *ranking of categories* is the more durable finding.

### Gaps
- No benchmark reports a physics-specific category, physics-puzzle family, or stacking/merge/ragdoll/rope/pinball
  tasks. GameCraft-Bench excludes large-scale physics by design.
- GameDevBench and GameCraft-Bench dollar cost per task: not retrievable (GameDevBench's Appendix H.1 was cut off;
  GameCraft-Bench reports none).
- I found no benchmark named "V-GameGym", "WebGameBench", "PhysicsCodeBench", "PhysGame" or "Morpheus" with relevant
  results in this session; they were not confirmed to exist or not searched individually. "Mage" (arXiv 2605.07342,
  Unity scenes) and GVGAI-LLM (arXiv 2508.08501, playing rather than generating) surfaced but were not read.
- No per-model list for SimuScene (only the best score is in the abstract), and the language/framework of its
  generated simulations was not on the abstract page.

## 2. Kotlin versus JavaScript / Python / GDScript; Compose Multiplatform

### Takeaway
There is now a first-party Kotlin benchmark, and frontier agents resolve most of its real repository tasks (Claude
Code + Opus 4.7: 85.71%), so plain Kotlin is no longer a weak language for agents. But there is no like-for-like
Kotlin-versus-JavaScript measurement, nothing on Compose Multiplatform or Kotlin/Native, and every game-generation
benchmark and almost every public agent-built game uses JavaScript or Godot.

### Cited Findings
- **[M] JetBrains Kotlin Benchmark (105 repository-level tasks, SWE-bench methodology, published July 2026; rows dated
  March–May 2026).** Resolution rate: Claude Code + Opus 4.7 xhigh **85.71%** (90/105, avg 10.59M tokens per task);
  Junie + Opus 4.7 max 81.90%; Codex + GPT 5.5 xHigh 81.90% (38.78M tokens); Claude Code + Opus 4.6 max 80.00%; Codex
  + GPT 5.4 xHigh 77.14%; Claude Code + Opus 4.7 medium 76.19% (5.31M tokens); Claude Code + Sonnet 4.6 high 74.29%;
  Gemini CLI + Gemini 3.1 Pro 65.71%; Gemini CLI + Gemini 3 Flash 44.76%. Newer models are listed as "Evaluating…".
  No dollar cost is reported; no Compose or Kotlin Multiplatform breakdown. —
  [kotlinlang.org/benchmark](https://kotlinlang.org/benchmark/)
- **[M] Task sources:** eight open-source projects including ktlint, detekt, okhttp and Anki-Android, containerised;
  JetBrains says results are a first iteration and a second is in progress. —
  [third-party write-up, 2026-07-27](https://yage.ai/share/jetbrains-kotlin-benchmark-reframe-en-20260727.html);
  [JetBrains blog (403 for me, title only)](https://blog.jetbrains.com/kotlin/2026/07/introducing-the-kotlin-benchmark-evaluate-ai-coding-agents-on-real-world-kotlin-tasks)
- **[A/opinion] Kotlin's feedback loop for agents:** a commentator argues Kotlin gives instant static diagnostics but
  each end-to-end verification needs a heavy Gradle build, whereas Python has denser training data but slow runtime
  discovery of type errors. Presented as reasoning, not measurement. —
  [yage.ai](https://yage.ai/share/jetbrains-kotlin-benchmark-reframe-en-20260727.html)
- **[M, stale 2024] Kotlin ML Pack:** on HumanEval translated to Kotlin, model performance on Kotlin was worse than on
  Python "in most cases", attributed partly to training-data volume. —
  [arXiv 2405.19250](https://arxiv.org/pdf/2405.19250)
- **[A] Compose Multiplatform with agents:** a practitioner who wrote an agent skill for Compose reports the
  multiplatform side was harder than plain Jetpack Compose because each library recommendation needs a check that it
  publishes multiplatform targets. Single anecdote. —
  [Medium, "AI Skill for Compose & Compose Multiplatform"](https://meet-miyani.medium.com/ai-skill-for-compose-compose-multiplatform-5fb2e56368ae)
- **[A] Tooling signal:** several community skill packs exist for Kotlin Multiplatform + Compose with Claude Code and
  Codex (app architecture, not games). —
  [kmp-compose-multiplatform-skill](https://github.com/felipechaux/kmp-compose-multiplatform-skill);
  [kotlin-kmp-claude-agent-skills](https://github.com/mmiani/kotlin-kmp-claude-agent-skills)
- **[M] Harness matters as much as language:** GameDevBench's Gemini 3 Flash fell from 45.4% in its native CLI to
  30.3% in OpenHands; on the Kotlin benchmark the same model (Opus 4.7) scored 85.71% in Claude Code and 81.90% in
  Junie. — [arXiv 2602.11103v2](https://arxiv.org/html/2602.11103v2);
  [kotlinlang.org/benchmark](https://kotlinlang.org/benchmark/)
- **[A] No Kotlin game stack in the public record:** a curated list of agent-built games contains no entry using
  Kotlin, Compose, libGDX or KorGE; its 2D entries use HTML5 Canvas (18), Godot (5), PixiJS (3), Matter.js (2), Unity
  (2), Phaser, LÖVE and React (1 each). A targeted search for agent-built Kotlin/libGDX/KorGE/Compose Canvas games
  returned no first-hand report. —
  [awesome-ai-built-games](https://github.com/lappemic/awesome-ai-built-games)

### Inferences
- For the *pure Kotlin physics core in common code with JVM tests*, the Kotlin benchmark is the closest proxy
  (repository-level Kotlin with tests) and it says frontier agents are competent. A physics solver is self-contained,
  numeric and unit-testable, which is the favourable case.
- For *Compose Canvas rendering, touch input, frame loop and iOS (Kotlin/Native)*, there is no measurement and no
  public example. The risk is unquantified rather than shown to be high.
- Agents cannot see a Compose Canvas the way they can see a browser page: the JavaScript benchmarks all verify through
  a headless browser. The Kotlin equivalent in this template is drawing the screen in a Robolectric test (as
  `StoreArtTest` does) or driving an emulator; both are slower per iteration than a browser reload, and Gradle build
  time multiplies token-independent wall-clock time.
- The Kotlin benchmark's 5–39M tokens per task shows repository-level Kotlin work is token-heavy; see section 6 for
  what that implies in dollars.
- The JavaScript-in-WebView and Godot 2D alternatives are where all the measured evidence and public cases are; Godot
  2D is the only stack with two dedicated benchmarks, and both show the best agent below 55%.

### Gaps
- No source compares the same tasks in Kotlin and JavaScript/Python/GDScript for 2025–2026 models. I did not verify in
  this session whether SWE-bench Multilingual, Multi-SWE-bench or Aider polyglot include Kotlin; my recollection is
  that none of the three does, but this is unsourced.
- No evidence at all (benchmark or anecdote) on agents writing Compose Canvas games, on Compose Multiplatform code
  generation quality, or on Kotlin/Native-specific agent pitfalls (memory model, floating-point differences between
  JVM and Native, iOS frame pacing).
- Kotlin benchmark "avg. latency" values (e.g. "10h 37m") are ambiguous on the page (per task versus per run); I could
  not read the methodology page.

## 3. Public cases of agent-built 2D and physics games (2025–2026)

### Takeaway
Agent-built 2D browser games are now common and cheap in human time (a well-known developer reports at most four
hours of his own time per game), and at least one full physics pinball game and one Suika-style merge game were built
with Claude. But almost none report tokens, dollars, retention or revenue; none is in Kotlin; and I found no agent-built
physics game documented as shipped to the App Store or Google Play.

### Cited Findings
- **[A] Alien Escape Pinball (itch.io, AI Browser Game Jam 2; devlog dated "189 days ago", i.e. roughly May 2026 by my
  calculation).** A full physics pinball game on LittleJS + Box2D (WASM), built with Claude Code on a Max subscription
  using Opus. Claude wrote all game logic and custom Box2D parts (slingshots, drop targets, spinners, ramps, ball
  locks), an in-game table editor, an AI debug player that flips automatically ("not great" but useful), and tuned
  procedural sound effects. The human did table design, art compositing and **feel tuning: restitution, flipper
  torque, ramp curvature, slingshot kick angles, peg bounce**, with many tiny tuning commits (e.g. peg bounce 1.49 →
  1.491). Quotes: "The model can write the system; a human still has to sit there bouncing balls until it feels
  right." and "The polish tail is brutal." No hours, cost, plays or revenue given; the author had prior game
  development experience. Self-reported. —
  [Alien Pinball postmortem](https://focaccai.itch.io/alien-pinball/devlog/1517147/alien-pinball-postmortem-how-i-made-a-full-physics-pinball-game-with-ai-tools)
- **[A] Physics entries in a curated list of agent-built games:** a Suika-style fruit-merge game (Cursor + Claude,
  Matter.js, suika.live); a Matter.js platformer; a 2D gravity sandbox (Claude Code, HTML5 Canvas); a crane-sorting
  game in Godot (Claude + Codex); 3D ragdoll sword fighting and a drifting racer on Three.js + Rapier. No pinball,
  slingshot, marble-run or rope game is listed, and no build time or cost is given for any entry. —
  [awesome-ai-built-games](https://github.com/lappemic/awesome-ai-built-games)
- **[A] Simon Willison, "The October inflection point for vibe game development" (2026-10-28).** Four browser games
  (three three.js, one that began 2D); "Not a single project represents more than four hours of my work", plus many
  hours of agent runtime; one game was built from a single prompt in the chat app; another took "millions of tokens
  and nearly two days" under Claude Code with Opus 5.5 and he is "not convinced by the game mechanics yet". He does
  not read the code. His conclusion: the bottleneck is now original concepts and taste. He calls it "not an exhaustive
  study". No dollar figures. —
  [simonwillison.net](https://simonwillison.net/2026/Oct/28/vibe-game-development/)
- **[A] Battle Drones (same author, 2026-10-20).** Started in July 2026 as a **2D Phaser** prototype built by Claude
  Fable 5 ("barely a proof of concept"), still "not very fun" at v3; became fun after an overnight run prompted "Make
  it a LOT more fun", then was ported to three.js. Totals: 12 major iterations, **321 commits by coding agents**. No
  token, dollar or hour totals. — [simonwillison.net](https://simonwillison.net/2026/Oct/20/battledrones/)
- **[A, vendor] Flappy Bird in Unity 6.4, three prompts, 20-minute cap each (2026-08-04, n=1 per model).** List-price
  equivalent cost per build: Claude Opus 5 $14.73 (10/10 rubric, working game plus an unprompted test suite); Claude
  Sonnet 5 $7.34 (5/10; clean code but **unplayable: the ground was outside the camera's view**); DeepSeek v4 Flash
  $0.61 (9/10), and $0.18 for the same prompts in Godot. Published by a company selling the agent integration. —
  [ziva.sh](https://ziva.sh/blogs/claude-code-unity-benchmark)
- **[A] Non-programmer case:** a writer built a playable 3D browser shooter in about 10 days on a $20 subscription,
  total cost about $15 "rounding very generously", but rewrote all dialogue and hand-picked sounds (3D; baseline
  only). — [vlstudio.app summary, updated 2026-11-02](https://vlstudio.app/blog/vibe-coding-a-game-2026/)
- **[A, self-reported] Store outcomes for vibe-coded mobile apps (not games specifically):** one developer reports 30+
  iOS/Android apps built with Claude Code over nine months reaching 10,000+ downloads and $2,000+ total revenue. —
  [X post](https://x.com/anulagarwal/status/2032086604614103087)
- **[A] Market context:** itch.io new game pages per day rose from about 3,000 to regularly over 6,000 between
  2026-08-03 and 2026-10-30 (via PC Gamer); Steam users asked for a filter to hide AI-generated games during the
  October 2026 Next Fest. — [vlstudio.app](https://vlstudio.app/blog/vibe-coding-a-game-2026/)
- **[A, unconfirmed] Clone refusals:** Tom's Hardware reported that Claude began refusing to recreate specific
  existing games ("Don't build a recreation of a specific existing game, even if it is renamed or called original");
  the secondary source notes the text is unconfirmed and Anthropic had not commented. Relevant to Suika / Cut the Rope
  / Angry Birds-style briefs. — [vlstudio.app](https://vlstudio.app/blog/vibe-coding-a-game-2026/)
- **[A] Store rules that bite small agent-built games:** Apple guidelines 4.1 (copycats), 4.3 (spam) and 4.2 (lasting
  entertainment value). — [vlstudio.app](https://vlstudio.app/blog/vibe-coding-a-game-2026/)

### Inferences
- The one detailed physics post-mortem (pinball) splits the work exactly as the hypothesis predicts: the agent writes
  the systems, parts and tools; the human tunes constants by playing. The tuning tail, not the code, set the schedule.
- The Flappy Bird "ground outside the camera" failure and the GameDevBench scene-tree failures are the same class:
  code that is correct in isolation and wrong on screen. A canvas-drawn game still has this risk (off-screen bodies,
  wrong scale, wrong units).
- Human time of "≤4 hours per game" applies to personal browser toys by an expert prompter with no store release, no
  quality gates and no level set; it is a floor, not a typical figure for a shipped mobile game.
- Doubling of itch.io submissions in three months implies that a generic agent-built physics game faces a saturated
  market; the differentiator is concept and curation, which agents do not supply.

### Gaps
- No public case with tokens or dollars for a 2D physics prototype or a shipped 2D physics game. No retention or
  revenue figures for any agent-built physics game.
- No documented agent-built physics game on the App Store or Google Play; no agent-built game in Kotlin, Compose,
  KorGE, libGDX or LittleKt; no pygame, js13k, Ludum Dare or "Vibe Jam" post-mortem was retrieved in this session (not
  searched individually for lack of budget).
- A Medium survey ("Claude Code for Game Development") that reportedly says Claude "can verify correctness but not
  fun, feel, or visual fidelity" and advises treating physics-heavy titles as human-led returned HTTP 403; the claim is
  from a search snippet only — [Medium](https://chierhu.medium.com/claude-code-for-game-development-7a88fcd19992).

## 4. Is physics an advantage or a liability for agents?

### Takeaway
Both, in different phases. Physics is an advantage for the code (mechanics are the agents' strongest category, and a
general solver generates behaviour without authored animation or art) and for verification (a deterministic headless
simulation can be run thousands of times by the agent). It is a liability for feel tuning and for level content:
decades of research show physics-puzzle levels must be validated by simulation because static plausibility does not
imply solvability, and the public pinball case shows feel constants are set by a human playing.

### Cited Findings
- **For — mechanics are the strong suit.** Mechanics 55.34% versus Art 36.86% and Depth 39.48% (Opus 4.7,
  GameCraft-Bench); gameplay logic 51.4% versus 2D graphics 33.0% (GameDevBench). —
  [arXiv 2606.17861](https://arxiv.org/html/2606.17861); [arXiv 2602.11103v2](https://arxiv.org/html/2602.11103v2)
- **For — procedural content instead of assets.** In the pinball case all sound effects were generated procedurally
  with no audio files, and the art was derived from the collision geometry ("the physics is the prompt"). Battle
  Drones' soundtrack is generated entirely in code. —
  [Alien Pinball postmortem](https://focaccai.itch.io/alien-pinball/devlog/1517147/alien-pinball-postmortem-how-i-made-a-full-physics-pinball-game-with-ai-tools);
  [simonwillison.net](https://simonwillison.net/2026/Oct/20/battledrones/)
- **For — simulation lets the agent playtest itself.** Claude ran "over a thousand simulated playtest sessions" to
  rebalance Battle Drones: across 1,200 AI-versus-AI matches, undecided games after 25 minutes fell from 14% to 0.9%
  and comeback wins rose from 24% to 46%. (Strategy game, not physics, but the loop is a headless deterministic
  simulation.) — [simonwillison.net](https://simonwillison.net/2026/Oct/20/battledrones/)
- **Against — feel is tuned by a human.** Restitution, flipper torque, ramp curvature, kick angles and peg bounce
  were tuned by hand by playing; "a human still has to sit there bouncing balls until it feels right". —
  [Alien Pinball postmortem](https://focaccai.itch.io/alien-pinball/devlog/1517147/alien-pinball-postmortem-how-i-made-a-full-physics-pinball-game-with-ai-tools)
- **Against — determinism is fragile.** Battle Drones replays desynchronised because Chrome and Safari returned very
  slightly different `Math.sin()`/`Math.cos()` results; the fix was custom math functions. —
  [simonwillison.net](https://simonwillison.net/2026/Oct/20/battledrones/)
- **Against — specified physical behaviour is hard to hit.** Best model 21.5% on SimuScene. —
  [arXiv 2602.10840](https://arxiv.org/abs/2602.10840)
- **Against — LLM-authored puzzles "almost right" are wrong (2025-02-27).** Brilliant reports that its first
  LLM-generated balance ("mobile") puzzles compiled and were structurally sound but many were unsolvable on the
  placement grid or with the available snap points, and weights overlapped; changing the *representation* the model
  generates "dramatically improved the metrics"; the eval suite checks correctness, unique solvability, visual
  clarity, state consistency, impossible states and physical plausibility; pass rates reached about 80–90% for
  foundational problems; puzzles failing evals never reach human review, and humans still review the rest; a pool-shot
  puzzle that "never sinks" is cited as shipped-broken content. —
  [Brilliant blog](https://blog.brilliant.org/when-almost-right-is-catastrophically-wrong-evals-for-ai-learning-games/)
- **Level validation needs simulation (stale, pre-2025, but foundational).** An Angry Birds level generator validates
  each level in two steps: stability under Box2D, then solvability by executing a stored solution strategy
  (2021). Cut the Rope research frames playability checking as the core problem because continuous time and state
  make exhaustive search inefficient, and uses a simulation-based approach to evolve playable levels. —
  [arXiv 2106.01639](https://arxiv.org/pdf/2106.01639);
  [AIIDE, "Evolving Playable Content for Cut the Rope through a Simulation-Based Approach"](https://ojs.aaai.org/index.php/AIIDE/article/view/12690)
- **LLM level generation with solver checks (non-physics, 2026, via a survey; not read in full):** LLM + tree-search
  loops that author and playtest PuzzleScript games are reported, with solver-verified solvability as the acceptance
  test. — [AI for Games in the Foundation Model Era, arXiv 2609.16679](https://arxiv.org/html/2609.16679)
- **LLM physics-puzzle generation (2026, abstract only):** a multi-agent framework with solver and designer roles
  generates puzzles for CREATE, a 2D physics puzzle environment. Details and success rates not retrieved (paywall). —
  [Springer chapter](https://link.springer.com/chapter/10.1007/978-3-032-18011-7_1)

### Inferences
- A common-code Kotlin physics core is the best possible position for the "for" side: the same deterministic step
  function can run headless on the JVM in tests, in a solver, and in the app. The agent can then be given a
  machine-checkable acceptance test per level (stable at rest; solvable by a search or scripted bot within N actions;
  not solvable trivially), which is what the academic and Brilliant evidence says is necessary.
- Solvability is checkable; difficulty curve and "is it fun" are not. Expect the agent to produce many valid levels and
  the human to curate and order them.
- Genres differ in how much they need authored levels: stacking and merge/drop games are systemic (one tuned ruleset,
  no levels), so they sit on the favourable side; rope-cutting, bridge-building, slingshot and physics puzzlers need
  50–100 validated levels and sit on the unfavourable side; pinball and marble runs need a few hand-tuned tables. This
  ranking is my inference from the evidence above, not a measured result.
- Golden-value tests on chaotic simulations will be brittle across JVM and Kotlin/Native if floating-point
  transcendental functions differ, by analogy with the browser `Math.sin` case. Property tests (energy bounds, no
  tunnelling, bodies stay in bounds, settles within N steps) and fixed-step replays on one platform are safer. The
  JVM-versus-Native difference itself is unverified here.

### Gaps
- No 2024–2026 source found that has an LLM generate Angry Birds / Cut the Rope / bridge-building levels and validate
  them by physics simulation with reported pass rates. The AIBIRDS level-generation competition and "Ropossum" were
  not retrieved by name; the Cut the Rope and Angry Birds papers cited are pre-2025.
- No evidence on reinforcement-learning or search-based playtesting bots built *by coding agents* for physics games,
  beyond the pinball "AI debug player" anecdote.
- No source quantifies tuning effort (hours) for restitution/friction/damping, tunnelling or stacking jitter in an
  agent-written solver; nothing found on agents writing a stable rigid-body stacking solver from scratch versus
  porting Box2D.

## 5. Verification loops for 2D: screenshots, video, headless simulation, judges

### Takeaway
Visual feedback helps but modestly and unevenly (0 to +11 points in GameDevBench, with video more useful than
screenshots), behavioural checks declared up front catch far more than "it runs", and vision-model judges are stable
but somewhat generous and blind to content depth and art quality. No result isolates 2D physics.

### Cited Findings
- **[M] Multimodal feedback uplift (GameDevBench, pass@1):** GPT-5.4 +10.9 points (41.1 → 52.0); Claude Sonnet 4.5
  +6.0; Gemini 3 Pro +3.7; Claude Haiku 4.5 screenshot +1.8, video +4.8, both +2.7; GPT-5.4-mini screenshot +0.9,
  video +6.3, both +2.1; Gemini 3 Flash screenshot 0.0, video +1.5, both −1.3. The paper notes video alone often helps
  more than both combined. Feedback raised cost up to 3.3× (GPT-5.4). —
  [arXiv 2602.11103v2](https://arxiv.org/html/2602.11103v2)
- **[M] How much agents look (GameCraft-Bench):** rendered-screen inspections per task: Kimi-K2.6 21.41, Opus-4.7
  13.94, GPT-5.5 1.91; yet GPT-5.5 scored within two points of Opus-4.7 (39.49 versus 41.46). No controlled ablation;
  rendered feedback is said to help with layout and visibility on the basis of one case study. —
  [arXiv 2606.17861](https://arxiv.org/html/2606.17861)
- **[M] Judge design and reliability (GameCraft-Bench):** recorded input traces are replayed in a fresh Godot instance
  at 30 fps, 1280×720; frames sampled at 2 fps in 20-second windows; GPT-5.5 scores each rubric item 0–1. Rerun
  stability: standard deviation ≈0.004–0.005. Preliminary human calibration on three families: **human overall 22.69
  versus judge 26.02**; humans were stricter on content depth and art, the judge stricter on functional visuals. The
  authors call it a small calibration check, not an agreement study. —
  [arXiv 2606.17861](https://arxiv.org/html/2606.17861)
- **[M] "Runs" is far from "right":** GameASG-Bench's best mean runtime-check pass rate is 93.2% while best strict task
  success is 55.3%; OpenGame notes logic desynchronisation fails silently with no warning or crash. —
  [arXiv 2609.21293](https://arxiv.org/abs/2609.21293); [arXiv 2604.18394](https://arxiv.org/html/2604.18394)
- **[M] Process failure:** some agents built a launchable game but submitted no demo traces (five zero-score MiMo
  tasks), and tool-call count did not correlate with score (r = +0.016): more activity is not more verification. —
  [arXiv 2606.17861](https://arxiv.org/html/2606.17861)
- **[M] No human agreement study** for OpenGame-Bench's VLM judge (it adds a pixel-level heuristic for visual
  usability). — [arXiv 2604.18394](https://arxiv.org/html/2604.18394)
- **[A] Practices reported to work:** deterministic simulation plus recorded replays; thousands of headless simulated
  playtests for balance; agent-built debug viewers and contact sheets of generated maps; a written spec file; research
  and critique before building ("Only build after we have agreed on any modifications"). —
  [simonwillison.net, Battle Drones](https://simonwillison.net/2026/Oct/20/battledrones/);
  [simonwillison.net, Oct 28](https://simonwillison.net/2026/Oct/28/vibe-game-development/)
- **[A] Practices named by a practitioner summary for where vibe-coded games break:** written design spec, automated
  playthrough tests and human playtesting for long-session logic; profiling on real mid-range Android devices ("smooth
  on a new iPhone, stuttering on a mid-range Android"). The source says some rows reflect standard engineering
  practice rather than findings. — [vlstudio.app](https://vlstudio.app/blog/vibe-coding-a-game-2026/)
- **[Docs] Vendor guidance:** "Give verification targets: include test cases, paste screenshots, or define expected
  output… When Claude can verify its own work, it catches issues before you need to request fixes." —
  [Claude Code docs, costs](https://code.claude.com/docs/en/costs)

### Inferences
- For a physics game the highest-value verification is not visual: headless simulation tests in `core` (invariants,
  per-level solvability, fixed-seed replays) are deterministic, cheap and already fit the template's JVM-tested module.
  Screenshots (rendered in a Robolectric test) should be reserved for what simulation cannot see: scale, framing,
  overlap, off-screen bodies, text legibility.
- Video beat screenshots in GameDevBench; for physics, a short frame strip or a contact sheet of simulation states at
  fixed steps is the cheap analogue. This transfer is my inference.
- A vision judge that was about three points (≈15% relative) more generous than humans overall, and specifically more
  lenient on depth and art, should not be the acceptance gate for level quality or feel; use it for layout defects
  only.
- Declaring the behavioural test interface before generation (GameASG-Bench) is the benchmark analogue of writing the
  solver/invariant tests before the levels.

### Gaps
- No 2D-specific or physics-specific measurement of screenshot/video feedback, and no study of vision-model accuracy
  on typical 2D defects (overlap, off-screen objects, unreadable text) with numbers.
- No measurement of headless-simulation testing or deterministic replays as an agent verification method; evidence is
  anecdotal.
- No evidence on verifying Compose Canvas output by agents (Robolectric screenshots, emulator driving).

## 6. Costs: pricing, typical usage, and derived ranges

### Takeaway
Official figures put Claude Code at about $13 per developer per active day ($150–250 a month, under $30 a day for 90%
of users); subscriptions run $20, $100 and $200 a month, and the GitHub Action can run on a subscription. No public
source gives the token or dollar cost of a 2D physics prototype or a shipped 2D physics game, so the ranges below are
my estimates: roughly $15–150 (API-equivalent) for a playable prototype and roughly $1,000–6,000 (API-equivalent), or a
few months of a $100–200 subscription, for a small shipped game with 50–100 levels.

### Cited Findings
- **[Docs] Typical usage:** "the average cost is around $13 per developer per active day and $150-250 per developer
  per month, with costs remaining below $30 per active day for 90% of users" (enterprise deployments). Agent teams use
  about 7× more tokens than standard sessions when teammates run in plan mode. Extended thinking tokens are billed as
  output. — [Claude Code docs, costs](https://code.claude.com/docs/en/costs)
- **[Docs] Example session mix:** a Sonnet 4.6 session with 1.2k input, 5.3k output, 940.0k cache-read and 50.0k
  cache-write tokens cost $0.55, i.e. sessions are dominated by cache reads. —
  [Claude Code docs, costs](https://code.claude.com/docs/en/costs)
- **[Docs] Subscriptions (claude.com/pricing as fetched 2026-11-13):** Pro $20/month ($17 with annual billing),
  includes Claude Code; Max $100/month, 5× Pro usage; Ultra $200/month, 10× Pro usage weekly with no five-hour limit
  for verified accounts; Max and Ultra include $100 and $200 a month in API credits; Team seats $25 (Standard) and
  $125 (Premium) monthly. — [claude.com/pricing](https://claude.com/pricing)
- **[Docs] API prices per million tokens (input / output / cache read):** Claude Fable 5.2 $8 / $40 / $0.20; Claude
  Opus 5.5 $4 / $20 / $0.20; Claude Sonnet 5.5 $2 / $10 / $0.10; Claude Haiku 5.5 $0.10 / $0.50 / $0.01 (prompts up
  to 100k tokens); for comparison Opus 4.7 $5 / $25 / $0.50 and Sonnet 4.6 $3 / $15 / $0.30. —
  [Claude platform pricing](https://platform.claude.com/docs/en/about-claude/pricing)
- **[Docs] GitHub Action on a subscription: yes.** `CLAUDE_CODE_OAUTH_TOKEN` is "an OAuth token that authenticates
  with your Claude subscription, available on Pro, Max, Ultra, Team, and Enterprise plans", generated with `claude
  setup-token`; "If you authenticate with an OAuth token, runs use your Claude subscription instead of API billing."
  For secrets shared across an organisation's repositories the docs recommend an API key instead, because the token is
  tied to one person's subscription. Runs also consume GitHub Actions minutes. —
  [Claude Code GitHub Actions docs](https://code.claude.com/docs/en/github-actions)
- **[M] Token volume of repository-level Kotlin tasks:** average 5.31M tokens per task (Claude Code + Opus 4.7
  medium) to 10.59M (xhigh); Codex + GPT 5.5 xHigh 38.78M. No dollar figure. —
  [kotlinlang.org/benchmark](https://kotlinlang.org/benchmark/)
- **[A, vendor, n=1] A minimal 2D game (Flappy Bird, Unity, three prompts):** $7.34 (Sonnet 5) to $14.73 (Opus 5) at
  list price, about 30 minutes wall time; $0.18–0.61 with DeepSeek v4 Flash. —
  [ziva.sh](https://ziva.sh/blogs/claude-code-unity-benchmark)
- **[A] No dollar figures in the best-documented cases:** one game took "millions of tokens and nearly two days";
  another took 321 agent commits; the heavy mode "burns tokens like there's no tomorrow". —
  [simonwillison.net, Oct 28](https://simonwillison.net/2026/Oct/28/vibe-game-development/);
  [simonwillison.net, Oct 20](https://simonwillison.net/2026/Oct/20/battledrones/)
- **[A] Running cost of a small multiplayer relay:** estimated at "no more than $2.19/month" (not needed for a
  single-player physics game; shows hosting is not the cost driver). —
  [simonwillison.net](https://simonwillison.net/2026/Oct/20/battledrones/)

### Inferences
All figures in this subsection are **[E] estimates**; none is a reported cost of a physics game.

- **Blended price per million total tokens.** Applying the docs' example mix (≈94% cache read, ≈5% cache write, ≈0.5%
  output) to current list prices gives about $0.55 per million total tokens on Opus 5.5 (940k × $0.20 + 50k × $5 +
  5.3k × $20 ≈ $0.54) and about $1.1 on Fable 5.2. Real sessions with more output or cache misses cost more; treat
  $0.5–1.5 per million total tokens as the working band.
- **Cost per repository-level Kotlin task.** 5–11M tokens (Kotlin benchmark, Claude Code) × $0.5–1.5 ≈ **$3–15 per
  task**. The benchmark ran older models at their prices; this applies the token volumes to current prices.
- **(a) Playable 2D physics prototype** (one mechanic, a Kotlin solver in `core` with tests, Compose Canvas drawing,
  a handful of levels or a systemic ruleset; passes `./gradlew check`): **≈$15–150 API-equivalent**. Basis: lower end
  from the $7–15 minimal-2D-game data point and from one to two average active days ($13/day); upper end from
  20–100M tokens at the blended rate (about five to fifteen Kotlin-benchmark-sized tasks) and from three to five
  days at the 90th-percentile rate (<$30/day); bracketed above by the 3D baseline of $30–200. On a subscription this
  fits inside one month of Max ($100) and plausibly of Pro ($20) for a small scope; subscription limits, not dollars,
  become the constraint.
- **(b) Small shipped 2D physics game with about 50–100 levels**: **≈$1,000–6,000 API-equivalent**, or **≈$300–1,000**
  if done on a $100–200 subscription over three to five months and the limits suffice. Basis: 300–600 agent tasks
  (anchored on Battle Drones' 321 agent commits for a polished non-store game, doubled for store release, quality
  gates, two platforms and level tooling) × $3–10 per task; upper bound stays below the 3D baseline of ≈$9,000 because
  there is no 3D asset and animation pipeline. Level generation and validation by simulation costs compute, not
  tokens, once the generator and solver exist; tokens are spent on tools, not on each level.
- Heavy autonomous modes (overnight runs, agent teams at about 7× tokens) can exceed these ranges several-fold; the
  ranges assume ordinary supervised sessions and CI runs.
- Kotlin adds wall-clock cost rather than token cost: each verification is a Gradle build/test run of minutes
  (`./gradlew check` "takes a few minutes" per the template's instructions), versus seconds for a browser reload.
  This is my inference from the repository's own documentation and the commentary in section 2.

### Gaps
- No reported token or dollar cost for any agent-built 2D physics prototype or shipped 2D physics game; no reported
  cost for any Kotlin game. The ranges above therefore cannot be validated against a real case.
- GameDevBench and GameCraft-Bench per-task dollar costs were not retrievable.
- Codex, Cursor and Gemini CLI pricing and typical spend were not researched in this session.
- Whether Pro/Max/Ultra usage limits are sufficient for months of daily agent work on a Kotlin project is not
  documented in numbers (the plans state multiples of Pro usage, not token allowances).

## 7. Human time and what humans still must do

### Takeaway
Reported human time for an agent-built browser game prototype is hours (at most four hours of the author's own time
per game in the best-documented 2026 case, alongside many more hours of unattended agent runtime); there is no
reported figure for a shipped agent-built 2D physics game. Every source assigns the same residue to the human:
concept and taste, feel tuning by playing, playtesting on real devices, content/level curation, and release.

### Cited Findings
- **[A] Hours per prototype:** "Not a single project represents more than four hours of my work" across four games;
  agent runtime was far longer (one run 3 hours 7 minutes in chat; others overnight or "nearly two days"). —
  [simonwillison.net, Oct 28](https://simonwillison.net/2026/Oct/28/vibe-game-development/);
  [search summary of the same post](https://simonwillison.net/2026/Oct/28/vibe-game-development/)
- **[A] Iteration scale for a polished game:** 12 major iterations over about three months (July to October 2026),
  321 agent commits; fun arrived only at the fourth version. —
  [simonwillison.net, Oct 20](https://simonwillison.net/2026/Oct/20/battledrones/)
- **[A] Non-programmer, 3D (baseline):** about 10 days for a playable browser shooter. —
  [vlstudio.app](https://vlstudio.app/blog/vibe-coding-a-game-2026/)
- **[A] What the human did in the physics pinball case:** table design, feel tuning by playing, directing an in-game
  editor, art selection and compositing, sound passes, final design decisions; "The polish tail is brutal… Budget for
  it." About half the input was speech-to-text. —
  [Alien Pinball postmortem](https://focaccai.itch.io/alien-pinball/devlog/1517147/alien-pinball-postmortem-how-i-made-a-full-physics-pinball-game-with-ai-tools)
- **[A] Playtesting catches what agents cannot:** a quest character left in the wrong place by a story event; an
  audio feature that gave a player a headache; game mechanics the author is "not convinced by" despite two days of
  agent work. — [simonwillison.net, Oct 28](https://simonwillison.net/2026/Oct/28/vibe-game-development/);
  [simonwillison.net, Oct 20](https://simonwillison.net/2026/Oct/20/battledrones/)
- **[A] Concept and taste are the bottleneck:** the same author's central point is that original concepts and taste,
  not technical skill, now limit game making; vague prompts ("Make it a LOT more fun") worked early, later features
  needed specific design input. —
  [simonwillison.net, Oct 28](https://simonwillison.net/2026/Oct/28/vibe-game-development/)
- **[M] Content is the measured weak point:** humans scored agent-built games lower than the vision judge on content
  depth and art (22.69 versus 26.02 overall). — [arXiv 2606.17861](https://arxiv.org/html/2606.17861)
- **[A] Human review of generated puzzles remains:** at Brilliant, automated evals filter generated puzzles (80–90%
  pass on foundational problems) and human experts still review what passes. —
  [Brilliant blog](https://blog.brilliant.org/when-almost-right-is-catastrophically-wrong-evals-for-ai-learning-games/)
- **[A] Device and store work:** performance differs between a new iPhone and a mid-range Android; store review rules
  on copycats, spam and lasting value apply. — [vlstudio.app](https://vlstudio.app/blog/vibe-coding-a-game-2026/)

### Inferences
- **[E] Human time per playable 2D physics prototype in this template: about 4–15 hours.** Basis: the four-hour
  browser figure as a floor, plus the overhead that case did not have (Kotlin/Gradle loop, two platforms, quality
  gates, feel tuning on a device). No direct evidence.
- **[E] Human time per small shipped game with 50–100 levels: on the order of 60–200 hours** spread over two to five
  months, dominated by feel tuning, playing and ordering levels, device testing and store release rather than code
  review. Basis: the three-month, twelve-iteration arc of the best-documented polished game, the "brutal" polish tail
  in the pinball case, and the 3D baseline of five months. This is a rough estimate; no source reports hours for a
  shipped agent-built game.
- Human tasks that remain, ranked by how little the evidence suggests agents can take over: (1) choosing an original
  concept; (2) feel tuning by playing on a phone; (3) judging difficulty and ordering levels; (4) art direction and
  visual identity; (5) device performance testing; (6) store listing and release decisions (which this repository's
  rules reserve for the owner in any case). Agents can take over: the solver, level generator, solvability checker,
  editor and debug tools, tests, procedural shapes and sounds.
- Building tools rather than content is the pattern that worked in the physics case (the in-game table editor, the
  debug player): the human's hours go further when the agent first builds a tuning panel and a level
  generator/validator.

### Gaps
- No reported human-hours figure for any shipped agent-built 2D game, physics or otherwise; no time-and-motion data
  on feel tuning.
- No evidence on how many generated-and-validated levels a human must review to keep 50–100 good ones.
- Game-jam post-mortems (js13k, Ludum Dare, Vibe Jam) with hours were not retrieved in this session.
