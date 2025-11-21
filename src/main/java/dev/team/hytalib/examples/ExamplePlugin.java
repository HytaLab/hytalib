package dev.team.hytalib.examples;

import dev.team.hytalib.core.PluginBase;
import dev.team.hytalib.db.Database;
import dev.team.hytalib.db.DatabaseBuilder;
import dev.team.hytalib.db.DatabaseTypes;

public class ExamplePlugin extends PluginBase {

    /*
     * HytaLib Studio code @ 2025
     * ExamplePlugin base showing database builder creation.
     */

    private Database database;

    @Override
    protected void onEnable() {
        /*
         * database builder - MYSQL type.
         */
        database = DatabaseBuilder.create()
                .type(DatabaseTypes.MYSQL)
                .host("localhost")
                .port(3306)
                .database("hytalib")
                .user("root")
                .password("password")
                .maxPoolSize(20)
                .build();

        /*
         * database builder - H2 type.
         */
        database = DatabaseBuilder.create()
                .type(DatabaseTypes.H2)
                .filePath("./plugins/hytalib/h2database")
                .user("root")
                .password("password")
                .build();

        /*
         * database builder - SQLITE type.
         */
        database = DatabaseBuilder.create()
                .type(DatabaseTypes.SQLITE)
                .filePath("./plugins/hytalib/h2database")
                .maxPoolSize(10)
                .build();

        getLogger().info("Databases built.");

        /*
         * database builder - REDIS type. (NOT YET IMPLEMENTED...)
         */
    }

    @Override
    protected void onDisable() {
        if (database != null) database.close();
    }
}