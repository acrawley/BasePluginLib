package net.andrewcr.minecraft.plugin.BasePluginLib.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public interface ICommand {
    String getCommandName();

    boolean invoke(Plugin plugin, CommandSender sender, String[] args);
}
