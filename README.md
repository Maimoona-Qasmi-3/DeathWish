# Deathwish — Phase 1

This is the small Java Swing foundation for Deathwish. It opens a window with a
fixed tile-grid test area and lets Max walk and jump on platforms. The shapes
are placeholders so the first phase can focus on the game structure.

## Run

From the project folder, compile and launch with JDK 17 or later:

```powershell
javac -d out src\deathwish\*.java
java -cp out deathwish.Main
```

Use **A/D** or the **left/right arrow keys** to move. Use **W**, **Up**, or
**Space** to jump.

## What this phase demonstrates

- `GameWindow` creates the Swing window.
- `GamePanel` runs a timer, reads keyboard input, and draws each frame.
- `World` stores platforms in a two-dimensional tile array.
- `Player` handles basic movement, gravity, jumping, and tile collision.

In the map, `#` means a solid platform and `.` means empty space.

The map is fixed for now. Seeded procedural generation, platform graphs, BFS,
enemies, and level transitions are later steps in the proposal.
