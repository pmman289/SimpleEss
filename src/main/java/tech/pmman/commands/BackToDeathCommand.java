package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.data.PlayerDeathPositionData;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.List;

public class BackToDeathCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.backdeath";

    public BackToDeathCommand() {
        super("backdeath", "simpleEssCommand.backdeath.desc");
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
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
                                                         .toString(), world.getWorldConfig()
                                                                           .getUuid()
                                                                           .toString(), lastDeath.getPosition(),
                lastDeath.getRotation());
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.backdeath.execute"));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
