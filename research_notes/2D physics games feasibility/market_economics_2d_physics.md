# Market and unit economics of small ad-funded 2D physics / puzzle mobile games (as of 13 November 2026)

Reading notes for the report writer:

- "ST" = Sensor Tower, "GA" = GameAnalytics, "TC" = Testers Community (a commercial Google Play testing service that publishes crawl research).
- Vendor samples (Adjust, AppsFlyer, Appodeal, Tenjin, GA, Mistplay) cover apps that use their tooling: they are biased toward games with paid acquisition and at least some scale. GA's 2026 benchmark only includes games with at least 1,000 monthly active users, so a typical solo game is not even in the sample.
- Every number in the scenario section is an **illustrative estimate**, never an observation.
- Verification of the earlier 3D study's figures is collected at the end of the first section.

## 1. Genre size and performance: where physics games sit, and 2025–2026 data by genre

### Takeaway
Puzzle is the best place in the market for ad money (53% of mobile game ad revenue against 13% for arcade) and has better mid- and long-term retention than arcade or hypercasual, but the ad money sits in block, match-pair, sort and match-swap games bought with heavy paid acquisition; no 2025–2026 report sizes "physics puzzle" as a subgenre. Choosing 2D physics/puzzle over 3D arcade improves the *ceiling* of revenue per user, not the expected number of installs.

### Cited Findings

Taxonomy and size
- Store categories are chosen by the developer and "boundaries such as Casual versus Arcade are not policed sharply"; a physics game can be listed under Puzzle, Arcade, Casual or Simulation. — [TC, fastest-growing categories](https://www.testerscommunity.com/research/fastest-growing-categories)
- Industry taxonomies (Sensor Tower, AppMagic, Naavik) split puzzle into Match Swap, Match Merge, Match Pair, Block, Sort, Screw, Hidden Objects, Word; none of the 2025–2026 reports found has a "physics" subgenre. Screw puzzles (the one clearly physics-flavoured subgenre) are counted inside hybrid-casual puzzle. — [Naavik, niche puzzle subgenres](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/); [AppMagic H1'25 via GameDev Reports](https://gamedevreports.substack.com/p/appmagic-mobile-casual-games-in-h125)
- Puzzle in 2025: about 9.7B downloads and about $10B IAP revenue (+14% YoY) per Naavik; $14.4B per Sensor Tower's State of Gaming (different scope and gross/net basis — the two do not reconcile). — [Naavik](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/); [ST State of Gaming 2026 via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-state-of-gaming-2026)
- Puzzle subgenres 2025 (IAP only): Match Swap about $5.3B (+1%, 54% of genre); Match Merge 2 about $1.7B (+95%); Block + Sort + Screw combined about $0.7B (+222%, 7% of genre). Sort: about 1B downloads, about $280M (+176%). Screw: about 399M downloads (−12% YoY), over $206M (about double). Block: about $183M (about +2,900%), downloads +39%. — [Naavik](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/)
- H1 2026: puzzle player spending $4.9B, 44% of casual mobile earnings (AppMagic). — [PocketGamer.biz](https://www.pocketgamer.biz/puzzle-accounts-for-44-of-casual-mobile-earnings-in-h1-2026/)
- Hybrid-casual was the only segment with meaningful IAP growth in 2025 (+20% YoY); a secondary summary puts it at $4.2B with hybrid-casual puzzle as the standout. Hypercasual was the only segment with download growth (Sensor Tower slides: 22.05B downloads, +2.2%). — [ST State of Gaming 2026 via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-state-of-gaming-2026); [Cinevva summary (secondary)](https://app.cinevva.com/guides/casual-games-trends-2026)
- Hypercasual IAP revenue is under $0.5B in total against about $22B for casual (Azur Games publishing lead, April 2026; IAP only, excludes ads). — [PocketGamer.biz guest article](https://www.pocketgamer.biz/what-happened-to-hypercasual-the-markets-evolution-over-the-past-year/)

Ad revenue by genre
- Sensor Tower (June 2026): about $12B mobile game ad revenue and 2.4 trillion impressions across 19 countries; puzzle 53% of ad revenue, arcade 13%; by model, hypercasual about 40%, casual about 40%, hybrid-casual 16%; subgenres: Block 10%, Match Pair / Sort / Match Swap about 7% each. Method: panel of about 5M monthly users, revenue estimated from CPM benchmarks; the summariser notes the ad estimates fluctuate more than the IAP ones. — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)
- Games outside the top 1,000 take 29% of ad revenue but 9% of IAP; top 10 games take 11% of ad revenue (22% of IAP); top 50 take 26% (47% of IAP). — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)
- Top ad earners January–May 2026: Block Blast! $127M, Vita Mahjong $89.8M, Candy Crush Saga $81.1M, Solitaire Associations Journey $73.3M, Wordscapes $42.6M. None is a physics game. — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)
- Deconstructor of Fun treats $12B as a floor and estimates $16–20B for 2025 from operator experience (author's estimate); calls puzzle "the reddest of oceans". — [Deconstructor of Fun](https://www.deconstructoroffun.com/blog/5-numbers-hiding-in-plain-sight-sensor-towers-ad-monetization-report)
- Ads are about 55–70% of game revenue in India, Indonesia and Brazil, while IAP is 77–90% in the US, Canada, South Korea and Japan. — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)
- Within hybrid-casual, lifestyle/puzzle titles are reported to lean harder on ads (41% share) than action/strategy (18%) — secondary summary, not checked against the original. — [Cinevva summary (secondary)](https://app.cinevva.com/guides/casual-games-trends-2026)

Ad revenue per user, ARPDAU, eCPM by genre
- No 2025–2026 report found gives eCPM by genre. The only genre-level money figures are per-user: AppsFlyer (January 2025–March 2026, $7.2B of ad revenue in sample) D90 ad revenue per user: casual $0.55, casino $0.47, midcore $0.40, hypercasual $0.22. Biased toward attributed (mostly paid, higher-value) users. — [AppsFlyer via GameDev Reports](https://gamedevreports.substack.com/p/appsflyer-app-monetization-in-2026)
- 63% of a hypercasual user's D60 ad revenue arrives on day 1; across apps, 89% of D60 ad revenue has arrived by day 7. — [AppsFlyer via GameDev Reports](https://gamedevreports.substack.com/p/appsflyer-app-monetization-in-2026)
- Appodeal (over 10,000 casual games, US users, Android only, June 2024–January 2025 — stale and US-only): ad revenue per user $0.86 for hypercasual on average, $2.99 match, $2.34 running, $2.19 slicing, $4.90 party, $12.23 luck battle, $14.83 merge-3. Ads shown per user in the period: puzzle 72.5 interstitials, 23.4 rewarded videos, 241.5 banners; match 36.5 interstitials, 39.1 rewarded, 114.3 banners; merge-3 101.5 rewarded; idle 73.2 rewarded. — [App Developer Magazine on Appodeal](https://appdevelopermagazine.com/mobile-casual-benchmarks-report-2025/)
- ARPDAU ranges circulating in 2026 (weak provenance: a consultancy blog relayed by an ad platform, ranges not medians, sample undisclosed): hypercasual ad-driven $0.03–0.08 (H1 2025), casual ads-only $0.01–0.05, hybrid-casual ads + IAP $0.15–0.50 with IAP 40–60% of revenue. The same article says no published benchmark exists for impressions per daily user. — [Playio ARPDAU benchmarks](https://blog.playio.co/arpdau-benchmarks-mobile-games)

Retention, sessions
- GA 2026 benchmarks (16,262 mobile games with at least 1,000 MAU, 2025 data, all genres pooled): D7 median just under 4%, top quartile 6–7%, top 10% 11–12%; D30 median 0.69–0.79%, top quartile about 1.6–1.8%; D1 top quartile just above 30%, top 10% about 40%. Top-quartile session length about 5.2 min, sessions per day 5.3–5.7, daily playtime 22–24 min. D1 declined again in 2025. No genre cut in the 2026 edition. — [GA via GameDev Reports](https://gamedevreports.substack.com/p/gameanalytics-mobile-and-pc-game)
- Median D1 about 22% and D7 3.4–3.9% are repeated by secondary summaries of the same report. — [Gamigion summary](https://www.gamigion.com/?p=28884)
- GA's 2025 edition found board, card, puzzle and casino games the best genres for medium- and long-term retention (search-result summary, not re-read in full). — [GA 2025 via GameDev Reports](https://gamedevreports.substack.com/p/gameanalytics-mobile-gaming-benchmarks)
- Mistplay averages by genre (collection period and sample undisclosed; Mistplay is a play-to-earn loyalty app, so its users and games are far from an organic solo title): puzzle D1 31.85% / D7 12.18% / D30 5.35%; match 32.65 / 13.98 / 7.15; simulation 30.10 / 8.71 / 2.96; hypercasual 29.31 / 5.90 / 1.38. — [Segwise citing Mistplay](https://segwise.ai/blog/mobile-gaming-app-user-retention-strategies.md)
- US D30 retention by puzzle subgenre (Naavik, top titles): screw about 17%, sort about 8%, block about 4%. — [Naavik](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/)
- Adjust (top 5,000 apps, January 2024–January 2026): D1 across game genres 27% in 2025; hypercasual 29.1% of installs but 15% of sessions; puzzle 10% of installs and 13% of sessions (sessions share above install share, unlike hypercasual); day-zero sessions 1.65. — [Adjust via GameDev Reports](https://gamedevreports.substack.com/p/adjust-gaming-app-insights-report)
- Azur Games: successful hypercasual/hybrid titles now target D30 of at least 5% (up to 7.5%) and about 500 levels at soft launch; users retained at day 30 generate most of the revenue. — [PocketGamer.biz guest article](https://www.pocketgamer.biz/what-happened-to-hypercasual-the-markets-evolution-over-the-past-year/)
- Top hybrid-casual puzzle engagement: Pixel Flow! 52.5 minutes and 9.4 sessions per day. — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)

Do the earlier study's figures still hold?
- Downloads −7.2% in 2025: confirmed (50.41B; Google Play 42.4B, −7.3%; App Store 7.8B, −5.7%; Google Play 81% of installs). IAP revenue $81.75B (+1.3%). — [ST State of Gaming 2026 via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-state-of-gaming-2026)
- Downloads −12% to 24B in H1 2026: confirmed; IAP −2% to $40B; gaming ad spend +8% to $7B; puzzle led ad *spend* at 33%. — [PocketGamer.biz](https://www.pocketgamer.biz/mobile-gaming-iap-dipped-2-as-ad-spend-climbed-in-h1-2026/)
- $12B across 19 countries, 29% outside the top 1,000, puzzle 53%, arcade 13%: confirmed (above).
- 84.5% of 2026 Google Play releases under 1,000 installs: confirmed; snapshot 27 July–25 August 2026, so the releases are at most eight months old. — [TC, fastest-growing categories](https://www.testerscommunity.com/research/fastest-growing-categories)
- Median retention D7 under 4% and D30 under 1%: confirmed (above). ARPDAU $0.01–0.05 for ads-only casual: still the circulating figure, weak provenance (above).
- New supply: Sensor Tower counts over 150,000 game releases on Google Play in 2025 (+45.9%) and 55,300 on the App Store (+11.4%). — [ST State of Gaming 2026 via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-state-of-gaming-2026)
- CPI: Adjust global gaming CPI $0.56 (+30% YoY); North America $1.68 (+31%), Europe $0.53 (+47%), LATAM $0.14 (+40%). — [Adjust via GameDev Reports](https://gamedevreports.substack.com/p/adjust-gaming-app-insights-report)

### Inferences
- The "puzzle takes 53% of ad revenue" headline does not transfer to physics games: the named subgenres with ad share (block, match pair, sort, match swap) are grid/board logic games, and the top earners are evergreen board puzzles with years of content. A stacking, ragdoll or slingshot game is closer to arcade/hypercasual in retention profile unless it has hundreds of levels.
- The genre does change revenue per user in the right direction: puzzle users play more sessions per install and stay longer, and Appodeal's US data shows puzzle games serve many interstitials per user (72.5) — which implies many active days. The benefit is conditional on content depth (levels), which is cheap for sort/screw/pin/"draw" puzzles with generated levels and expensive for hand-built physics levels.
- AppsFlyer's "63% of hypercasual D60 ad revenue on day 1" means a short-lived physics toy must show ads in the first session to earn anything, which conflicts with store-rating and retention goals.

### Gaps
- No source sizes physics puzzle as a subgenre (downloads, ad revenue, retention) for 2025–2026. Liftoff, Unity/LevelPlay, AppLovin and Tenjin genre cuts were not found in accessible form.
- No genre-level eCPM, no rewarded-ad engagement rate for puzzle, and no published impressions-per-daily-user benchmark were found; the full Appodeal and Sensor Tower reports (not accessible) may have them.
- Newzoo's $121.1B 2026 forecast and the "+84% App Store submissions" figure were not re-checked.

## 2. Recent physics-based hits, 2021–2026, and what drove them

### Takeaway
Almost every physics or simple-2D-puzzle hit of the period was scaled by a publisher with paid acquisition, and the big ones now earn mostly from IAP, not ads. The few organic cases (Bacon, Suika, Smash Fest!) are real but rare, came from a social-video or streamer spark, and one of them shows that even a chart-topping physics game can monetise at about two cents per download.

### Cited Findings
- Screw puzzles: Screw Jam (Rollic) was the 2D predecessor of the wave; Screwdom (iKame) now holds about 45% of subgenre IAP revenue; the second title earns under $10M and most others under $5M a year; monetisation is IAP plus ads with pay-to-continue. — [Naavik](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/)
- Screw Sort Puzzle: $4.7M from 3.1M installs in the period covered by AppMagic's Q2 2025 top-10 hybrid-casual list (IAP only; search-result summary). — [AppMagic via GameDev Reports](https://gamedevreports.substack.com/p/appmagic-top-10-hybrid-casual-games)
- Sort: Magic Sort (Grand Games) about $50M (18% share), Hexa Sort (Lion Studios) nearly $20M, "a dozen-plus" others at $5–15M; Magic Sort bought Android installs at about $0.50 CPI in early 2025. Block: Color Block Jam (Rollic) about 75% of subgenre revenue. — [Naavik](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/)
- Pixel Flow! (launched August 2025): about $34M a month, of which IAP about $26M and ads $7–8M; 2.4B ad impressions in the US alone as an advertiser; developer acquired by Scopely in February 2026 at a reported $1B valuation. — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization); [ST State of Gaming 2026 via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-state-of-gaming-2026)
- Arrows Puzzle Escape (a minimal 2D puzzle, Lessmore GmbH, global launch July 2025) was the most-downloaded game worldwide in May 2026, with a look-alike (Arrows GO!) in fifth place; it was also the fourth-largest advertiser in games by impressions (4.99B, January–May 2026). — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)
- IAP-first hybrids earn about four times the revenue of the largest ad-first hybrids; three of the top five ad-focused games show no visible IAP. Gecko Out: 81–90% of revenue from IAP. — [Deconstructor of Fun](https://www.deconstructoroffun.com/blog/5-numbers-hiding-in-plain-sight-sensor-towers-ad-monetization-report); [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)
- Mob Control (Voodoo, physics crowd game): AppMagic IAP revenue about $0.2M (2021), $3.2M (2022), $20.1M (2023), $24.4M (2024), $22.0M (2025); over 21M downloads in Q1 2025. Revenue split reported as 85% ads / 15% IAP by an older PocketGamer figure, but 44% ads / 56% IAP in a 2026 tracker snapshot — the sources conflict (and likely reflect a shift toward IAP over time). — [Udonis on Mob Control](https://www.blog.udonis.co/mobile-marketing/mobile-games/mob-control); [maf.ad analysis](https://maf.ad/en/blog/mob-control-analysis-hybrid-casual/)
- Hole.io (Voodoo): 391.8M installs, estimated 55% ads / 45% IAP; record year for installs in 2025. Going Balls (Supersonic, ball-rolling physics): 428.3M installs, estimated 29% ads / 71% IAP. Tracker estimates, single snapshot. — [AppGoblin Hole.io](https://appgoblin.info/apps/io.voodoo.holeio); [AppGoblin Going Balls](https://appgoblin.info/apps/com.pronetis.ironball2); [PocketGamer.biz most downloaded 2025](https://www.pocketgamer.biz/the-most-downloaded-mobile-games-of-2025)
- "Save the Dog/Doge" draw-to-save physics puzzles circulated on TikTok and were then lifted into X-Hero's ads and onboarding; X-Hero (an idle battler) made almost $52M from 14.5M downloads by September 2022 with an estimated up to $100k a day of paid acquisition for 200–300k installs a day. Stale (2022), but it shows the physics puzzle earned for whoever bought the traffic, not for the puzzle's author. — [Mobilegamer.biz](https://mobilegamer.biz/x-hero-is-the-weirdest-hit-game-yet-earning-its-maker-52m-from-14m-downloads-to-date/)
- Suika Game (watermelon drop-merge): originated as a 2021 Chinese browser game; the Switch version passed 4M downloads by mid-November 2023 and 11M by October 2024; virality is credited to Japanese live streamers; the developer said sales rose "more than 50,000 times" (self-reported). It is a paid console title, not an ad-funded mobile one. — [Wikipedia](https://en.wikipedia.org/wiki/Suika_Game); [Automaton](https://automaton-media.com/en/news/20231005-22096/); [Shacknews](https://shacknews.com/article/137892/suika-game-nintendo-switch-4-million)
- Watermelon-style on mobile: Merge Fellas, a "merge watermelon-like" game, reached No. 14 in global downloads in May 2025 with downloads up 31,370% month on month; a typical small clone ("Watermelon Game – Merge Party") sits at about 203K installs. — [Insightrackr May 2025](https://blog.insightrackr.com/en/docs/2025-May-Top-Mobile-Games/); [AppGoblin](https://appgoblin.info/apps/com.unit5soft.suika.watermelon/trends)
- Smash Fest! (physics destruction, Flow Games, Istanbul, founded May 2025 by people from Good Job Games, Peak, Dream Games and Grand Games): top 3 on the US iOS free games chart from 15 June 2026; about 2.9M downloads in the first month (66.6% US), peak about 195,000 a day; revenue "just over $60,000" a month from IAP; "almost no" paid iOS campaigns detected. All figures are FoxData estimates, and FoxData sells the tool; the article's headline says "#1" but its body documents top 3. — [FoxData](https://foxdata.com/en/blogs/how-smash-fest-hit-1-on-the-us-app-store-with-zero-paid-uaand-what-it-means-for-hybrid-casual/)
- Bacon: The Game (physics, solo developer, released 2018): went viral on TikTok in mid-2022 after a dark-mode update; over 160k downloads in one day, 18.5M lifetime, all organic per the developer (self-reported); no revenue disclosed. Stale. — [Mobilegamer.biz](https://mobilegamer.biz/bacon-the-game-hits-18-5m-downloads-after-going-viral-on-tiktok/)
- New-launch success rates in casual puzzle (AppMagic H1 2025, threshold $100k monthly IAP revenue): 3 of 213 new match-3 launches (1.4%), 0 of 91 merge, 0 of 97 match-3D. — [AppMagic H1'25 via GameDev Reports](https://gamedevreports.substack.com/p/appmagic-mobile-casual-games-in-h125)

### Inferences
- Smash Fest!'s estimated $60k a month on about 2.9M downloads is roughly $0.02 of IAP per download (derived from the two FoxData estimates; ad revenue unknown). A physics toy with shallow progression monetises poorly even with millions of US installs.
- For an organic solo developer the pattern is: the mechanic is not the asset, distribution is. A new physics mechanic that clips well is copied within weeks by studios with acquisition budgets (Arrows GO!, 100+ Wool Sort copies, Save-the-Dog puzzles reused by X-Hero).
- The hits' revenue mix has moved to IAP; an ads-plus-remove-ads design takes the low end of the monetisation range by construction.

### Gaps
- No reliable install or revenue figures were found for Pull the Pin, Save the Doge, Brain It On!, Bridge Constructor, Cut the Rope, Stack/Tower games, Helix Jump or pinball titles for 2025–2026; tracker pages exist but were not accessible as citable figures.
- No evidence was found of a 2024–2026 solo, organic, ad-funded 2D physics game with disclosed revenue above trivial amounts.

## 3. Clone density and competition in physics-puzzle subgenres; how many reach 1,000 / 10,000 / 100,000 installs

### Takeaway
Puzzle is the most crowded and fastest-growing game category on Google Play (66,255 listings, about 40% of dated ones released in 2026) and 56% of puzzle listings never passed 1,000 installs — better than arcade (66%) but worse than casual (49%) and far worse than simulation (24%). The category choice shifts the odds by ten points or so; it does not change the fact that the typical new title gets a few hundred installs.

### Cited Findings
- TC crawl of 3,037,501 Google Play listings (312,795 games), observed 27 July–25 August 2026. By game category — listings / share of dated listings released in 2026 / share under 1,000 lifetime installs: Puzzle 66,255 / 39.9% / 56.0%; Arcade 29,094 / 39.2% / 66.0%; Casual 57,316 / 38.9% / 49.2%; Simulation 21,880 / 22.2% / 24.0%; Action 17,560 / 24.2% / 43.5%; Educational 18,938 / 17.8% / 51.7%; Board 12,231 / 28.4% / 50.3%; Role Playing 13,598 / 20.7% / 21.6%. Puzzle, arcade and casual are the three fastest-growing of all 40 store categories. — [TC, fastest-growing categories](https://www.testerscommunity.com/research/fastest-growing-categories)
- A sister page from the same crawl gives slightly different "launched in 2026" shares (puzzle 37.1%, casual 35.2%) — an internal inconsistency in the source; the under-1,000 shares agree. — [TC, is it worth building a game in 2026](https://www.testerscommunity.com/research/is-it-worth-building-a-game-in-2026)
- Games overall: 47.2% under 1,000 installs (other apps 64.5%); 8.2% of game listings have 1M+ installs (other apps 1.5%); 69.8% of games declare ads, 30.8% declare IAP, 4.3% are paid; 27.1% show a star rating; 31.8% of dated games launched in 2026. — [TC, is it worth building a game in 2026](https://www.testerscommunity.com/research/is-it-worth-building-a-game-in-2026)
- All listings, lifetime installs: under 100: 36.4%; 100–999: 26.3%; 1,000–9,999: 19.0%; 10,000–99,999: 11.1%; 100,000–999,999: 5.0%; 1M+: 2.2%. So 18.3% ever reach 10,000 and 7.2% reach 100,000 — across all ages of listing. Among 2026 releases, 84.5% are under 1,000 and 96.4% have no rating; about 2,100 new apps arrive per day. — [TC, average app installs](https://www.testerscommunity.com/research/average-app-installs); [TC, fastest-growing categories](https://www.testerscommunity.com/research/fastest-growing-categories)
- Caveats stated by the source: single snapshot; delisted apps are missing (survivorship flatters older cohorts); categories are self-declared; correlation only; installs are listing brackets, not revenue. — [TC, is it worth building a game in 2026](https://www.testerscommunity.com/research/is-it-worth-building-a-game-in-2026)
- Clone volume: over 2,500 hybrid-casual puzzle projects launched in H1 2025 alone — 376 screw puzzles and more than 2,000 sort and block games; more than 100 released projects copied Wool Sort's yarn idea. — [AppMagic H1'25 via GameDev Reports](https://gamedevreports.substack.com/p/appmagic-mobile-casual-games-in-h125)
- Concentration inside subgenres: Screwdom about 45% of screw revenue, Color Block Jam about 75% of block revenue, Match Factory! 56% of match-3D. — [Naavik](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/); [AppMagic H1'25 via GameDev Reports](https://gamedevreports.substack.com/p/appmagic-mobile-casual-games-in-h125)
- A 2026 solo post-mortem: GravityShot, a slingshot physics puzzle (Godot, 70 hand-made levels, two languages, months of evenings, zero marketing spend, ad-supported unlocks) had about 50 downloads and 10 cents of revenue at the time of writing (30 May 2026); the author found a similar game with the same mechanics and reported performance problems on cheap Android devices. Self-reported, one case. — [GameDō post-mortem](https://gamedo-live.pages.dev/news/gravityshot-postmortem-shipping-mobile-game-godot)
- Small clones do reach six figures occasionally: a "Save the Dog" clone at about 187.5K installs and a watermelon clone at about 203K (tracker estimates). — [AppGoblin Save The Dog clone](https://appgoblin.info/apps/com.savethedog.beevsdog.crazydoge); [AppGoblin watermelon clone](https://appgoblin.info/apps/com.unit5soft.suika.watermelon/trends)

### Inferences
- Listing a physics game under Casual or Simulation rather than Arcade or Puzzle puts it in a population with a lower share of sub-1,000 listings, but the simulation figure (24%) most likely reflects what is in that category (vehicle and life simulators with strong search demand, older surviving listings), not a causal benefit of the label.
- Combining the two TC cuts: a puzzle title released in 2026 faces a sub-1,000 probability somewhere between the category's all-ages 56% and the store-wide 84.5% for new releases — most likely near the upper end, because new releases have had under a year to accumulate installs. This is an inference; TC publishes no genre × release-year cut.
- Six-figure clone installs exist where a keyword is trending (watermelon, save the dog). Riding a search trend early is the one organic lever visible in the data, and it is time-limited.

### Gaps
- No crawl gives install distribution for physics puzzles specifically, nor for 2026 releases by game genre, nor counts of titles per mechanic (e.g. number of watermelon or pin-pull clones on the stores).
- No App Store equivalent of the TC crawl was found (Apple shows no install brackets).

## 4. Ad monetisation specifics in 2026

### Takeaway
Ad prices recovered in 2025–2026 and the feared Android identifier shutdown did not happen, but ad income is concentrated in the US (58% of iOS and 32% of Android game ad revenue) and falls by a factor of 5–15 in low-income countries, where most organic Android installs of an unmarketed game come from. For a tiny publisher, a realistic ads-only ARPDAU is around one to two cents; remove-ads purchases add little.

### Cited Findings
- eCPM by format (public figures disagree widely — US rewarded video ranges from about $13 to $30 and US interstitial from about $5 to $23 depending on source and period): Playwire 2025, Tier 1: rewarded $15–30, interstitial $5–8, banner $0.5–1.5; global: rewarded $8–18, interstitial $2.5–5, banner $0.2–0.8 (methodology undisclosed). — [Playio eCPM benchmarks 2026](https://blog.playio.co/mobile-game-ecpm-benchmarks-2026)
- Appodeal Q4 2024 (100k+ apps; chart readings, approximate; stale), rewarded iOS / Android: US about $17.3 / $13.2; North America $13.6 / $9.0; Europe $8.9 / $5.1; APAC $7.5 / $8.2; Middle East $8.4 / $2.4; Latin America $3.4 / $1.8. Interstitial iOS / Android: US $13.1 / $12.3; North America $10.4 / $9.6; Europe $5.3 / $3.6; APAC $5.3 / $5.5; Middle East $3.3 / $1.6; Latin America $2.2 / $1.3. Banner North America about $0.4–0.5. — [Playio eCPM benchmarks 2026](https://blog.playio.co/mobile-game-ecpm-benchmarks-2026)
- Tenjin × CAS Q2 2024 (games, stale): US rewarded $24.39 iOS / $30.25 Android; US interstitial $23.17 / $16.15. — [Playio eCPM benchmarks 2026](https://blog.playio.co/mobile-game-ecpm-benchmarks-2026)
- India: the only figure found is an unsourced glossary range of $0.30–1.00 for Android rewarded video (against $10–25 for US iOS); one guide puts a US impression at 5–15 times one from a low-income country. No Brazil or Indonesia country figure found. — [mwm.ai glossary](https://mwm.ai/glossary/ecpm); [MonetizeMore](https://www.monetizemore.com/blog/how-much-ad-revenue-can-apps-generate/)
- Trend: iOS eCPMs rose at least 8% in every format across five countries from Q1 to Q2 2026 and Android rose 2.55–13.94% (Bidlogic); Q4 runs 20–40% above Q1 (AdReact). Tenjin: iOS eCPMs fell 35% and Android 28% across 2022 after ATT, since recovered. — [Playio eCPM benchmarks 2026](https://blog.playio.co/mobile-game-ecpm-benchmarks-2026); [Deconstructor of Fun](https://www.deconstructoroffun.com/blog/5-numbers-hiding-in-plain-sight-sensor-towers-ad-monetization-report)
- Platform and country split of game ad revenue, Q2 2026 (Tenjin): Android 55%, iOS 45%; the US is 58% of iOS game ad revenue and 32% of Android; Japan 10% of iOS and 5% of Android. — [Playio eCPM benchmarks 2026](https://blog.playio.co/mobile-game-ecpm-benchmarks-2026); [Playio ARPDAU benchmarks](https://blog.playio.co/arpdau-benchmarks-mobile-games)
- Rewarded video can carry 30–100 times the price per impression of other formats (author's statement); non-gaming advertisers are 30–50% of impressions in every genre but 28% of ad revenue. — [Deconstructor of Fun](https://www.deconstructoroffun.com/blog/5-numbers-hiding-in-plain-sight-sensor-towers-ad-monetization-report)
- Networks: AppLovin (36%) and AdMob (29%) together carried 65% of mobile game ad revenue from January 2025 to May 2026; Unity announced in March 2026 that it would shut down the ironSource Ads network. — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization); [Game Developer](https://www.gamedeveloper.com/business/unity-sells-supersonic-publishing-biz-to-tripledot-for-40m)
- ATT: gaming opt-in 39% in Q1 2026 (38% a year earlier); arcade is the only genre that fell (50% → 46%); sports 54%, action 50%; India 44% → 51%, Indonesia 60%. Adjust sample (top 5,000 apps). — [Adjust via GameDev Reports](https://gamedevreports.substack.com/p/adjust-gaming-app-insights-report)
- Android privacy: Google announced on 17 October 2025 that it was retiring most of Privacy Sandbox (Topics, Protected Audience, Attribution Reporting) on Chrome and Android; the advertising ID stays, with Google's documentation said to promise substantial notice before any change. Source is an ad-platform blog relaying Google's pages. — [Playio on Privacy Sandbox](https://blog.playio.co/privacy-sandbox-shutdown-android-game-marketing)
- Remove-ads purchases: Breakout Ninja earned $23,000 from ads against $1,100 from a $1.99 ad-removal purchase — a 0.08% conversion (developer-reported; older case). A vendor blog claims 11% on iOS and 7% on Android without a source or price. Wordscapes moved from a single $10 remove-ads purchase to monthly purchases. — [Game Developer on Breakout Ninja](https://gamedeveloper.com/business/-i-breakout-ninja-i-dev-highlights-the-waning-appeal-of-ad-removal-iaps); [Fungies (vendor, unsourced)](https://fungies.io/how-to-effectively-monetize-your-player-base-in-mobile-games)
- Hybrid monetisation: in apps using ads, IAP and subscriptions (mostly games), ads fell from 63% to 56% of revenue between January 2025 and early 2026, IAP about 35%, subscriptions 4% → 7%; the share of games with ads rose from 45% (mid-2021) to 56% (May 2026); ad-supported games are 84.1% of game downloads. — [AppsFlyer via GameDev Reports](https://gamedevreports.substack.com/p/appsflyer-app-monetization-in-2026); [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization)
- Solo studios living on AdMob exist: Google's own (promotional, undated) case studies cite a solo logic-puzzle studio with 90% of revenue from AdMob and another whose puzzle game passed 115,000 iOS and 30,000 Android downloads with about 95% AI-generated code. — [Google AdMob story: Rottz Games](https://www.google.co.uk/ads/publisher/stories/rottz_games/); [Google AdMob story: Spark & Spiral](https://www.google.gr/ads/publisher/stories/spark_and_spiral_software/)

### Inferences
- Bottom-up ARPDAU check (illustrative, using Appodeal's Android regional eCPMs, 4 interstitials and 1 rewarded view per daily user, 100% fill): a geography mix of 10% North America / 25% Europe / 65% rest (Latin-America-level prices) gives about $0.014; an EU-heavy mix (5% / 60% / 35%) about $0.017; a rich mix (40% / 40% / 20%) about $0.028. This agrees with the circulating $0.01–0.05 range and puts a tiny organic publisher in its lower half.
- With the US at 58% of iOS game ad revenue, an EU-based developer whose organic installs skew to their own language market and to low-income Android countries will see materially lower revenue per install than vendor benchmarks, which are weighted to paid US users.
- Remove-ads revenue is a rounding item: at 0.1–0.5% conversion and about $2–3 net, it adds $2–13 per 1,000 installs.

### Gaps
- No evidence found on AdMob alone versus mediation (AppLovin MAX, LevelPlay) for publishers with a few hundred daily users, nor on a minimum scale at which mediation pays; a Singular article reportedly says MAX is ending waterfalls in favour of bidding with no clear revenue gain yet, but the date could not be confirmed.
- No 2026 measured country eCPMs for India, Brazil, Indonesia, the UK or Germany; no rewarded opt-in rate for puzzle games; no current benchmark for remove-ads conversion.
- EU consent (the share of EU users who decline personalised ads and the eCPM effect) was not researched.

## 5. Organic discovery in 2026 for small games

### Takeaway
Organic installs are the scarce input: the industry's median app now buys more than three installs for every organic one, instant-play and Amazon's Android store have closed, and no data was found that small indies get meaningful store featuring. Physics games do have one real edge — they produce short, loopable, "satisfying" clips — but the documented wins are a handful of outliers over eight years.

### Cited Findings
- Paid dominance: the median paid-to-organic install ratio in Adjust's sample rose from 2.07 to 3.33 (+61%) in 2025; Sensor Tower puts the paid traffic share at 50.6% for hypercasual and 28.8% for midcore; 79% of hypercasual payment revenue and 61% of casual comes from paid users (AppsFlyer). — [Adjust via GameDev Reports](https://gamedevreports.substack.com/p/adjust-gaming-app-insights-report); [ST State of Gaming 2026 via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-state-of-gaming-2026); [AppsFlyer via GameDev Reports](https://gamedevreports.substack.com/p/appsflyer-app-monetization-in-2026)
- New-app outcome on Google Play: the typical listing sits at 100–499 lifetime installs; 96.4% of 2026 releases have no rating. — [TC, average app installs](https://www.testerscommunity.com/research/average-app-installs)
- Social video: physics clips are named among the formats that work on TikTok/Reels/Shorts ("physics fails, emergent behavior, satisfying loops"); a perfectly looping clip of a satisfying mechanic gains from repeat plays being counted as views. Both sources are marketing guides, not data, and one argues TikTok drives awareness more than direct installs. — [FindClout](https://findclout.com/blog/how-to-market-an-indie-game-on-tiktok); [Presskit.gg](https://presskit.gg/field-guides/tiktok-indie-game-marketing)
- Documented organic physics cases: Bacon: The Game (2022, one TikTok with over 9M views, 160k downloads in a day, US No. 1); Smash Fest! (June 2026, about 2.9M downloads in a month with almost no detected paid campaigns — estimate by a tool vendor; team of ex-top-studio staff); Suika Game (streamers, 2023); Save-the-Dog puzzles spreading on TikTok (2022). — [Mobilegamer.biz on Bacon](https://mobilegamer.biz/bacon-the-game-hits-18-5m-downloads-after-going-viral-on-tiktok/); [FoxData](https://foxdata.com/en/blogs/how-smash-fest-hit-1-on-the-us-app-store-with-zero-paid-uaand-what-it-means-for-hybrid-casual/); [Wikipedia on Suika Game](https://en.wikipedia.org/wiki/Suika_Game); [Mobilegamer.biz on X-Hero](https://mobilegamer.biz/x-hero-is-the-weirdest-hit-game-yet-earning-its-maker-52m-from-14m-downloads-to-date/)
- Google Play Instant is gone: from December 2025 instant apps can no longer be published and the Play services Instant APIs no longer work. — [Android Developers](https://developer.android.com/topic/google-play-instant/getting-started/support-play-games-services?hl=en); [Android Authority](https://androidauthority.com/google-killing-android-instant-apps-3567211)
- Amazon closed its Appstore on Android devices on 20 August 2025; it continues on Fire tablets and Fire TV. — [9to5Google](https://9to5google.com/2025/02/20/amazon-appstore-android-shutting-down-2025/)
- Closed testing before launch is a real cost for new personal developer accounts: the GravityShot developer ran the required closed test through a paid/peer testing community before release. — [GameDō post-mortem](https://gamedo-live.pages.dev/news/gravityshot-postmortem-shipping-mobile-game-godot)

### Inferences
- The clip-friendliness of physics is the strongest argument for the 2D physics route over 3D arcade, because it is the only discovery channel in the evidence that does not cost money. It needs a person (or an agent with posting rights) producing clips steadily; none of the documented cases came from a store listing alone.
- With instant play and Amazon gone, "alternative distribution" for an Android game in 2026 means OEM stores and web portals; neither was evidenced here as a material source of installs for a new title.
- A hub app concentrates all organic trickle and ratings on one listing (helping it cross the rating-display threshold that 96.4% of new releases miss) but exposes fewer search keywords than several listings; no data was found either way.

### Gaps
- No data found on: Google Play's treatment of new apps (any launch boost), featuring odds for indies, Play Pass economics, Play Games on PC, Samsung Galaxy Store, Huawei AppGallery, Xiaomi GetApps, pre-install or instant-game platforms, Reddit/Discord effectiveness for mobile games, or cross-promotion inside a hub app. Apple Arcade's ineligibility for ad-funded games was taken from the brief, not verified.
- No measured conversion from short-video views to mobile installs was found.

## 6. Publishers and testing platforms open to solo developers

### Takeaway
Publishers still take prototypes from small teams but have moved from "cheap install, then scale" to engagement-first hybrid-casual puzzle with hundreds of levels and IAP; none publishes fixed pass marks or revenue-share terms, and none states its engine rules publicly. Their tooling and staff are Unity-oriented, so a Kotlin/Compose game should be assumed outside the publisher route unless a publisher confirms otherwise.

### Cited Findings
- Voodoo's publishing page lists a testing dashboard, a prototype phase and a "Voodoo Academy" for new or solo developers, but no thresholds, engine requirement or terms; its showcase puzzle hit is Block Jam 3D; Voodoo's 2025 revenue was reported at $778M. — [Voodoo publishing](https://voodoo.io/publishing); [Next Big Games](https://nextbiggames.com/2026/02/27/voodoo-2025-revenue-778m-growth/)
- Supersonic: Unity sold it to Tripledot Studios for about $40M (completed 4 August 2026, announced 6 August) after announcing the exit in March 2026; it continues as a standalone hybrid-casual publisher. Its site advertises "Unity hybrid-casual experts". — [PocketGamer.biz](https://pocketgamer.biz/tripledot-acquires-supersonic-from-unity-for-40m); [Game Developer](https://www.gamedeveloper.com/business/unity-sells-supersonic-publishing-biz-to-tripledot-for-40m); [Supersonic](https://supersonic.com/)
- Supersonic's 2026 guidance stresses playtime quality over download volume; one of its studios says it would drop a puzzle whose CPI topped $1 but that a higher CPI can still make a hit if retention and LTV are strong. — [Supersonic studio spotlight](https://supersonic.com/learn/blog/studio-spotlight-eogames/)
- CrazyLabs: tests CPI first, then D1 and 24-hour playtime, and says it has no fixed targets; its older "Publishing For All" programme set a CPI ceiling of $0.80; a 2024-era time-limited challenge used Facebook CPI at or below $0.20 and D1 of 32% and offered a 55% revenue share. Stale. — [PocketGamer.biz on CrazyLabs](https://www.pocketgamer.biz/comment-and-opinion/80564/a-deep-dive-into-crazylabs-transparency/); [PocketGamer.biz, Publishing For All](https://www.pocketgamer.biz/publishing-for-all-hypercasual-games-wanted)
- Kwalee: a CPI test is the first gate; a prototype should have at least 15 minutes of gameplay; a vertical slice follows a successful CPI test (undated guides). — [Kwalee guide](https://www.kwalee.com/blog/how-get-your-game-published-kwalee-guide); [Kwalee prototype tips](https://www.kwalee.com/blog/5-tips-making-attractive-game-prototypes-game-publishers)
- TapNation says it reviews D1–D30 retention, playtime and LTV rather than judging on D1 or CPI alone, on a profit-share model with no published percentage (from a search snippet; the page returned 404 when fetched). — [TapNation publishing](https://tap-nation.io/publishing/)
- Azur Games (April 2026): targets D30 of at least 5%, about 500 levels at soft launch, and wants player segmentation, behavioural analytics and an A/B testing culture from studios; about 70% of the top hypercasual chart launched or began scaling in 2025. — [PocketGamer.biz guest article](https://www.pocketgamer.biz/what-happened-to-hypercasual-the-markets-evolution-over-the-past-year/)
- A third-party kill-gate template for hypercasual soft launches uses CPI below $0.30 and D1 above 35% within 72 hours (vendor framing, not a publisher's threshold). — [Bruin](https://getbruin.com/use-cases/mobile-gaming/hypercasual-soft-launch-kill-decision-framework/)
- Engine: no publisher page found states "Unity only". The evidence is indirect — a developer who submitted a Flutter prototype to Voodoo got an automated reply referring to Unity and no answer on whether Flutter was acceptable (2020, anecdote); a 2026 Godot article notes that no mediation platform ships a first-party Godot SDK. — [Unity forum thread](https://forum.unity.com/threads/how-does-voodoos-prototyping-work.884068/); [Ziva on Godot mobile](https://ziva.sh/blogs/godot-mobile)
- Demand: publishers' recent hits and stated focus are hybrid-casual puzzles (block, sort, screw), which are 2D or 2.5D and physics-light; Rollic's Screw Jam and Color Block Jam and Voodoo's Block Jam 3D are the reference titles. — [Naavik](https://naavik.co/digest/how-niche-subgenres-are-reshaping-the-mobile-puzzle-market/); [Voodoo publishing](https://voodoo.io/publishing)

### Inferences
- A screw/sort/pin-style 2D puzzle is the kind of prototype publishers want in 2026; a ragdoll, pinball or stacking toy is not, unless it tests with an unusually low CPI.
- The bar a publisher applies (D30 ≥ 5%, about 500 levels, IAP economy, live events, A/B infrastructure) is roughly an order of magnitude more product than a "small game built by agents"; the publisher route is a different project, not an add-on to the portfolio plan.
- Because publisher test dashboards depend on the publisher's own SDK inside the build, and those stacks are distributed for Unity, a Kotlin/Compose game would most likely need a publisher willing to accept a native build with standard attribution and mediation SDKs. Background knowledge, not verified in this session: the major mediation and attribution SDKs do have native Android and iOS versions, so self-publishing with ads is unaffected.

### Gaps
- No current (2026) public KPI thresholds, revenue-share percentages or engine requirements were found for Voodoo, Supersonic, Homa, Kwalee, SayGames, TapNation or Rollic; these are disclosed after sign-up. Whether each requires Unity is unconfirmed and should be asked directly before any work is planned around a publisher.

## 7. Unit-economics inputs and illustrative scenarios (3 games; 1 hub app; 12 months; no paid acquisition)

### Takeaway
Illustrative only: three distinct 2D physics games gross about **$10 (pessimistic) / $180 (base) / $5,000 (optimistic)** in year one, and one hub app about **$4 / $125 / $7,600**. Per game, the base case ($60) is below the earlier 3D study's implied $140–290 per game, because the 2026 crawl data supports about 1,000 installs per title as a generous median; puzzle-like retention raises revenue per install by perhaps 25–100%, which does not offset low install counts.

### Cited Findings (inputs)
- Installs: typical Google Play listing 100–499 lifetime installs; 84.5% of 2026 releases under 1,000; puzzle listings of all ages 56.0% under 1,000 (arcade 66.0%, casual 49.2%); 18.3% of all listings ever reach 10,000. — [TC, average app installs](https://www.testerscommunity.com/research/average-app-installs); [TC, fastest-growing categories](https://www.testerscommunity.com/research/fastest-growing-categories)
- One observed solo physics-puzzle launch without marketing: about 50 downloads and $0.10. — [GameDō post-mortem](https://gamedo-live.pages.dev/news/gravityshot-postmortem-shipping-mobile-game-godot)
- Retention anchors: GA median D1 about 22%, D7 just under 4%, D30 0.69–0.79%; top quartile about 30% / 6–7% / 1.6–1.8%; top 10% about 40% / 11–12%. Mistplay puzzle average 31.85% / 12.18% / 5.35%. — [GA via GameDev Reports](https://gamedevreports.substack.com/p/gameanalytics-mobile-and-pc-game); [Gamigion summary](https://www.gamigion.com/?p=28884); [Segwise citing Mistplay](https://segwise.ai/blog/mobile-gaming-app-user-retention-strategies.md)
- ARPDAU anchors: ads-only casual $0.01–0.05; hypercasual $0.03–0.08 (weak provenance). — [Playio ARPDAU benchmarks](https://blog.playio.co/arpdau-benchmarks-mobile-games)
- Per-user cross-checks from vendor samples (paid-user-weighted): D90 ad revenue per user $0.22 hypercasual, $0.55 casual (AppsFlyer, global); $0.86 hypercasual (Appodeal, US Android). — [AppsFlyer via GameDev Reports](https://gamedevreports.substack.com/p/appsflyer-app-monetization-in-2026); [App Developer Magazine on Appodeal](https://appdevelopermagazine.com/mobile-casual-benchmarks-report-2025/)
- Remove-ads anchor: 0.08% conversion at $1.99 in one disclosed case. — [Game Developer on Breakout Ninja](https://gamedeveloper.com/business/-i-breakout-ninja-i-dev-highlights-the-waning-appeal-of-ad-removal-iaps)

### Inferences

All figures below are **illustrative estimates** computed by the researcher; none is an observation.

Formulae
- Retention curve: r(t) = D1 × t^(−b), with b = ln(D1 / D7) / ln 7, for day t ≥ 1.
- Lifetime active days per install over 12 months: LAD = 1 + Σ r(t) for t = 1…365 (the "1" is the install day).
- Ad revenue = installs × LAD × ARPDAU.
- Remove-ads revenue = installs × conversion × price × (1 − store fee). Assumed price $2.99 and store fee 15% (assumption, not verified in this research).
- Gross revenue = ad revenue + remove-ads revenue (before tax; ad networks pay net of their own cut).

Lifetime active days derived from the retention anchors (the fitted curves overshoot observed D30 slightly — fitted 0.98% against 0.69–0.79% observed for the median — so these are mild overestimates):

| Retention profile | D1 / D7 used | b | LAD over 365 days | LAD over 90 days |
|---|---|---|---|---|
| GA median (all genres) | 22% / 3.7% | 0.92 | 2.8 | 2.3 |
| Mistplay hypercasual average | 29.3% / 5.9% | 0.82 | 4.2 | 3.2 |
| GA top quartile | 30% / 6.5% | 0.79 | 4.7 | 3.4 |
| GA top 10% | 40% / 11.5% | 0.64 | 9.4 | 5.7 |
| Mistplay puzzle average (biased sample) | 31.85% / 12.18% | 0.49 | 13.0 | 6.7 |

Revenue per 1,000 installs (ads only) = 1,000 × LAD × ARPDAU:

| Case | LAD | ARPDAU | Per 1,000 installs | Per install |
|---|---|---|---|---|
| Weak (median retention, low-income geography) | 2.5 | $0.008 | $20 | $0.02 |
| Base for a competent 2D puzzle | 3.5 | $0.015 | $52.50 | $0.05 |
| Good (top-quartile retention, richer geography) | 5 | $0.03 | $150 | $0.15 |
| Ceiling for an established puzzle (not a new solo title) | 13 | $0.03 | $390 | $0.39 |

The base ($0.05 per install) sits inside the earlier study's $0.04–0.12; the 2D puzzle effect is the move from LAD 2.8 toward 3.5–5, i.e. roughly +25% to +80% revenue per install, if and only if the game has enough levels to be played on later days.

Scenario (a): three distinct 2D physics games, 12 months, no paid acquisition

| | Pessimistic | Base | Optimistic |
|---|---|---|---|
| Installs per game | 150, 150, 150 | 1,000, 1,000, 1,000 | 25,000, 3,000, 3,000 (one game catches a trend or a clip) |
| Total installs | 450 | 3,000 | 31,000 |
| LAD | 2.5 | 3.5 | 5 |
| ARPDAU | $0.008 | $0.015 | $0.03 |
| Ad revenue | 450 × 2.5 × 0.008 = $9 | 3,000 × 3.5 × 0.015 = $158 | 31,000 × 5 × 0.03 = $4,650 |
| Remove-ads conversion | 0.1% | 0.3% | 0.5% |
| Remove-ads revenue | 450 × 0.001 × 2.99 × 0.85 = $1 | 3,000 × 0.003 × 2.99 × 0.85 = $23 | 31,000 × 0.005 × 2.99 × 0.85 = $394 |
| **Gross, year one** | **about $10** | **about $180** | **about $5,000** |

Why these install levels: pessimistic is the median listing band (100–499) and above the one observed solo case (about 50); base is the 1,000-install line that 84.5% of 2026 releases had not crossed by August 2026, so "base" is already better than the median outcome; optimistic puts one title in the 10,000–99,999 band that 11.1% of all listings of any age occupy.

Scenario (b): one hub app holding several physics mini-games

| | Pessimistic | Base | Optimistic |
|---|---|---|---|
| Installs | 150 | 1,500 | 30,000 |
| LAD (assumed higher: more content per install) | 3 | 5 | 8 |
| ARPDAU | $0.008 | $0.015 | $0.03 |
| Ad revenue | 150 × 3 × 0.008 = $4 | 1,500 × 5 × 0.015 = $113 | 30,000 × 8 × 0.03 = $7,200 |
| Remove-ads revenue (0.1% / 0.3% / 0.5%) | $0.4 | $11 | $381 |
| **Gross, year one** | **about $4** | **about $125** | **about $7,600** |

Hub assumptions with no evidence behind them (see Gaps): one listing draws half the installs of three listings in the base case (fewer keywords and store entries), and variety lifts LAD from 3.5 to 5. If the hub drew as many installs as three separate games (3,000), base would be 3,000 × 5 × 0.015 + $23 = about $250.

Comparison with the earlier 3D base case ($1,400–2,900 gross for ten games)
- The earlier base equals $140–290 per game. Scaled to three games: $420–870.
- This note's base for three 2D games is about $180 ($60 per game), i.e. lower per game, because it assumes 1,000 installs per title on the strength of the 2026 crawl. Holding the earlier study's implied install level (about 3,000 per game — derived as $140–290 ÷ $0.04–0.12, not re-verified) and applying the 2D puzzle inputs gives 9,000 × 3.5 × 0.015 + 9,000 × 0.003 × 2.99 × 0.85 = about $540 for three games, or $180 per game — inside the earlier range.
- So on like-for-like install assumptions, going 2D physics/puzzle leaves gross revenue per game roughly unchanged to modestly higher (through LAD); it does not move the result out of the "tens to low hundreds of dollars per game" band. The difference between scenarios is almost entirely the install count, which genre choice barely affects.

Break-even arithmetic (illustrative)
- Installs needed = cash cost ÷ revenue per install. At the base $0.0525 per install: $1,300 needs about 25,000 installs; $3,000 needs about 57,000. At the good case ($0.15): about 8,700 and 20,000. Only 18.3% of all Google Play listings of any age have ever passed 10,000 installs, and far fewer do so in their first year.
- The lottery tail exists and is not modelled: at 1M installs and the good case, 1,000,000 × 5 × 0.03 = $150,000. Only 2.2% of all listings (8.2% of game listings, all ages, survivors only) have ever passed 1M installs, and the documented organic physics cases (Bacon, Smash Fest!) are a few titles among hundreds of thousands.

### Gaps
- No measured lifetime-active-days or ARPDAU figure exists for small organic ad-funded physics games; LAD is derived from pooled retention medians by curve fitting.
- No evidence on hub apps versus separate listings (installs, retention, ad yield); the hub scenario rests on assumptions.
- iOS is not separately modelled: no install-bracket data exists for the App Store, and iOS ad prices are higher (Europe rewarded about $8.9 against $5.1 on Android) but organic iOS volume for an unknown title is unknown.
- Costs were not re-derived; the earlier study's $1,300–3,000 was used as given.

## 8. Does 2D lower costs versus 3D in ways that change break-even?

### Takeaway
2D plausibly lowers art cost (free CC0 libraries, procedural shapes) and download size, and smaller downloads correlate with slightly better install conversion — but the best-known evidence is from 2018, the effect is small (about 1% per 6 MB), and one independent test failed to reproduce it. Cheaper production lowers the break-even install count; it does not create installs.

### Cited Findings
- Google Play (2018, internal data; stale): for apps under 100 MB, install conversion falls about 1% for every 6 MB of APK size; in emerging markets, removing 10 MB correlates with about a 2.5% conversion increase; the average APK downloaded in emerging markets is a quarter of the size of those in developed markets; for games of 500–3,000 MB, conversion rises only 1% per 200 MB removed. — [Google Play Apps & Games on Medium](https://medium.com/googleplaydev/shrinking-apks-growing-installs-5d3fcba23ce2)
- Counter-evidence: a developer who cut 42 MB saw a relative conversion change of −0.38% across more than 170,000 store listing visitors — no gain. — [Medium, "Reduced app size by 42MB — was it worth it?"](https://medium.com/@martinmazanec/shrinked-app-size-by-42mb-was-it-worth-it-ac34f76b14c7)
- Art: all Kenney game assets are public domain (CC0) and may be used in commercial projects. — [Kenney support](https://kenney.nl/support)
- Low-end devices: 2D does not make physics free — the GravityShot developer reported that performance "tanked on cheap Android devices" with several gravity sources active. — [GameDō post-mortem](https://gamedo-live.pages.dev/news/gravityshot-postmortem-shipping-mobile-game-godot)
- Where the low-end audience is, ad income is lowest: ads are 55–70% of game revenue in India, Indonesia and Brazil, and Latin American Android interstitial eCPM is about $1.3 against $9.6 in North America (Q4 2024). — [ST ad monetization via GameDev Reports](https://gamedevreports.substack.com/p/sensor-tower-mobile-game-ad-monetization); [Playio eCPM benchmarks 2026](https://blog.playio.co/mobile-game-ecpm-benchmarks-2026)
- Scope creep is the solo developer's real cost driver: the GravityShot author underestimated scope, cut a meta-progression layer and several mechanics, and still spent "months of evenings" on 70 levels. — [GameDō post-mortem](https://gamedo-live.pages.dev/news/gravityshot-postmortem-shipping-mobile-game-godot)

### Inferences
- Size effect in money terms (illustrative, using Google's 2018 coefficient): a 2D game 30 MB smaller than a 3D one would convert about 5% more store visitors — on 1,000 installs, about 50 more installs, worth about $2.60 at the base revenue per install. Not a break-even factor.
- Wider reach on low-end devices adds installs mainly in the countries with the lowest ad prices, so it raises install counts more than revenue.
- The cost advantage of 2D matters only through the denominator: if a 2D physics game costs half as much to build as a 3D one, the break-even install count halves — from tens of thousands to still more than ten thousand per portfolio, which remains above what the crawl data says a typical new title reaches.
- Content cost shifts rather than disappears: publishers' and benchmarks' retention levels assume hundreds of levels. Level generation and tuning for physics puzzles (solvability, difficulty curve) is the part least suited to quick automated production and the part retention depends on.

### Gaps
- No newer (2023–2026) study of install conversion versus download size was found; no data comparing build time or cost of 2D versus 3D small games; no evidence on the share of emerging-market devices that cannot run light 3D games.
