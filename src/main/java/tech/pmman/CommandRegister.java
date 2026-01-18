package tech.pmman;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import tech.pmman.commands.*;

import java.util.Arrays;

public class CommandRegister {
    private final static SetWorldSpawnCommand SET_WORLD_SPAWN_COMMAND = new SetWorldSpawnCommand();
    private final static SetHomeCommand SET_HOME_COMMAND = new SetHomeCommand();
    private final static HomeCommand HOME_COMMAND = new HomeCommand();
    private final static BackCommand BACK_COMMAND = new BackCommand();
    private final static BackToDeathCommand BACK_TO_DEATH_COMMAND = new BackToDeathCommand();
    private final static TpaCommand TPA_COMMAND = new TpaCommand();
    private final static TpaTabCommand TPA_TAB_COMMAND = new TpaTabCommand();
    private final static MsgCommand MSG_COMMAND = new MsgCommand();

    private final static AbstractCommand[] ACTIVE_COMMAND = new AbstractCommand[]{
            SET_WORLD_SPAWN_COMMAND,
            SET_HOME_COMMAND,
            HOME_COMMAND,
            BACK_COMMAND,
            BACK_TO_DEATH_COMMAND,
            TPA_COMMAND,
            TPA_TAB_COMMAND,
            MSG_COMMAND
    };

    public static void register(CommandRegistry commandRegistry) {
        String[] publicCommand = SimpleEssPlugin.pluginConfig.get()
                                                             .getPublicCommand();
        for (AbstractCommand command : ACTIVE_COMMAND) {
            // 如果开放了该命令，则加入Default组
            if (Arrays.asList(publicCommand)
                      .contains(command.getName())) {
                ((PermissionGroupSettable) command).resetPermissionGroups("Default", GameMode.Adventure.toString(), GameMode.Creative.toString());
            }
            commandRegistry.registerCommand(command);
        }
    }
}
