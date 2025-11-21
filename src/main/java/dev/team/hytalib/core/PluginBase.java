package dev.team.hytalib.core;

public abstract class PluginBase {

    private PluginLogger logger;

    private boolean enabled = false;

    public final void onLoad(String pluginName) {
        this.logger = new PluginLogger(pluginName);
    }

    public final PluginLogger getLogger() {
        return logger;
    }

    protected abstract void onEnable();
    protected abstract void onDisable();
}