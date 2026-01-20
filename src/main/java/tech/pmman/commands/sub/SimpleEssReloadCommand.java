package tech.pmman.commands.sub;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import lombok.Getter;
import tech.pmman.ConfigManager;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;

public class SimpleEssReloadCommand extends CommandBase {
    @Getter
    private final String permissionStr = "simpleess.command.simpleess.reload";

    public SimpleEssReloadCommand() {
        super("reload", "simpleEssCommand.simpleess.reload.desc");
        requirePermission(permissionStr);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        MessageTool.sendPluginMessage(
                commandContext, Message.translation("simpleEssCommand.simpleess.reload.execute"));
        // 这里做重载配置文件操作
        ConfigManager.PLUGIN_CONFIG.load();
        MessageTool.sendPluginMessage(
                commandContext, Message.translation("simpleEssCommand.simpleess.reload.executed")
        );
    }
}
