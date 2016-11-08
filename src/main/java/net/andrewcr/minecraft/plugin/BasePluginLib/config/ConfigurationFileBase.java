package net.andrewcr.minecraft.plugin.BasePluginLib.config;

import lombok.Getter;
import lombok.Synchronized;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public abstract class ConfigurationFileBase {
    private final Plugin plugin;
    private SaveFileTask task;
    private boolean isLoading;
    @Getter private Object syncObj = new Object();

    protected ConfigurationFileBase(Plugin plugin) {
        this.plugin = plugin;
    }

    @Synchronized("syncObj")
    public void save() {
        File configFile = new File(this.getStorageLocation(), this.getFileName());
        YamlConfiguration config = new YamlConfiguration();

        this.saveCore(config);

        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save DistributedSpawns configuration: " + e.toString());
        }
    }

    @Synchronized("syncObj")
    public void load() {
        try {
            File configFile = new File(this.getStorageLocation(), this.getFileName());
            if (configFile.exists()) {
                this.isLoading = true;

                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                this.loadCore(config);
            }
        } finally {
            this.isLoading = false;
        }
    }

    protected File getStorageLocation() {
        return this.plugin.getDataFolder();
    }

    protected abstract String getFileName();

    protected abstract void saveCore(YamlConfiguration configuration);

    protected abstract void loadCore(YamlConfiguration configuration);

    public void notifyChanged() {
        if (this.isLoading) {
            // Ignore change notifications during load
            return;
        }

        if (this.task != null) {
            // Already had a pending save due to another change - cancel the task so we can restart it
            this.task.cancel();
        }

        // Save the file in 5 seconds
        this.task = new SaveFileTask(this);
        this.task.runTaskLaterAsynchronously(this.plugin, 100);
    }

    private class SaveFileTask extends BukkitRunnable {
        private final ConfigurationFileBase file;

        public SaveFileTask(ConfigurationFileBase file) {
            this.file = file;
        }

        @Override
        public void run() {
            this.file.save();
        }
    }
}
