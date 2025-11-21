package dev.team.hytalib.core;

public class PluginLogger {

    /*
     * Using temp System prints, but will change when hytale has their own console logging system.
     */

    private final String prefix;

    public PluginLogger(String pluginName) {
        this.prefix = pluginName;
    }

    public void info(String msg) {
        System.out.println(prefix + " INFO: " + msg);
    }

    public void warn(String msg) {
        System.out.println(prefix + " WARN: " + msg);
    }

    public void error(String msg) {
        System.out.println(prefix + " ERROR: " + msg);
    }

    public void debug(String msg) {
        System.out.println(prefix + " DEBUG: " + msg);
    }
}