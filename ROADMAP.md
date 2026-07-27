# Sandbox Roadmap

Progress tracker for turning the retro sandbox POC into a standalone game.
Check items as they land. Prefer finishing a phase before expanding scope.

**Legend:** `[ ]` todo · `[~]` in progress · `[x]` done · `[-]` deferred / won't do

---

## Phase 0 — Architecture foundations

Decouple rendering, add scenes, consolidate collision. Unlocks everything else.

### PixelBuffer / render decoupling
- [x] Add `PixelBuffer` (or `RenderContext`) with `drawSprite`, `drawRect`, `setPixel`, `clear`
- [x] Move pixel writes out of `GamePanel`; `GamePanel` only owns buffer + Swing blit
- [x] Change `GameObject.render(...)` to take `PixelBuffer` + `Camera` (not `GamePanel`)
- [x] Update `World` / `WorldMap` render signatures accordingly

### Scene / app lifecycle
- [x] Add `GameState` / `Scene` interface (`enter`, `update`, `render`, `onKey`)
- [x] Add `SceneManager` (or state machine) owned by `GamePanel`
- [x] `TitleScene` — starfield background, title text, Play / Quit
- [x] `PlayScene` — wraps existing `World` + player input
- [x] Pause overlay / pause menu (Esc → resume, quit to title)
- [x] Fade or simple transition between title ↔ play (optional polish)

### Collision consolidation
- [x] Single `CollisionSystem` used by `World` (retire duplicate logic)
- [x] Remove or absorb dead `CollisionChecker` into `CollisionSystem`
- [x] Fix entity–entity repulsion math (`delta / mag`, not `mag²`)
- [x] Fix double player render in `World.render()`
- [x] Spatial grid / uniform grid broad-phase (for 100+ entities)

### Camera & input cleanup
- [x] Camera follows a position/target, not hard-wired `Player` type
- [x] Cache `cameraX` / `cameraY` once per frame (`camera.update()`)
- [x] `InputManager` with key-state map (`isDown`, `wasPressed`)
- [x] Wire scenes through `InputManager` instead of only `PlayerController`

### Quick correctness / hygiene
- [x] Align package path with `package sandbox;` (`src/main/java/sandbox/…`)
- [x] Delete unused `Player.render(GamePanel)` center-screen path
- [x] Fix `CollisionChecker.constrainToBounds` height quirk if keeping that API

---

## Phase 1 — Standalone app shell

Feels like a game you launch, not a tech demo.

### Build & packaging
- [x] Add Gradle (or Maven) with `run` task
- [x] Fat / executable JAR
- [x] Update README with one-command run instructions
- [x] `jpackage` macOS `.app` (`./gradlew jpackageApp` → `build/jpackage/Sandbox.app`)
- [x] Load sprite assets from classpath (works inside packaged app)
- [ ] Optional: custom `.icns` app icon
- [ ] Optional: `--type dmg` installer image

### Title / menus / HUD
- [x] Title screen polish (logo/wordmark, menu selection highlight)
- [x] Settings scene stub (even if few options at first)
- [x] In-game HUD: HP bar
- [x] Game-over / return-to-title flow when HP hits 0

---

## Phase 2 — World variety

Wire existing tiles/plants; make exploration interesting.

### Map generation
- [x] Procedural `WorldMap` (noise / random walk) — grass, water, fire patches
- [ ] Enable / place DIRT, STONE, SAND sprites if art is ready
- [x] Viewport tile culling (only draw tiles in camera range)
- [x] Entity off-screen culling (skip render when outside viewport)

### Plants & static objects
- [x] Spawn trees and flowers (uncomment/adapt loop in `World`)
- [x] Non-overlapping placement via `CollisionSystem`
- [x] Use `Tile.opaque` (or equivalent) for solid tiles / tree trunks
- [x] Tile effects: water slows or blocks; fire damages on contact

### Layers & draw order
- [x] Replace `HashSet` with ordered lists / layers (`static`, `entities`, `particles`)
- [x] Sort by `y` for pseudo-depth where needed

---

## Phase 3 — Mechanics

Actual gameplay loop on top of the world.

### Combat & HP
- [x] Wire `Entity` HP: damage from fire / attacks
- [x] Player attack (e.g. Space) — short-range hitbox
- [x] Implement `PeacefulMobState.HURT` and `PANIC` behavior
- [x] Chicken death / despawn (optional drop)

### Interaction & inventory (light sandbox)
- [x] Interact key (e.g. E) with plants / chickens
- [x] Simple inventory (eggs, wood, flowers)
- [ ] Optional craft / place mode later

### Mob & AI quality
- [x] Separation so chickens don’t stack as badly
- [x] Real `StateMachine` for mobs — or simplify `Mob<State>` if unused
- [ ] More peaceful mob types (optional)

### Feedback
- [x] Use `particles.png` (hit sparks, splash, footsteps)
- [x] Cache `Sprite.brighter()` variants (no per-frame alloc)
- [ ] Optional sound (menu click, hit, ambient)

---

## Phase 4 — Customization

Player-facing options built on the palette system.

- [x] Settings: theme / palette presets (classic, night, autumn, …)
- [x] Player color variants (shirt / hair presets)
- [x] World options on New Game (map size, chicken density, seed)
- [x] Persist settings (`~/.sandbox-game/settings.properties`)
- [x] Remappable controls via `InputManager`

---

## Phase 5 — Polish & scale

Efficiency and feel once content exists.

- [x] Delta-time movement (don’t assume exact 60 FPS)
- [x] Rasterize sprites to `int[]` / `BufferedImage` at load; faster blit
- [x] Day/night cycle (reuse `StarField` at night)
- [ ] Save / load world (optional)
- [x] Objectives / soft goals (“collect 10 eggs”, explore lake)

### Scale / feel (post-roadmap)
- [x] 8×8 ground tiles; actors stay 16×16 (trees 32×32)
- [x] Map presets retuned to 32 / 64 / 96 for 8px grid

---

## Current focus

**Optional wrap-up** — save/load world, craft mode, sound, custom icon/DMG. Core roadmap Phases 0–5 are largely complete.

### Landed recently
- 8×8 tiles + 16×16 actors; map size migration
- Delta-time sim, ARGB sprite blit, day/night overlay, egg goal HUD

Update the checkboxes above as work lands. When a phase is done, set **Current focus** to the next phase.
