package dev.team.hytalib.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * HytaLab Studio code @ 2025
 * <p>
 * Clean, self-contained YAML configuration manager.
 * This handles:
 *  - file creation
 *  - folder creation
 *  - autoloading
 *  - optional default values
 */
public final class Configuration {

    private final Path file;
    private final Yaml yaml;
    private Map<String, Object> data;

    public Configuration(Path file) {
        this.file = file;

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(50);

        SafeConstructor constructor = new SafeConstructor(loaderOptions);

        DumperOptions dumper = new DumperOptions();
        dumper.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumper.setIndent(2);
        dumper.setPrettyFlow(true);

        Representer representer = new Representer(dumper);

        this.yaml = new Yaml(constructor, representer, dumper, loaderOptions);

        initFile();
        loadInternal();
    }

    private void initFile() {
        try {
            if (!Files.exists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }

            if (!Files.exists(file)) {
                Files.createFile(file);
                this.data = new LinkedHashMap<>();
                saveInternal();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize config file: " + file, e);
        }
    }

    private synchronized void loadInternal() {
        try {
            String content = Files.readString(file);

            if (content.isBlank()) {
                this.data = new LinkedHashMap<>();
                return;
            }

            Object loaded = yaml.load(content);
            if (loaded instanceof Map) {
                //noinspection unchecked
                this.data = (Map<String, Object>) loaded;
            } else {
                this.data = new LinkedHashMap<>();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration: " + file, e);
        }
    }

    private synchronized void saveInternal() {
        try (Writer writer = new FileWriter(file.toFile())) {
            yaml.dump(this.data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Could not save configuration: " + file, e);
        }
    }

    public synchronized void reload() {
        loadInternal();
    }

    public synchronized void save() {
        saveInternal();
    }

    public synchronized Object get(String key) {
        return data.get(key);
    }

    public synchronized <T> T getOrDefault(String key, T def) {
        Object v = data.get(key);
        if (v == null) return def;
        try {
            @SuppressWarnings("unchecked")
            T casted = (T) v;
            return casted;
        } catch (ClassCastException ex) {
            return def;
        }
    }

    public synchronized String getString(String key, String def) {
        Object o = data.get(key);
        return o == null ? def : String.valueOf(o);
    }

    public synchronized String getString(String key) {
        Object o = data.get(key);
        return o == null ? "" : String.valueOf(o);
    }

    public synchronized int getInt(String key, int def) {
        Object o = data.get(key);

        if (o instanceof Number) return ((Number) o).intValue();
        if (o instanceof String) {
            try { return Integer.parseInt((String) o); }
            catch (NumberFormatException ignored) {}
        }
        return def;
    }

    public synchronized int getInt(String key) {
        Object o = data.get(key);

        if (o instanceof Number) return ((Number) o).intValue();
        if (o instanceof String) {
            try { return Integer.parseInt((String) o); }
            catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    public synchronized double getDouble(String key, double def) {
        Object o = data.get(key);

        if (o instanceof Number) return ((Number) o).doubleValue();
        if (o instanceof String) {
            try { return Double.parseDouble((String) o); }
            catch (NumberFormatException ignored) {}
        }
        return def;
    }

    public synchronized double getDouble(String key) {
        Object o = data.get(key);

        if (o instanceof Number) return ((Number) o).doubleValue();
        if (o instanceof String) {
            try { return Double.parseDouble((String) o); }
            catch (NumberFormatException ignored) {}
        }
        return 0.0;
    }

    public synchronized boolean getBoolean(String key, boolean def) {
        Object o = data.get(key);

        if (o instanceof Boolean) return (Boolean) o;
        if (o instanceof String) return Boolean.parseBoolean((String) o);

        return def;
    }

    public synchronized boolean getBoolean(String key) {
        Object o = data.get(key);

        if (o instanceof Boolean) return (Boolean) o;
        if (o instanceof String) return Boolean.parseBoolean((String) o);

        return false;
    }

    @SuppressWarnings("unchecked")
    public synchronized Map<String, Object> getConfigurationSection(String key) {
        Object o = data.get(key);
        if (o instanceof Map) return (Map<String, Object>) o;
        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    public synchronized Map<String, Object> getConfigurationSectionOrNull(String key) {
        Object o = data.get(key);
        if (o instanceof Map) return (Map<String, Object>) o;
        return null;
    }

    public synchronized void set(String key, Object value) {
        data.put(key, value);
        saveInternal();
    }

    public synchronized void remove(String key) {
        data.remove(key);
        saveInternal();
    }

    public synchronized void setDefault(String key, Object value) {
        if (!data.containsKey(key)) {
            data.put(key, value);
            saveInternal();
        }
    }

    public synchronized void applyDefaults(Map<String, Object> defaults) {
        boolean changed = false;
        for (var e : defaults.entrySet()) {
            if (!data.containsKey(e.getKey())) {
                data.put(e.getKey(), e.getValue());
                changed = true;
            }
        }
        if (changed) saveInternal();
    }

    public synchronized Map<String, Object> asMap() {
        return Collections.unmodifiableMap(data);
    }
}
