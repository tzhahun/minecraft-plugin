# Student handout — Entity Launcher & Instant Break (Paper plugin)

One-page checklist: install tools → build the plugin → run Paper → test in-game.

---

## What you are building

A **Paper (Bukkit) server plugin** — **not** Fabric, **not** mods in `mods/`.

| Command | What it does |
|---------|----------------|
| `/cowlaunch [type\|all] [radius] [power]` | Launch nearby entities upward (default: living entities + vehicles) |
| `/instabreak [on\|off\|toggle]` | Toggle instant block breaking in survival |

Examples: `/cowlaunch` · `/cowlaunch pig 15 2` · `/instabreak on`

---

## Install (in order)

Use **Cursor AI** if you get stuck — e.g. “how do I install JDK 17 on Windows?”

| # | Tool | Why you need it |
|---|------|-----------------|
| 1 | **Git** | Clone and update the project from GitHub |
| 2 | **JDK 17+** | Compile Java (Maven uses this) |
| 3 | **Maven 3.6+** | Build the plugin JAR (`mvn package`) |
| 4 | **Cursor** | Edit code + AI assistant for prompts and fixes |
| 5 | **Paper server** JAR | Run a Minecraft server that loads plugins |
| 6 | **Java** for the server | Run Paper (e.g. Java 21 LTS) |
| 7 | **Minecraft: Java Edition** | Join your server at `localhost` (not Bedrock / Education) |

**Clone the repo:**

```powershell
git clone https://github.com/YOUR_USER/minecraft-plugin.git
cd minecraft-plugin
```

*(Or: GitHub → Code → Download ZIP → extract.)*

**Open in Cursor:** `File → Open Folder…` → select `minecraft-plugin`.

---

## Build the plugin

From the project folder:

```powershell
mvn clean package
```

**Output:** `target/minecraft-plugin-1.0.0.jar`

If the build fails, check: JDK installed (`java -version`), Maven installed (`mvn -version`), and internet access for Maven downloads.

---

## Set up Paper (first time)

1. Create a folder, e.g. `C:\MinecraftServer\`
2. Download **Paper** from [papermc.io/downloads/paper](https://papermc.io/downloads/paper)
3. First run: `java -jar paper-*.jar --nogui`
4. Edit **`eula.txt`**: set `eula=true`
5. Copy **one** plugin JAR → `plugins\` (only one copy — see troubleshooting)
6. Start the server again

**Success:** console shows `[CowLauncher] CowLauncher enabled (/cowlaunch, /instabreak).`

---

## Test in-game

1. Open **Minecraft: Java Edition** → Multiplayer → add server **`localhost`**
2. If commands are “unknown”, run in the **server console**: `op YourName`
3. In-game checks:
   - `/plugins` — CowLauncher should appear in green
   - `/cowlaunch` — nearby entities fly upward
   - `/cowlaunch pig 15 2` — only pigs within 15 blocks, stronger launch
   - `/instabreak on` — survival blocks break instantly (toggle off with `/instabreak off`)

**Permissions:** both commands default to **OP** only (`cowlauncher.use`, `cowlauncher.instabreak`).

---

## Troubleshooting

| Problem | Fix |
|---------|-----|
| `git` / `mvn` / `java` not recognised | Install the tool; restart terminal; check PATH |
| Build fails (cannot resolve `paper-api`) | Check internet; Maven uses [repo.papermc.io](https://repo.papermc.io/repository/maven-public/) |
| Server: “ambiguous plugin” or duplicate load | Keep **only one** JAR in `plugins/` |
| Plugin not listed in `/plugins` | Ensure `plugin.yml` is in `src/main/resources/`, rebuild, copy new JAR, restart server |
| “You don't have permission” | `op YourName` in server console |
| Cannot connect to server | Server running? Correct Minecraft version? Use `localhost` on the same PC |
| EULA error | `eula=true` in `eula.txt`, run server from the correct folder |
| Wrong edition | Use **Java Edition** client, not Bedrock or Education |

---

## Quick reference

| Item | This project |
|------|----------------|
| Plugin version | **1.0.0** |
| Build JDK | **Java 17** (`pom.xml`) |
| Paper API (compile) | **1.20.6-R0.1-SNAPSHOT** — align Paper download with your server version when possible |
| Runtime | Paper provides the API; players only need the Java Edition client |

**More detail:** [README](../README.md) · [Presentation slides](PRESENTATION-SLIDES.md) · [Paper docs](https://docs.papermc.io/)
