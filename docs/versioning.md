# Versioning (X.Y.Z)

Functional, milestone‑driven scheme. Numbers signal product evolution scale for publisher features.

```
X.Y.Z[-pre]
```
- X = Milestone (new capability pillar / major roadmap headline)
- Y = Feature Wave (meaningful user-facing additions inside existing pillars)
- Z = Maintenance (fixes, polish, safe internal / perf / UI tweaks)

## When to bump
- Bump X: New pillar, platform-wide capability, or large cohesive launch marketed externally.
- Bump Y: Net-new user value (screens, workflows, major UX improvements) without a new pillar.
- Bump Z: Only fixes, performance, visual tweaks, dependency/security patches, refactors.

Decision flow: Pillar? → X. Else user-facing feature(s)? → Y. Else → Z.

## Quick examples
| Change | Bump |
|--------|------|
| New Monetization module | X |
| Add advanced scheduling panel | Y |
| Bulk edit + inline rename (shipped together) | Y |
| Crash fix + spacing tweak | Z |
| List render perf optimization | Z |
| Offline caching layer (cross‑cutting) | X |

## Pre-releases (optional)
`-alpha.N` (incomplete) → `-beta.N` (feature complete) → `-rc.N` (stabilizing) → final.
Only increment if changes occurred since previous pre-release of same track.

## VersionCode (Android)
```
versionCode = 10000 // 1 Major, 00 Minor, 00 Patch
versionName = "1.0.0"

versionCode = 10000 // 1 Major, 00 Minor, 00 Patch
versionName = "1.0.0-rc.1"

versionCode = 10203 // 1 Major, 02 Minor, 03 Patch
versionName = "1.2.3"

versionCode = 20304 // 2 Major, 03 Minor, 04 Patch
versionName = "2.3.4"
```

## Release steps
1. Choose bump (rule above).
2. Update `versionName` / `versionCode` in `app/build.gradle.kts`.
3. Update `CHANGELOG.md` (sections: Added / Improved / Fixed / Maintenance / Security; add Milestone Highlights if X).
4. Commit: `chore(release): vX.Y.Z`.
5. Tag + push: `git tag -a vX.Y.Z -m "Release vX.Y.Z" && git push && git push --tags`.
6. CI artifacts & internal announcement.

## Anti-patterns
- Using Y for tiny tweaks (bundle them or use Z).
- Sneaking features into Z.
- Inflating X without a true milestone.

## Summary
```
X = Milestone | Y = Feature Wave | Z = Maintenance
```
Keep concise; update if roadmap philosophy changes.
