# Entity Launcher and Instant blocks breaking (Paper plugin)

A Maven-based **Paper** server plugin with `/cowlaunch` - launches nearby entities upward and `/instabreak` - breaks blocks instantly.

Built as a learning project: GitHub → Maven → local Paper server → Minecraft Java Edition.

---

## Presentation materials (group session)

| Document | Purpose |
|----------|---------|
| **[docs/PRESENTATION-SLIDES.md](docs/PRESENTATION-SLIDES.md)** | 5-slide outline (Cursor → installs → AI build → test → wrap-up) |
| **[docs/STUDENT-HANDOUT.md](docs/STUDENT-HANDOUT.md)** | One-page checklist for students (install, build, test, troubleshoot) |

---

## Student handout (quick reference)

**Install:** Git · JDK 17+ · Maven 3.6+ · Paper JAR · Java for server · Minecraft **Java Edition**

**Build & test:**

```bash
mvn clean package
```

Copy `target/minecraft-plugin-1.0.0.jar` → server `plugins/` (one JAR only) → restart Paper → join **`localhost`** → `op YourName` → `/cowlaunch` · `/instabreak on`


Full handout: **[docs/STUDENT-HANDOUT.md](docs/STUDENT-HANDOUT.md)**

---

## Runtime (what you need to run the plugin)

These run on the **machine that hosts the Paper server** (players only need the Java Edition client to connect).

| Component | Purpose | Version / notes |
|-----------|---------|-----------------|
| **Paper** | Server; loads plugins | Example: Paper `26.1.2-61-main@8dea6f1` for Minecraft `26.1.2`. Check with `/version`. |
| **Java (JVM)** | Runs Paper + plugins | Example: Java `25.0.3`. Prefer the Java version [Paper recommends](https://papermc.io/downloads/paper) for your build. |
| **Plugin JAR** | This project | **1.0.0** — place in the server `plugins/` folder. |
| **Minecraft: Java Edition** | Client | Must match server Minecraft version (e.g. **26.1.2**). |

**Not used:** Fabric, Fabric API, or mods in `mods/`.

---

## Build-time (what you need to compile the plugin)

| Component | Purpose | Version (this repo) |
|-----------|---------|---------------------|
| **JDK** | Compile sources | **Java 17** (`maven.compiler.release` in `pom.xml`) |
| **Maven** | Build tool | **3.6+** |
| **paper-api** | Compile-only API (provided by Paper at runtime) | **1.20.6-R0.1-SNAPSHOT** — align with your Paper version when possible |
| **maven-compiler-plugin** | Compile | **3.13.0** |
| **maven-shade-plugin** | Package JAR | **3.6.0** |
| **JUnit** | Tests only | **4.13.2** |

Maven repository: https://repo.papermc.io/repository/maven-public/

---

## Getting started

### Build

```bash
mvn clean package
```

Output: `target/minecraft-plugin-1.0.0.jar`

### Install on Paper

1. Copy the JAR into your Paper server `plugins/` folder.
2. Restart the server.
3. In-game (OP or permissions): `/cowlaunch`, `/instabreak`.

### Paper server (first time)

1. Download Paper from https://papermc.io/downloads/paper
2. Run: `java -jar paper-*.jar --nogui`
3. Set `eula=true` in `eula.txt`
4. Start again; connect with **`localhost`** (same PC)

---

## Commands

| Command | Description |
|---------|-------------|
| `/cowlaunch [type\|all] [radius] [power]` | Launch nearby entities upward (default: living + vehicles) |
| `/instabreak [on\|off\|toggle]` | Toggle instant block breaking in survival |

Examples: `/cowlaunch` · `/cowlaunch pig 15 2` · `/instabreak on`

---

## Project structure

```
minecraft-plugin/
├── docs/
│   ├── PRESENTATION-SLIDES.md
│   └── STUDENT-HANDOUT.md
├── src/main/java/com/example/cowlauncher/
│   ├── CowLauncherPlugin.java
│   ├── CowLaunchCommand.java
│   ├── InstaBreakCommand.java
│   ├── InstaBreakListener.java
│   └── InstaBreakState.java
├── src/main/resources/plugin.yml
├── pom.xml
└── README.md
```

---

## How components interact at runtime

1. Paper scans `plugins/` and reads **`plugin.yml`** (`main`, commands, permissions).
2. Paper calls **`onEnable()`** on `CowLauncherPlugin` — registers command executors and the instabreak listener.
3. **`/cowlaunch`** → `CowLaunchCommand` finds nearby entities and sets velocity (server-side).
4. **`/instabreak`** → `InstaBreakListener` sets `instaBreak` on `BlockDamageEvent` in survival.

**Flow:** player input → server → your plugin → world changes → clients see the result.

Players do **not** install this plugin in their own game; only the **server** needs the JAR.

---

## License

MIT License — see [LICENSE](LICENSE).
