package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractWorldCommand;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.spawn.FitToHeightMapSpawnProvider;
import com.hypixel.hytale.server.core.universe.world.spawn.GlobalSpawnProvider;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.util.CheckTool;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;

public class SetWorldSpawnCommand extends AbstractWorldCommand implements PermissionGroupSettable {

    public SetWorldSpawnCommand() {
        super("setworldspawn", "simpleEssCommand.setworldspawn.desc");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull World world, @Nonnull Store<EntityStore> store) {
        Ref<EntityStore> playerRef = CheckTool.getPlayerRefByCommandContext(commandContext);
        Transform transform = PlayerTool.getTransform(store, playerRef).clone();
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
