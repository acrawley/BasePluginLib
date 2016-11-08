package net.andrewcr.minecraft.plugin.BasePluginLib.plugin;

import net.andrewcr.minecraft.plugin.BasePluginLib.command.ICommand;
import net.andrewcr.minecraft.plugin.BasePluginLib.util.PluginUtil;
import net.andrewcr.minecraft.plugin.BasePluginLib.util.Version;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public abstract class PluginBase extends JavaPlugin {
    //region Private Fields

    private Map<String, ICommand> commandMap;
    private boolean enabled;

    //endregion

    @Override
    public final void onEnable() {
        super.onEnable();

        if (!PluginUtil.ensureBPLVersion(this.getRequiredBPLVersion())) {
            this.getLogger().severe("Installed BasePluginLib version '" + PluginUtil.getBPLVersion()
                + "' is older than required version '" + this.getRequiredBPLVersion() + "'!");
            this.getPluginLoader().disablePlugin(this);

            return;
        }

        this.commandMap = new HashMap<>();
        this.enabled = true;

        this.onEnableCore();
    }

    @Override
    public final void onDisable() {
        super.onDisable();

        if (this.enabled) {
            this.enabled = false;

            this.onDisableCore();
        }
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

    protected abstract Version getRequiredBPLVersion();

    protected void onEnableCore() {
    }

    protected void onDisableCore() {
    }

}

