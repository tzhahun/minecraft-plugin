# Presentation slides — CowLauncher (5 slides)

**Flow:** Cursor → AI-guided steps → install tools in order → build plugin → run Paper & test  
**Length:** ~20–30 minutes with demo

---

## Slide 1 — Start in Cursor (find the project)

**Goal:** Get the code on your PC and open it in Cursor.

1. Project lives on **GitHub** — it must be on your machine to edit it.
2. **Install Git** if `git` is not recognised ([git-scm.com](https://git-scm.com/download/win)).
3. Clone the repo:
   ```powershell
   git clone https://github.com/YOUR_USER/minecraft-plugin.git
   ```
   *(Or: GitHub → Code → Download ZIP → extract.)*
4. **Cursor:** `File → Open Folder…` → select `minecraft-plugin`.
5. Browse files: **`Ctrl+Shift+E`** (Explorer), **`Ctrl+P`** (quick open file).

**From here:** Use **Cursor AI (chat)** to ask questions and generate the plugin step by step — you don’t have to look everything up alone.

---

## Slide 2 — What we installed (in order)

**Use Cursor AI to guide each step** — e.g. “update my pom”, “create Paper plugin”, “how do I add Paper server?”

| Order | What | Why |
|-------|------|-----|
| 1 | **Git** | Clone / commit / push to GitHub |
| 2 | **JDK 17+** | Compile Java (plugin + Maven) |
| 3 | **Maven 3.6+** | Build the plugin JAR (`mvn package`) |
| 4 | **Cursor** | Edit code + **AI assistant** for prompts and fixes |
| 5 | **Paper server** JAR | Run Minecraft server that loads plugins |
| 6 | **Java** for the server | Run Paper (e.g. Java 21 LTS; we also used Java 25) |
| 7 | **Minecraft: Java Edition** | Join your server at `localhost` (not Education / Bedrock) |

**Clarified with AI:** This is a **Paper (Bukkit) plugin** — **not** Fabric, **not** Fabric API, **not** mods in `mods/`.

---

## Slide 3 — Build the plugin (AI + Maven)

**Prompts we used in Cursor AI (examples):**

- Update `pom.xml` for Paper / Maven (Java 17, `paper-api`).
- Create a Paper plugin that **launches nearby cows upward** → `/cowlaunch`.
- Add **instant break in survival** → `/instabreak`.
- Refactor: launch **other entities** (pig, zombie, all); **separate** instabreak from cow launch code.

**What got created:**

- `src/main/resources/plugin.yml` — Paper reads this to load the plugin.
- Java classes: `CowLauncherPlugin`, `CowLaunchCommand`, `InstaBreak*`.
- `pom.xml` — Paper API dependency (compile only; server provides it at runtime).

**Build:**

```powershell
cd minecraft-plugin
mvn clean package
```

→ JAR in `target/` (e.g. `minecraft-plugin-1.0.0.jar`).

---

## Slide 4 — Paper server & test (same PC)

**Set up server (AI helped with EULA, errors, `cat` / PowerShell):**

1. Folder e.g. `C:\MinecraftServer\`
2. Download **Paper** from [papermc.io](https://papermc.io/downloads/paper)
3. First run: `java -jar paper-*.jar --nogui`
4. Edit **`eula.txt`**: `eula=true`
5. Copy **one** plugin JAR → `plugins\` (not two — avoids “ambiguous plugin” error)
6. Start server again → console should show: **`[CowLauncher] CowLauncher enabled`**

**Play & test:**

- Minecraft Java → Multiplayer → **`localhost`**
- Server console: `op YourName` (if commands say “unknown”)
- `/plugins` · `/cowlaunch` · `/cowlaunch pig 15 2` · `/instabreak on`

**Fixes we hit along the way:**

| Problem | Fix |
|---------|-----|
| No `plugin.yml` in jar | `plugin.yml` under `src/main/resources/`, rebuild |
| Duplicate plugin JARs | Keep only one file in `plugins/` |
| EULA failed | `eula=true`, run server from correct folder |

---

## Slide 5 — How it fits together & what we achieved

**How the plugin talks to Paper (simple):**

1. Paper loads JAR from `plugins/` + reads **`plugin.yml`**
2. Calls **`onEnable()`** — registers `/cowlaunch`, `/instabreak`, listeners
3. You type a command in game → **server** runs your Java → world changes → everyone sees it

**What we achieved:**

- Working **Paper plugin** on a **local server**
- Two features: **entity launcher** + **insta-break toggle**
- **GitHub** repo with README, runtime/build notes, and student handout
- Learned: **build → copy JAR → restart → localhost → test**

**Our environment (example):**

- Paper **26.1.2** · Plugin **1.0.0** · Check with `/version`

**Resources:** [README](../README.md) · [Student handout](STUDENT-HANDOUT.md) · [Paper docs](https://docs.papermc.io/)

**Q&A**
