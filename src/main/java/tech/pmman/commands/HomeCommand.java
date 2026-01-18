package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractWorldCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.util.CheckTool;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.Map;

public class HomeCommand extends AbstractWorldCommand implements PermissionGroupSettable {

    public HomeCommand() {
        super("home", "simpleEssCommand.home.desc");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull World world, @Nonnull Store<EntityStore> store) {
        Ref<EntityStore> playerRef = CheckTool.getPlayerRefByCommandContext(commandContext);
        String uuid = PlayerTool.getUUID(store, playerRef)
                                .toString();
        Map<String, Transform> homeData = SimpleEssPlugin.playerHomeConfig.get()
                                                                          .getHomeData();
        if (!homeData.containsKey(uuid)) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.home.nohome"));
            return;
        }
        Transform targetTransform = homeData.get(uuid);
        PlayerTool.teleportPlayerNoRotation(playerRef, targetTransform);
        PlayerTool.recordPlayerTransformHistoryNoRotation(PlayerTool.getUUID(store, playerRef)
                                                                    .toString(), targetTransform);
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.home.execute"));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
