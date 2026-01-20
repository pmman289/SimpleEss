package tech.pmman;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import tech.pmman.commands.*;

import java.util.Arrays;

public class CommandRegister {
    private final static SimpleEssCommand SIMPLE_ESS_COMMAND = new SimpleEssCommand();
    private final static SetWorldSpawnCommand SET_WORLD_SPAWN_COMMAND = new SetWorldSpawnCommand();
    private final static SetHomeCommand SET_HOME_COMMAND = new SetHomeCommand();
    private final static HomeCommand HOME_COMMAND = new HomeCommand();
    private final static BackCommand BACK_COMMAND = new BackCommand();
    private final static BackToDeathCommand BACK_TO_DEATH_COMMAND = new BackToDeathCommand();
    private final static TpaCommand TPA_COMMAND = new TpaCommand();
    private final static TpaTabCommand TPA_TAB_COMMAND = new TpaTabCommand();
    private final static MsgCommand MSG_COMMAND = new MsgCommand();
    private final static RtpCommand RTP_COMMAND = new RtpCommand();

    private final static AbstractCommand[] ACTIVE_COMMAND = new AbstractCommand[]{
            SIMPLE_ESS_COMMAND,
            SET_WORLD_SPAWN_COMMAND,
            SET_HOME_COMMAND,
            HOME_COMMAND,
            BACK_COMMAND,
            BACK_TO_DEATH_COMMAND,
            TPA_COMMAND,
            TPA_TAB_COMMAND,
            MSG_COMMAND,
            RTP_COMMAND
    };

    // 如果配置文件没有打开限制开放的命令设置，则使用这里的默认开放
    private final static AbstractCommand[] DEFAULT_PUBLIC_COMMAND = new AbstractCommand[]{
            SET_HOME_COMMAND,
            HOME_COMMAND,
            BACK_COMMAND,
            BACK_TO_DEATH_COMMAND,
            TPA_COMMAND,
            TPA_TAB_COMMAND,
            MSG_COMMAND,
            RTP_COMMAND
    };

    public static void register(CommandRegistry commandRegistry) {
        String[] publicCommand = ConfigManager.PLUGIN_CONFIG.get()
                                                            .getPublicCommand();
        // 授权处理
        if (ConfigManager.PLUGIN_CONFIG.get()
                                       .isEnablePublicCommandControl()) {
            for (AbstractCommand command : ACTIVE_COMMAND) {
                // 如果开放了该命令，则自动向Default授权
                if (Arrays.asList(publicCommand)
                          .contains(command.getName()) && command instanceof PermissionGroupSettable) {
                    ((PermissionGroupSettable) command).resetPermissionGroups("Default");
                }
            }
        } else {
            for (AbstractCommand command : DEFAULT_PUBLIC_COMMAND) {
                ((PermissionGroupSettable) command).resetPermissionGroups("Default");
            }
        }
        Arrays.stream(ACTIVE_COMMAND)
              .forEach(commandRegistry::registerCommand);
    }
}
