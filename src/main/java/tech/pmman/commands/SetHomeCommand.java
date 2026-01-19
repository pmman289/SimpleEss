package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;
import java.util.Map;

public class SetHomeCommand extends AbstractPlayerCommand implements PermissionGroupSettable {

    public SetHomeCommand() {
        super("sethome", "simpleEssCommand.sethome.desc");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        String uuid = playerRef.getUuid()
                               .toString();
        Map<String, PlayerLocationEntry> homeData = SimpleEssPlugin.playerHomeConfig.get()
                                                                                    .getHomeData();
        Transform transform = playerRef.getTransform();
        homeData.put(uuid, new PlayerLocationEntry(world.getWorldConfig()
                                                        .getUuid()
                                                        .toString(), transform.getPosition(), transform.getRotation()));
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.execute"));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
