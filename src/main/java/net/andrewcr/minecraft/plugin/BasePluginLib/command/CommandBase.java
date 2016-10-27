package net.andrewcr.minecraft.plugin.BasePluginLib.command;


import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class CommandBase implements ICommand {
    public abstract CommandExecutorBase getExecutor();

    public final String getCommandName() {
        return this.getExecutor().getName();
    }

    public final boolean invoke(Plugin plugin, CommandSender sender, String[] args) {
        CommandExecutorBase executor = this.getExecutor();

        executor.init(plugin, sender, args);
        return executor.invoke();
    }
}
