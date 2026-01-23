package tech.pmman.service;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.SavedMovementStates;
import com.hypixel.hytale.protocol.packets.player.SetMovementStates;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.ecs.compoent.FlyComponent;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;

public class FlyService {
    public static void fly(@Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref,
                           Boolean mode) {
        MovementManager movementManager = store.getComponent(ref, MovementManager.getComponentType());
        if (movementManager == null) {
            MessageTool.sendPluginMessage(ref, Message.translation("simpleEssCore.unknownError")
                                                      .param("errorMsg", "MoveManager component is null"));
            return;
        }
        boolean flyResult = mode == null ? !movementManager.getSettings().canFly : mode;
        movementManager.getSettings().canFly = flyResult;
        PlayerRef playerRef = PlayerTool.getPlayerRef(store, ref);
        movementManager.update(playerRef.getPacketHandler());
        // 挂组件
        if (flyResult) {
            store.ensureAndGetComponent(ref, FlyComponent.getComponentType());
        } else {
            store.removeComponentIfExists(ref, FlyComponent.getComponentType());
            // 这里需要发包取消玩家的飞行状态
            playerRef.getPacketHandler().writeNoCache(new SetMovementStates(new SavedMovementStates(false)));
        }
        MessageTool.sendPluginMessage(ref, Message.translation("simpleEssCommand.fly.execute")
                                                  .param("player", playerRef.getUsername())
                                                  .param("mode", movementManager.getSettings().canFly));
    }
}
