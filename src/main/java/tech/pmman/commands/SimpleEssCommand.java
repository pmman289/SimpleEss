package tech.pmman.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import lombok.Getter;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.commands.sub.SimpleEssReloadCommand;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;

public class SimpleEssCommand extends CommandBase implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.simpleess";

    public SimpleEssCommand() {
        super("simpleess", "simpleEssCommand.simpleess.desc");
        requirePermission(permissionStr);

        // 添加子命令
        addSubCommand(new SimpleEssReloadCommand());
    }


    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        Message msg = Message.translation("simpleEssCommand.simpleess.execute")
                             .param("version", SimpleEssPlugin.VERSION);
        MessageTool.sendPluginMessage(commandContext, msg);
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
