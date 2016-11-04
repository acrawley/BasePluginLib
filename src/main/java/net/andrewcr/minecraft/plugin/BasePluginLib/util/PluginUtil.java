package net.andrewcr.minecraft.plugin.BasePluginLib.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUtil {
    public static boolean ensureBPLVersion(Version requiredVersion) {
        Version bplVersion = PluginUtil.getBPLVersion();
        if (bplVersion == null) {
            // Couldn't determine the version?
            return false;
        }

        return !bplVersion.isOlderThan(requiredVersion);
    }

    public static Version getBPLVersion() {
        Plugin bpl = Bukkit.getPluginManager().getPlugin("BasePluginLib");

        return Version.tryParse(bpl.getDescription().getVersion());
    }
}
