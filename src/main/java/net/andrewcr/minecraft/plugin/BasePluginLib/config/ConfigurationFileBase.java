package net.andrewcr.minecraft.plugin.BasePluginLib.config;

import lombok.Getter;
import lombok.Synchronized;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ConfigurationFileBase {
    private SaveFileTask task;
    private boolean isLoading;
    private Plugin plugin;

    @Getter
    private Object syncObj = new Object();

    private class SaveFileTask extends BukkitRunnable {
        private ConfigurationFileBase file;

        public SaveFileTask(ConfigurationFileBase file) {
            this.file = file;
        }

        @Override
        public void run() {
            this.file.save();
        }
    }

    protected ConfigurationFileBase(Plugin plugin) {
        this.plugin = plugin;
    }

    @Synchronized("syncObj")
    public void save() {
        this.saveCore();
    }

    @Synchronized("syncObj")
    public void load() {
        try {
            this.isLoading = true;
            this.loadCore();
        } finally {
            this.isLoading = false;
        }
    }

    protected abstract void saveCore();

    protected abstract void loadCore();

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
}
