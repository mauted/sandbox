![image](https://github.com/mauted/sandbox/assets/63406974/c762e56e-fa14-4861-af6b-27abacaddd21)

A proof-of-concept, fun project to emulate retro-style video game graphics using limited color palettes and screen resolution.

See [ROADMAP.md](ROADMAP.md) for architecture and feature progress.

## Run as a macOS app (no terminal)

Build a double-clickable `Sandbox.app` (bundles its own Java runtime):

```bash
./gradlew jpackageApp
```

Then open it:

```bash
open build/jpackage/Sandbox.app
```

Or Finder: go to `build/jpackage/` and double-click **Sandbox**. You can drag it to Applications or the Dock.

Shortcut that builds and opens:

```bash
./gradlew openApp
```

Requires JDK 17+ once (for building). Prefer an **Apple Silicon (aarch64)** JDK so `jpackage` produces a native arm64 app — otherwise macOS may warn that an Intel build will stop working later. The finished `.app` does **not** need a JDK on the machine that runs it.

### If macOS says the app is “damaged”

That’s Gatekeeper blocking an unsigned local build (not actual corruption). Rebuild so the project signs it automatically:

```bash
./gradlew jpackageApp
```

Or fix an existing app:

```bash
chmod -R u+w build/jpackage/Sandbox.app
xattr -cr build/jpackage/Sandbox.app
codesign --force --deep --sign - build/jpackage/Sandbox.app
```

Then open it again (right-click → **Open** the first time if prompted).

## Dev run (Gradle)

```bash
./gradlew run
```

## Controls

| Screen    | Keys |
|-----------|------|
| Title     | ↑/↓ or W/S select · Enter/Space confirm |
| New Game  | Map size / chicks / seed · Start |
| Settings  | Theme, skin, hitboxes · Controls remap · Esc back |
| Play      | Move / attack / interact / pause (remappable; arrows still work for move) |
| Pause     | ↑/↓ select · Enter confirm · Esc resume |
| Game Over | ↑/↓ select · Enter (Retry / Title) |

Chickens peck on contact; Space scares/kills them (eggs drop). E picks flowers, chops trees (wood), grabs drops. Water blocks; fire burns. Inventory shows as `E# W# F#` under the HP bar.

**Scale:** ground tiles are **8×8**; player/chickens are **16×16** (about 2 tiles tall). Day/night cycles in-world (~2 min); soft goal is **10 eggs**.
