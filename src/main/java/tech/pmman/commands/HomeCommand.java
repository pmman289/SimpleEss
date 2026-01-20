package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.ConfigManager;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class HomeCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.home";

    private final DefaultArg<String> name;

    public HomeCommand() {
        super("home", "simpleEssCommand.home.desc");
        name = withDefaultArg("name", "simpleEssCommand.home.desc.argName",
                ArgTypes.STRING, "default", "simpleEssCommand.home.desc.argNameDefault");
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        String homeName = name.get(commandContext);
        String uuid = playerRef.getUuid()
                               .toString();
        Map<String, PlayerLocationEntry> homeMap = ConfigManager.PLAYER_HOME_DATA.get()
                                                                .getHomeMap(uuid);
        if (!homeMap.containsKey(homeName)) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.home.nohome")
                                                                 .param("homeName", homeName));
            return;
        }
        PlayerLocationEntry location = homeMap.get(homeName);
        // 判断世界是否在线
        World targetWorld = Universe.get()
                                    .getWorld(UUID.fromString(location.getWorldUUID()));
        if (targetWorld == null || !targetWorld.isAlive()) {
            MessageTool.sendPluginMessage(playerRef, Message.translation("simpleEssCore.teleport.targetWorldNotOnline"));
            return;
        }
        PlayerTool.teleportPlayer(ref, targetWorld, location.getPosition(), location.getRotation());
        PlayerTool.recordPlayerTransformHistory(playerRef.getUuid()
                                                         .toString(), location);
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.home.execute"));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
