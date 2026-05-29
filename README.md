# Age of War — Java

A 2D real-time strategy game inspired by the classic "Age of War" flash game, built with Java Swing.

> **By** Romain Sebire & Joris Schwarz — IMT Mines Alès

---

## Description

Age of War is a 1v1 (player vs AI) strategy game where two bases fight across **3 eras** of evolution. Destroy the enemy base while defending your own.

### Game Mechanics

- **3 unit types**: Melee, Ranged, and Tank — each with unique stats (HP, damage, range, cost)
- **3 eras of evolution**: Evolve your civilization to unlock stronger units with improved visuals and stats
- **Economy system**: Earn gold by eliminating enemies to recruit troops or evolve
- **Enemy AI**: The opponent automatically spawns and sends units
- **Soundtrack**: Integrated background music
- **Full GUI**: Game board display, sprites, control buttons, real-time HUD

---

## Project Structure

```
src/age_of_war/
├── AgeOfWar.java       — Entry point (main)
├── Display.java        — GUI management (Swing JFrame, buttons, HUD)
├── GameBoard.java      — Core game logic (movement, combat, timers)
├── GameData.java       — XML data loader (unit/base stats)
├── Base.java           — Player/enemy base (HP, gold, rendering)
├── Unit.java           — Player unit (movement, combat, rendering)
├── EnemyUnit.java      — Enemy unit (reversed movement)
└── assets/             — Sprites (PNG) + background + soundtrack (WAV)
    ├── background.jpg
    ├── base_era{1-3}.png / base_era{1-3}_enemy.png
    ├── melee_era{1-3}.png / melee_era{1-3}_enemy.png
    ├── distance_era{1-3}.png / distance_era{1-3}_enemy.png
    ├── tank_era{1-3}.png / tank_era{1-3}_enemy.png
    └── soundtrack.wav

characters.xml          — Unit & base configuration data
```

### XML Data

All unit and base characteristics (HP, damage, range, cost, sprite dimensions) are stored in `characters.xml` and loaded dynamically at startup using the standard `javax.xml.parsers` DOM parser.

---

## Technologies

| Component | Technology |
|-----------|------------|
| Language | **Java** |
| GUI | **Java Swing** (JFrame, JPanel, JButton, JLabel) |
| Rendering | Custom `paintComponent()` with `Graphics.drawImage()` |
| Game data | **XML** parsed with `javax.xml.parsers` (DOM) |
| Audio | `javax.sound.sampled` (Clip, AudioInputStream) |
| Game loop | `javax.swing.Timer` |

---

## Prerequisites & Running

### Prerequisites

- **Java 17+** (uses switch expressions)

### Compile and run

```bash
# Compile
javac -d build src/age_of_war/*.java

# Run (from project root)
java -cp build age_of_war.AgeOfWar
```

---

## How to Play

1. Launch the game and click **"Start Game"**
2. Use the buttons to spawn units:
   - **Melee Warrior** — Close combat unit (cheap)
   - **Ranged Warrior** — Ranged unit (longer reach)
   - **Tank** — Heavy unit (high HP and damage, expensive)
3. Click **"Evolve"** to advance to the next era (upgrades all stats)
4. Destroy the enemy base to win!
5. **Replay** or **Quit** at game over

---

## Java Concepts Demonstrated

- Object-oriented programming (inheritance, encapsulation)
- GUI development with Java Swing
- Custom rendering (`paintComponent`, `Graphics`)
- Event handling (ActionListener, Timer)
- XML parsing with DOM
- Audio playback with `javax.sound.sampled`
- Resource loading from classpath
- Modular architecture (display / logic / data separation)

---

## License

Academic project — IMT Mines Alès.
