package tech.pmman.service;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;
import java.util.Objects;

public class FlyService {
    public static void fly(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef, Boolean mode) {
        MovementManager movementManager = store.getComponent(ref, MovementManager.getComponentType());
        if (movementManager == null) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCore.unknownError")
                                                                 .param("errorMsg", "MoveManager component is null"));
            return;
        }
        movementManager.getSettings().canFly = Objects.requireNonNullElseGet(mode, () -> !movementManager.getSettings().canFly);
        movementManager.update(playerRef.getPacketHandler());
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.fly.execute")
                                                             .param("player", playerRef.getUsername())
                                                             .param("mode", movementManager.getSettings().canFly));
    }
}
