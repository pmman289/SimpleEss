package tech.pmman.service;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import tech.pmman.ConfigManager;
import tech.pmman.DbManager;
import tech.pmman.dao.mapper.PlayerHomeMapper;
import tech.pmman.pojo.Location;
import tech.pmman.pojo.db.PlayerHomeEntry;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;
import java.util.List;

public class SetHomeService {
    public static void setHome(String homeName, @Nonnull CommandContext commandContext,
                               @Nonnull PlayerRef playerRef, @Nonnull World world) {
        PermissionsModule permissionsModule = PermissionsModule.get();
        String uuid = playerRef.getUuid()
                               .toString();
        String worldUUID = world.getWorldConfig()
                                .getUuid()
                                .toString();
        Transform transform = playerRef.getTransform()
                                       .clone();
        Location location = new Location(transform.getPosition(), transform.getRotation());
        DbManager.getInstance()
                 .get()
                 .useExtension(PlayerHomeMapper.class, dao -> {
                     List<PlayerHomeEntry> homeList = dao.queryPlayerHomes(uuid);
                     // 如果已有同名home，则覆盖
                     PlayerHomeEntry existHome = homeList.stream()
                                                         .filter(h -> h.getHomeName()
                                                                       .equals(homeName))
                                                         .findFirst()
                                                         .orElse(null);
                     if (existHome != null) {
                         existHome.setWorldUUID(worldUUID);
                         existHome.setLocation(location);
                         dao.updateLocationAndWorld(existHome);
                         MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.execute"));
                         return;
                     }
                     // 限制不能超过配置中的最大home数量，如果有bypass权限就不限制
                     if (homeList.size() >= ConfigManager.PLUGIN_CONFIG.get()
                                                                       .getMaxHome() &&
                             !permissionsModule.hasPermission(playerRef.getUuid(), "simpleess.command.sethome.limit.bypass")) {
                         MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.outOfMaxHome")
                                                                              .param("maxHome", ConfigManager.PLUGIN_CONFIG.get()
                                                                                                                           .getMaxHome()));
                         return;
                     }
                     PlayerHomeEntry insertDo = new PlayerHomeEntry();
                     insertDo.setUuid(uuid);
                     insertDo.setHomeName(homeName);
                     insertDo.setWorldUUID(worldUUID);
                     insertDo.setLocation(location);
                     dao.insert(insertDo);
                     MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.execute"));
                 });
    }
}
