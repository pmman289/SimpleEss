package tech.pmman.commands;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractWorldCommand;
import com.hypixel.hytale.server.core.command.system.exceptions.GeneralCommandException;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerDeathPositionData;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.List;

public class BackToDeathCommand extends AbstractWorldCommand implements PermissionGroupSettable {

    public BackToDeathCommand() {
        super("backdeath", "simpleEssCommand.backdeath.desc");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull World world, @Nonnull Store<EntityStore> store) {
        if (!commandContext.isPlayer()) {
            throw new GeneralCommandException(Message.raw("Only players can use this command"));
        }
        Player player = commandContext.senderAs(Player.class);
        List<PlayerDeathPositionData> deathPositions = player.getPlayerConfigData()
                                                             .getPerWorldData(World.DEFAULT)
                                                             .getDeathPositions();
        if (deathPositions.isEmpty()) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.backdeath.nodeath"));
            return;
        }
        Transform lastDeath = deathPositions.getLast()
                                            .getTransform();
        assert player.getReference() != null;
        PlayerTool.teleportPlayerNoRotation(player.getReference(), lastDeath);
        PlayerTool.recordPlayerTransformHistoryNoRotation(PlayerTool.getUUID(store, player.getReference())
                                                                    .toString(), lastDeath);
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
