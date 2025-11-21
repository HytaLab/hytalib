package dev.team.hytalib.examples;

import dev.team.hytalib.config.Configuration;
import dev.team.hytalib.core.PluginBase;
import dev.team.hytalib.db.Database;
import dev.team.hytalib.db.DatabaseBuilder;
import dev.team.hytalib.db.DatabaseTypes;

import java.nio.file.Paths;

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