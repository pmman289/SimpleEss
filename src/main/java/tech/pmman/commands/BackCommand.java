package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.ConfigManager;
import tech.pmman.DbManager;
import tech.pmman.dao.mapper.PlayerTeleportHistoryMapper;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.pojo.db.PlayerTeleportHistory;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public class BackCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.back";

    public BackCommand() {
        super("back", "simpleEssCommand.back.desc");
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        String uuid = playerRef.getUuid()
                               .toString();
        DbManager.getInstance()
                 .get()
                 .useExtension(PlayerTeleportHistoryMapper.class, dao -> {
                     PlayerTeleportHistory lastTeleportHistory = dao.queryLastTeleportHistory(uuid);
                     if (lastTeleportHistory == null) {
                         MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.back.execute"));
                         return;
                     }
                     // 判断世界是否在线
                     World targetWorld = Universe.get()
                                                 .getWorld(UUID.fromString(lastTeleportHistory.getWorldUUID()));
                     if (targetWorld == null || !targetWorld.isAlive()) {
                         MessageTool.sendPluginMessage(playerRef, Message.translation("simpleEssCore.teleport.targetWorldNotOnline"));
                         return;
                     }
                     PlayerTool.teleportPlayer(ref, world, lastTeleportHistory.getLocationObj()
                                                                              .getPosition(), lastTeleportHistory.getLocationObj()
                                                                                                                 .getRotation());
                 });
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
