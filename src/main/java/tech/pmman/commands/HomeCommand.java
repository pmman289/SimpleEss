package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class HomeCommand extends AbstractPlayerCommand implements PermissionGroupSettable {

    public HomeCommand() {
        super("home", "simpleEssCommand.home.desc");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        String uuid = playerRef.getUuid()
                               .toString();
        Map<String, PlayerLocationEntry> homeData = SimpleEssPlugin.playerHomeConfig.get()
                                                                                    .getHomeData();
        if (!homeData.containsKey(uuid)) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.home.nohome"));
            return;
        }
        PlayerLocationEntry location = homeData.get(uuid);
        // 判断世界是否在线
        World targetWorld = Universe.get()
                                    .getWorld(UUID.fromString(location.getWorldUUID()));
        if (targetWorld == null || !targetWorld.isAlive()) {
            MessageTool.sendPluginMessage(playerRef, Message.translation("simpleEssCore.teleport.targetWorldNotOnline"));
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
