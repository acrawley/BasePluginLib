package net.andrewcr.minecraft.plugin.BasePluginLib.command;

public abstract class GroupCommandExecutorBase extends CommandExecutorBase {
    protected GroupCommandExecutorBase(String name) {
        super(name);
    }

    protected GroupCommandExecutorBase(String name, String permission) {
        super(name, permission);
    }

    @Override
    protected boolean isCommandGroup() {
        return true;
    }

    @Override
    protected boolean invoke(String[] args) {
        // Command is a container for other commands, so just show usage if invoked directly
        return false;
    }
}
