package net.andrewcr.minecraft.plugin.BasePluginLib.plugin;

import net.andrewcr.minecraft.plugin.BasePluginLib.command.ICommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class PluginBase extends JavaPlugin {
    //region Private Fields

    private Map<String, ICommand> commandMap;

    //endregion

    @Override
    public void onEnable() {
        super.onEnable();

        this.commandMap = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmdName = command.getName().toLowerCase();

        ICommand cmd = this.commandMap.get(cmdName);
        if (cmd == null) {
            this.getLogger().severe("No handler for command '" + cmdName + "'!");
            return false;
        }

        return cmd.invoke(this, sender, args);
    }

    protected void registerCommand(ICommand command) {
        this.commandMap.put(command.getCommandName().toLowerCase(), command);
    }

    protected void registerListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

}

