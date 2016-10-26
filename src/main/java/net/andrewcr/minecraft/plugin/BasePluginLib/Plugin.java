package net.andrewcr.minecraft.plugin.BasePluginLib;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("Loaded BasePluginLib!");
    }
}
