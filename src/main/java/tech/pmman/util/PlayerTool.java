package tech.pmman.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.SimpleEssPlugin;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class PlayerTool {
    public static Transform getTransform(Store<EntityStore> store, Ref<EntityStore> playerRef) {
        return Objects.requireNonNull(store.getComponent(playerRef, TransformComponent.getComponentType()))
                      .getTransform()
                      .clone();
    }

    public static Vector3f getHeadRotation(Store<EntityStore> store, Ref<EntityStore> playerRef) {
        return Objects.requireNonNull(store.getComponent(playerRef, HeadRotation.getComponentType()))
                      .getRotation()
                      .clone();
    }

    public static UUID getUUID(Store<EntityStore> store, Ref<EntityStore> playerRef) {
        return Objects.requireNonNull(store.getComponent(playerRef, UUIDComponent.getComponentType()))
                      .getUuid();
    }

    public static String getNameByUUID(UUID uuid) {
        PlayerRef player = Universe.get()
                                   .getPlayer(uuid);
        if (player == null || !player.isValid()) return "";
        return player.getUsername();
    }

    /**
     * 传送玩家到指定位置，不使用旋转参数
     *
     * @param playerRef       玩家引用
     * @param targetTransform 目标位置
     */
    public static void teleportPlayerNoRotation(Ref<EntityStore> playerRef, Transform targetTransform) {
        Store<EntityStore> store = playerRef.getStore();
        store.addComponent(playerRef, Teleport.getComponentType(), new Teleport(targetTransform.getPosition(), targetTransform.getRotation()));
    }

    /**
     * 记录玩家传送记录，不记录视角参数
     *
     * @param playerUUID 玩家uuid
     * @param transform  位置
     */
    public static void recordPlayerTransformHistoryNoRotation(String playerUUID, Transform transform) {
        // 添加传送记录，不记录视角
        SimpleEssPlugin.playerLastTeleportConfig.get()
                                                .getLastTeleportData()
                                                .put(playerUUID, transform);
    }

    /**
     * 通过引用获取玩家对象
     *
     * @param playerRef 玩家引用
     * @return 玩家对象
     */
    public static Player getPlayerFromRef(@Nonnull Ref<EntityStore> playerRef) {
        return playerRef.getStore()
                        .getComponent(playerRef, Player.getComponentType());
    }

    public static String getPlayerDisplayName(@Nonnull Ref<EntityStore> playerRef) {
        Player player = playerRef.getStore()
                                 .getComponent(playerRef, Player.getComponentType());
        if (player == null) return "";
        return player.getDisplayName();
    }
}
