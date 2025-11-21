# 🚀 Hytalib — Getting Started Guide

Welcome to **Hytalib**, the lightweight helper library built to make Hytale plugin development faster, cleaner, and way more fun.  
This guide walks you through installation, setup, and writing your first plugin using Hytalib.

---

# 📦 1. Installation

Add Hytalib to your plugin project using Gradle:

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'dev.team.hytalib:hytalib:0.0.1'
}
```

If you are developing Hytalib locally, clone the repo and import it into IntelliJ.

---

# ⚙️ 2. Project Setup (IntelliJ + Gradle)

1. Open IntelliJ  
2. Click **New Project → Gradle → Java**  
3. Set **Java 17**  
4. Add the dependencies listed above  
5. Reload Gradle  
6. Create your main plugin module

---

# 🧩 3. Your First Plugin

Every plugin extends `PluginBase`:

```java
public class ExamplePlugin extends PluginBase {

    @Override
    protected void onEnable() {
        getLogger().info("Example plugin is alive!");
    }

    @Override
    protected void onDisable() {
        getLogger().info("Example plugin is stopping...");
    }
}
```

You now have:
- Plugin lifecycle  
- Built‑in logger  
- Ready support for database integrations  

---

# 🗄️ 4. Database Setup

Hytalib supports:

- MySQL  
- SQLite  
- H2  
- Redis  

### Example — MySQL with HikariCP:

```java
var dataSource = DatabaseBuilder.create()
        .type(DatabaseType.MYSQL)
        .host("localhost")
        .port(3306)
        .database("hytalib")
        .user("root")
        .password("password")
        .build();
```

### SQLite:

```java
var dataSource = DatabaseBuilder.create()
        .type(DatabaseType.SQLITE)
        .file("plugins/Hytalib/data.db")
        .build();
```

---

# 🗃️ 5. Creating a Data Model

Example: user storage.

```java
public class UserData {

    private final UUID uuid;
    private String name;
    private int coins;

    public UserData(UUID uuid, String name, int coins) {
        this.uuid = uuid;
        this.name = name;
        this.coins = coins;
    }
}
```

---

# 📁 6. Repository for Database Access

```java
public class UserRepository {

    private final Database db;

    public UserRepository(Database db) {
        this.db = db;
    }

    public void createTable() throws SQLException {
        try (var conn = db.getConnection();
             var stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    uuid VARCHAR(36) PRIMARY KEY,
                    name VARCHAR(32),
                    coins INT
                );
            """);
        }
    }
}
```

---

# 🔌 7. Putting It All Together

```java
@Override
protected void onEnable() {
    getLogger().info("Booting Hytalib example...");

    var ds = DatabaseBuilder.create()
            .type(DatabaseType.MYSQL)
            .host("localhost")
            .port(3306)
            .database("hytalib")
            .user("root")
            .password("password")
            .build();

    var db = new Database(ds);
    var users = new UserRepository(db);

    try {
        users.createTable();
        getLogger().info("User table initialized.");
    } catch (Exception e) {
        getLogger().error("Error setting up database: " + e.getMessage());
    }
}
```

---

# 🧰 8. Utilities

Hytalib includes useful helper classes like:

### Number Utils

```java
NumberUtils.isInt("123");
NumberUtils.isDouble("3.14");
NumberUtils.format(1500); // "1,500"
NumberUtils.round(3.14159, 2);
```

More utilities will be added over time.

---

# 👨‍💻 9. Folder Structure (Recommended)

```
src/
 └─ main/
     └─ java/
         └─ dev/
             └─ team/
                 └─ hytalib/
                     ├─ core/
                     ├─ db/
                     ├─ utils/
                     └─ examples/
```

---

# ❤️ 10. Need Help?

Open an issue or create a discussion on GitHub.  
We’re building this library *for developers*, so feedback is always welcome.

---

# 🎉 You’re Ready!

You now have everything you need to start creating clean, scalable Hytale server plugins using Hytalib.  
Happy building 💙
