package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.spawn.FitToHeightMapSpawnProvider;
import com.hypixel.hytale.server.core.universe.world.spawn.GlobalSpawnProvider;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;

public class SetWorldSpawnCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.setworldspawn";

    public SetWorldSpawnCommand() {
        super("setworldspawn", "simpleEssCommand.setworldspawn.desc");
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Transform transform = PlayerTool.getTransform(store, ref).clone();
        FitToHeightMapSpawnProvider spawnProvider = new FitToHeightMapSpawnProvider(new GlobalSpawnProvider(transform));
        world.getWorldConfig()
             .setSpawnProvider(spawnProvider);
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.setworldspawn.execute")
                                                             .param("x", transform.getPosition().x)
                                                             .param("y", transform.getPosition().y)
                                                             .param("z", transform.getPosition().z));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
