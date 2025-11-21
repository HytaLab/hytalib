package dev.team.hytalib.core;

/**
 * HytaLab Studio code @ 2025
 * <p>
 * CSubject to change once Hytale server code is released.
 */
public abstract class PluginBase {

    private PluginLogger logger;
    private boolean enabled = false;
    private String pluginName;

    public final void onLoad(String pluginName) {
        this.pluginName = pluginName;
        this.logger = new PluginLogger(pluginName);
        getLogger().info("Loaded plugin: " + pluginName);
    }

    public final void enable() {
        if (enabled) {
            getLogger().warn("Plugin is already enabled!");
            return;
        }

        getLogger().info("Enabling plugin...");

        try {
            onEnable();
            enabled = true;
            getLogger().info("Plugin enabled successfully.");
        } catch (Exception e) {
            getLogger().error("Error while enabling plugin: " + e.getMessage());
            enabled = false;
            e.printStackTrace();
        }
    }

    public final void disable() {
        if (!enabled) {
            getLogger().warn("Plugin is already disabled!");
            return;
        }

        getLogger().info("Disabling plugin...");
        enabled = false;

        try {
            onDisable();
            getLogger().info("Plugin disabled successfully.");
        } catch (Exception e) {
            getLogger().error("Error while disabling plugin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public PluginLogger getLogger() {
        return logger;
    }

    public abstract void onLoad();
    protected abstract void onEnable();
    protected abstract void onDisable();

    public String getPluginName() {
        return pluginName;
    }
}
