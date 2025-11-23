# 🚀 Hytalib — Getting Started Guide

Welcome to **Hytalib**, the lightweight helper library made to power clean, scalable Hytale server plugins.  
It gives you config handling, database utilities, logging, builders, and common helpers — without bloat.

---

# 📦 1. Installation

Add Hytalib to your Gradle/Maven project:

<details>
  <summary><strong>Gradle</strong></summary>

```
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.HytaLab:hytalib:VERSION'
}
```
</details> 
<details> <summary><strong>Maven</strong></summary>
  
```
<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.HytaLab</groupId>
        <artifactId>hytalib</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```
</details>

If you're working on the library itself, simply clone the repo and open it in IntelliJ.

---

# ⚙️ 2. Project Setup (IntelliJ + Gradle)

1. Open IntelliJ  
2. **New Project → Gradle → Java**  
3. Set Java version (**21+ recommended**) **Will change to 24**
4. Add the Gradle dependency above  
5. Reload Gradle  
6. Create your first plugin class

---

# 🧩 3. Your Plugin Entry Class

Every plugin extends `PluginBase`.

```java
public class ExamplePlugin extends PluginBase {

    @Override
    public void onLoad() {
        super.onLoad("ExamplePlugin");
    }

    @Override
    protected void onEnable() {
        getLogger().info("ExamplePlugin has started!");
    }

    @Override
    protected void onDisable() {
        getLogger().info("ExamplePlugin is shutting down...");
    }
}
```

PluginBase gives you:

- A built-in logger  
- An easy plugin lifecycle  
- Auto registration of plugin name  
- Clean structure for startup and shutdown  

---

# 🧾 4. Configuration System (YAML)

Hytalib ships with a minimal, fast config loader built on SnakeYAML.

### Creating a configuration:

```java
Configuration config = new Configuration(Paths.get("plugins/ExamplePlugin/config.yml"));
```

### Reading values:

```java
config.getString("host");
config.getInt("port");
config.getBoolean("feature.enabled");

// All getters have a built-in safe fallback:
String value = config.getString("missing.key"); // returns null safely
```

### Complete getters:

- `getString(key)`
- `getInt(key)`
- `getBoolean(key)`
- `getDouble(key)`
- `getLong(key)`
- `getList(key)`
- `getMap(key)`

Defaults are optional — missing values simply return `null`.

---

# 🗄️ 5. Databases (HikariCP Connection Pooling)

Hytalib supports:

- MySQL  
- SQLite  
- H2  
- PostgreSQL *(soon)*  
- Redis (via Jedis)

### Example from plugin:

```java
database = DatabaseBuilder.create()
        .type(DatabaseTypes.MYSQL)
        .host(config.getString("data.host"))
        .port(config.getInt("data.port"))
        .database(config.getString("data.database"))
        .user(config.getString("data.username"))
        .password(config.getString("data.password"))
        .maxPoolSize(20)
        .build();
```

### SQLite example:

```java
database = DatabaseBuilder.create()
        .type(DatabaseTypes.SQLITE)
        .file("plugins/ExamplePlugin/data.db")
        .build();
```

DatabaseBuilder automatically:

- Creates a HikariDataSource  
- Applies safe defaults  
- Validates missing fields  
- Provides pooled connections  

---

# 🗃️ 6. Example Data Model

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

# 📁 7. Repository Example

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

# 🔌 8. Full Example Plugin (Config + Database)

```java
public class ExamplePlugin extends PluginBase {

    private static ExamplePlugin instance;
    private Configuration config;
    private Database database;

    @Override
    public void onLoad() {
        super.onLoad("ExamplePlugin");
    }

    @Override
    protected void onEnable() {
        instance = this;

        config = new Configuration(Paths.get("plugins/ExamplePlugin/config.yml"));

        if (config.getBoolean("data.sql-enabled")) {
            database = DatabaseBuilder.create()
                    .type(DatabaseTypes.MYSQL)
                    .host(config.getString("data.host"))
                    .port(config.getInt("data.port"))
                    .database(config.getString("data.database"))
                    .user(config.getString("data.username"))
                    .password(config.getString("data.password"))
                    .maxPoolSize(20)
                    .build();
        }

        getLogger().info("Example plugin enabled.");
    }

    @Override
    protected void onDisable() {
        if (database != null) database.close();
    }

    public static ExamplePlugin getInstance() {
        return instance;
    }

    public Configuration getConfig() {
        return config;
    }
}
```

---

# 🧰 9. Utilities

### NumberUtils

```java
NumberUtils.isInt("123");
NumberUtils.isDouble("3.14");
NumberUtils.format(1500);     // "1,500"
NumberUtils.round(3.14159, 2);
```

### ConfigUtils

- Safe loading  
- Auto-creating missing files  
- Includes optional default-saving  

---

# 📂 10. Recommended Project Structure

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
                     ├─ config/
                     └─ examples/
```

---

# ❤️ 11. Need Help?

Open an issue or discussion.  
This project exists *for developers like you*.

---

# 🎉 You're Ready to Build

You now have a fully functional foundation for serious Hytale plugin development.  
Happy coding! 💙
