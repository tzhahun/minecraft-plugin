# CowLauncher (Paper plugin)

A Maven-based **Paper** server plugin with `/cowlaunch` and `/instabreak`.

## Runtime (what you need to run the plugin)

These run on the **machine that hosts the Paper server** (not on each player’s PC except the game client).


| Component                   | Purpose                                         | Version / notes                                                                                                                                                                                           |
| --------------------------- | ----------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Paper**                   | Server software that loads Bukkit/Paper plugins | Use a Paper build that matches your world and players. **Example:** Paper `26.1.2-61-main@8dea6f1` for Minecraft `26.1.2`. Check with `/version` on the server.                                           |
| **Java (JVM)**              | Runs Paper and all plugins                      | Follow the **Java version** required for your Paper build (see [Paper downloads](https://papermc.io/downloads/paper)). **Example:** Java `25.0.3`. Prefer the LTS Java Paper recommends for your version. |
| **This plugin’s JAR**       | Your compiled plugin                            | `**1.0.0`** (from `plugin.yml` / `pom.xml`). Place it in the server’s `plugins/` folder.                                                                                                                  |
| **Minecraft: Java Edition** | Client used to join and run commands            | Must be compatible with the server’s Minecraft version (e.g. `**26.1.2`** if the server is `26.1.2`).                                                                                                     |


**Not used:** Fabric, Fabric API, or mods in `mods/` — this is a **Paper plugin** only.

---

## Build-time (what you need to compile the plugin)

These run on **your development machine** when you run Maven.


| Component                 | Purpose                                                      | Version (this repo)                                                                                                                     |
| ------------------------- | ------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------- |
| **JDK**                   | Compiles Java sources                                        | **Java 17** language level (`maven.compiler.release` in `pom.xml`). Install a JDK **17 or newer** to build.                             |
| **Maven**                 | Build tool                                                   | **3.6+** recommended (e.g. 3.9.x).                                                                                                      |
| `**paper-api`**           | Compile-only Paper/Bukkit API (provided at runtime by Paper) | `**1.20.6-R0.1-SNAPSHOT**` (`paper.api.version` in `pom.xml`). For fewer surprises, align this with the Paper version you actually run. |
| **maven-compiler-plugin** | Compiles the project                                         | **3.13.0**                                                                                                                              |
| **maven-shade-plugin**    | Packages the JAR                                             | **3.6.0**                                                                                                                               |
| **JUnit**                 | Unit tests (not shipped in the plugin JAR)                   | **4.13.2** (`test` scope)                                                                                                               |


Repository for dependencies: **[https://repo.papermc.io/repository/maven-public/](https://repo.papermc.io/repository/maven-public/)**

---

## Getting started

### Prerequisites

See **Build-time** above (JDK + Maven).

### Building

```bash
mvn clean package
```

The plugin JAR is in the `target/` directory (e.g. `minecraft-plugin-1.0.0.jar`).

### Installation

1. Copy the JAR from `target/` into your Paper server’s `plugins/` folder.
2. Restart the server.
3. In-game (as OP or with permissions): `/cowlaunch`, `/instabreak`.

---

## Project structure

```
minecraft-plugin/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/cowlauncher/
│   │   └── resources/
│   │       └── plugin.yml
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

## License

This project is licensed under the MIT License — see the LICENSE file for details.

## How different compoenets interract with each other during run time

Paper starts the Minecraft server, then **loads your jar as a plugin** and calls into your code through the **Bukkit/Paper plugin API**.

### **1. How Paper discovers your plugin**

- Your jar contains `plugin.yml` at the root.
- On startup, Paper scans `plugins/`, reads `plugin.yml`, and finds:
  - `main:` → your `JavaPlugin` class (`CowLauncherPlugin`)
  - **commands** and **permissions** you declared

That’s how Paper knows your plugin exists and what commands to register.

### **2. How your code gets control (lifecycle)**

When your plugin loads, Paper constructs your main class and calls:

- `onEnable()` — your “startup” hook: register listeners, wire command executors, etc.
- `onDisable()` — your “shutdown” hook (cleanup if needed)

Your plugin runs **inside the server’s Java process**, on the server’s threads (mostly the main tick thread for gameplay events).

### **3. How** `/cowlaunch` **and** `/instabreak` **interact with the world**

Commands are **server-side**. When a player types a command:

1. The client sends it to the server.
2. Paper routes it to your `CommandExecutor` (your `CowLaunchCommand` / `InstaBreakCommand`).
3. Your code uses Bukkit/Paper objects like `Player`, `Entity`, `Vector`, etc.
4. You call `entity.setVelocity(...)` — that tells the server simulation to change entity motion, and Paper broadcasts the result to clients.

So interaction is: **player input → server → your plugin → server changes world state → clients see it**.

### **4. How instant break interacts**

Your `InstaBreakListener` registers for `BlockDamageEvent`:

- Paper fires that event when a player starts mining a block.
- Your listener calls `event.setInstaBreak(true)` (Paper/Bukkit API), which changes how the server treats that mining action for that tick/sequence.

Again: **server event → your code → you modify the event outcome → Paper applies block breaking rules**.

### **5. What Paper provides vs what you provide**

- **Paper provides**: world, entities, players, ticking, networking, events, command framework, permissions, scheduling APIs, etc.
- **Your plugin provides**: small pieces of logic registered to those hooks (commands + events).

### **6. Important mental model**

- Players **do not run your plugin** in their Minecraft install for server plugins.
- Only the **server** needs the jar in `plugins/` (for this kind of plugin).

If you want, paste your `CowLauncherPlugin.java` `onEnable()` and I can annotate it line-by-line against “Paper calls this / you call that.”