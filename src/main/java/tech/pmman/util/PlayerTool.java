package tech.pmman.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.ConfigManager;
import tech.pmman.DbManager;
import tech.pmman.dao.mapper.PlayerTeleportHistoryMapper;
import tech.pmman.pojo.Location;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.pojo.db.PlayerTeleportHistory;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class PlayerTool {
    public static Transform getTransform(Store<EntityStore> store, Ref<EntityStore> playerRef) {
        return Objects.requireNonNull(store.getComponent(playerRef, TransformComponent.getComponentType()))
                      .getTransform()
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

    public static void teleportPlayer(Ref<EntityStore> playerRef, World world, Vector3d position, Vector3f rotation) {
        Store<EntityStore> store = playerRef.getStore();
        store.addComponent(playerRef, Teleport.getComponentType(), new Teleport(world, position, rotation));
    }

    /**
     * 记录传送历史记录
     *
     * @param playerUUID 玩家uuid
     * @param position   位置信息
     * @param rotation   旋转信息
     */
    public static void recordPlayerTransformHistory(String playerUUID, String worldUUID, Vector3d position, Vector3f rotation) {
        // 添加传送记录
        DbManager.getInstance()
                 .get()
                 .useExtension(PlayerTeleportHistoryMapper.class, dao -> {
                     PlayerTeleportHistory insertDo = new PlayerTeleportHistory();
                     insertDo.setUuid(playerUUID);
                     insertDo.setWorldUUID(worldUUID);
                     insertDo.setLocation(new Location(position, rotation));
                     dao.insertTeleportHistory(insertDo);
                 });
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
