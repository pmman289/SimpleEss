package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.exceptions.GeneralCommandException;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerDeathPositionData;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.List;

public class BackToDeathCommand extends AbstractPlayerCommand implements PermissionGroupSettable {

    public BackToDeathCommand() {
        super("backdeath", "simpleEssCommand.backdeath.desc");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        if (!commandContext.isPlayer()) {
            throw new GeneralCommandException(Message.raw("Only players can use this command"));
        }
        Player player = commandContext.senderAs(Player.class);
        List<PlayerDeathPositionData> deathPositions = player.getPlayerConfigData()
                                                             .getPerWorldData(world.getName())
                                                             .getDeathPositions();
        if (deathPositions.isEmpty()) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.backdeath.nodeath"));
            return;
        }
        Transform lastDeath = deathPositions.getLast()
                                            .getTransform();
        PlayerTool.teleportPlayer(ref, world, lastDeath.getPosition(), lastDeath.getRotation());
        PlayerTool.recordPlayerTransformHistory(playerRef.getUuid()
                                                         .toString(),
                new PlayerLocationEntry(world.getWorldConfig()
                                             .getUuid()
                                             .toString(),
                        lastDeath.getPosition(),
                        lastDeath.getRotation()));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
